package com.halkyproject.pausemenu.singletons.finances

import android.icu.text.DecimalFormat
import android.os.AsyncTask
import android.util.Log
import com.google.gson.reflect.TypeToken
import com.halkyproject.lifehack.controller.FinancesController.Companion.ACTIVE_PREVISIONS_URI
import com.halkyproject.lifehack.controller.FinancesController.Companion.CURRENT_BALANCE_URI
import com.halkyproject.lifehack.controller.FinancesController.Companion.URI_BASE
import com.halkyproject.lifehack.model.aggregation.finances.CurrentBalanceReportItem
import com.halkyproject.lifehack.model.aggregation.finances.MovementSourcePrevisionGroup
import com.halkyproject.lifehack.model.enums.Currency
import com.halkyproject.lifehack.model.finances.FinancialAccount
import com.halkyproject.lifehack.model.finances.MovementSource
import com.halkyproject.lifehack.model.finances.MovementSourcePrevision
import com.halkyproject.pausemenu.auxobjs.finances.ExtractEntry
import com.halkyproject.pausemenu.extensions.getFunction
import com.halkyproject.pausemenu.singletons.HttpService
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.util.*
import kotlin.math.max
import kotlin.math.min

object FinancesService {

    val DEFAULT_FORMATTER = DecimalFormat("#,##0.00 +;#,##0.00 -")

    fun getCurrentBalances(): Map<Currency, List<CurrentBalanceReportItem>> {
        val resultMap = HashMap<Currency, MutableList<CurrentBalanceReportItem>>()
        val getResponse = HttpService.MakeGetTo(URI_BASE + CURRENT_BALANCE_URI, object : TypeToken<List<CurrentBalanceReportItem>>() {}).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR).get()
        if (getResponse != null) {
            getResponse.first!!.forEach {
                if (resultMap[it._id.currency] == null) resultMap[it._id.currency] = arrayListOf(it)
                else resultMap[it._id.currency]!! += it
            }
            return resultMap
        }
        return mapOf()
    }

    fun getCurrentBalancePhysicalLocal(): BigDecimal {
        val balances = getCurrentBalances()
        if (balances.containsKey(Currency.BRL)) {
            var result: BigDecimal = BigDecimal.ZERO
            for (item in balances[Currency.BRL]!!) {
                when (item._id.type) {
                    FinancialAccount.AccountType.SAVINGS,
                    FinancialAccount.AccountType.CURRENT -> result += item.currentBalance
                    else -> {
                    }
                }
            }
            return result
        }
        return BigDecimal.ZERO
    }

    private fun getMovementPrevisionStackForDates(startDate: Date, endDate: Date): Pair<List<Pair<Date, MovementSourcePrevision>>, Map<Int, MovementSource>> {
        val previsionsDate = getActiveMovementPrevisions()
        val pass1 = previsionsDate.map {
            getEffectiveEventDatesForGroup(it, startDate, endDate)

        }
        val first = pass1.map { it.first }.flatten().sortedBy { it.first }
        val second = pass1.asSequence().map { it.second }.filterNotNull().map { it.id!! to it }.toMap()
        return Pair(first, second)

    }

    fun doProjection(startDate: Date, endDate: Date): Pair<Map<Int, FinancialAccount>, List<ExtractEntry>> {
//        //Log.w("XXX", "startProjection for $startDate a $endDate")
        val stackPair = getMovementPrevisionStackForDates(startDate, endDate)
        val stack = stackPair.first
        val sources = stackPair.second
        val accountsMap = sources.values.map {
            listOf(it.inAccount, it.outAccount)
        }.flatten().asSequence().filterNotNull().distinctBy {
            it.id
        }.map {
            it.id!! to it
        }.toMap()
        val extractList = arrayListOf<ExtractEntry>()
        for (item in stack.sortedWith(compareBy({ it.first }, { it.second.valueType.ordinal }))) {
            if (item.first.time < startDate.time) continue
            var resultPair = item.second.valueType.getFunction()(
                    accountsMap[sources[item.second.movSourceId]!!.outAccountId]!!,
                    accountsMap[sources[item.second.movSourceId]!!.inAccountId]!!,
                    item.second.value ?: BigDecimal.ZERO
            )
            val inAccount = accountsMap[sources[item.second.movSourceId]!!.inAccountId]!!
            if (inAccount.type == FinancialAccount.AccountType.DEBT) {
                if (inAccount.balance!! >= BigDecimal.ZERO) {
                    continue
                } else if (inAccount.balance!! + resultPair.second >= BigDecimal.ZERO) {
                    resultPair = Pair((resultPair.second / resultPair.first).abs() * inAccount.balance!!, inAccount.balance!!.abs())
                }
            }
            accountsMap[sources[item.second.movSourceId]!!.outAccountId]!!.balance = (accountsMap[sources[item.second.movSourceId]!!.outAccountId]!!.balance
                    ?: BigDecimal.ZERO).plus(resultPair.first)

            accountsMap[sources[item.second.movSourceId]!!.inAccountId]!!.balance = (accountsMap[sources[item.second.movSourceId]!!.inAccountId]!!.balance
                    ?: BigDecimal.ZERO).plus(resultPair.second)

            extractList.add(ExtractEntry(
                    item.first,
                    resultPair.first,
                    resultPair.second,
                    accountsMap[sources[item.second.movSourceId]!!.outAccountId]!!.balance!!,
                    accountsMap[sources[item.second.movSourceId]!!.inAccountId]!!.balance!!,
                    sources[item.second.movSourceId]!!.outAccountId!!,
                    sources[item.second.movSourceId]!!.inAccountId!!,
                    sources[item.second.movSourceId]!!.name
            ))
        }
        return Pair(accountsMap, extractList)
    }

    private fun getActiveMovementPrevisions(): List<MovementSourcePrevisionGroup> {
        return HttpService.MakeGetTo(URI_BASE + ACTIVE_PREVISIONS_URI, object : TypeToken<List<MovementSourcePrevisionGroup>>() {}).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR).get()?.first
                ?: listOf()
    }

    private fun getEffectiveEventDatesForGroup(group: MovementSourcePrevisionGroup, startDate: Date, endDate: Date): Pair<List<Pair<Date, MovementSourcePrevision>>, MovementSource?> {
        //Log.w("XXX", "getEffectiveEventDatesForGroup $startDate a $endDate")
        if (group.values.isEmpty()) return Pair(listOf(), null)
        val startTimeMillis = startDate.time / 86400000L * 86400000L
        val endTimeMillis = endDate.time / 86400000L * 86400000L
        val movSource = group.movSource
        val values = group.values.asSequence().map { it.startDate = Date((it.startDate.time / 86400000L) * 86400000L); it }.filter { it.startDate.time <= endTimeMillis }.sortedBy { it.startDate }.toMutableList()
        if (values.isEmpty()) return Pair(listOf(), movSource)
        while (values.size > 1 && values[1].startDate.time < startTimeMillis) {
            values.removeAt(0)
        }
        if (values.size == 1) {
            if (values[0].frequency == MovementSourcePrevision.Frequency.DISABLED) return Pair(listOf(), movSource)
            if (values[0].frequency == MovementSourcePrevision.Frequency.ONCE && values[0].startDate.time < startTimeMillis) return Pair(listOf(), movSource)
        }
        val currentDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        currentDate.timeInMillis = max(startTimeMillis - 1, values[0].startDate.time - 1)
        //Log.w("XXX", "currentDate ${currentDate.time}")
        var nextIdx = 1
        val resultList: MutableList<Pair<Date, MovementSourcePrevision>> = arrayListOf()
        do {
            if (nextIdx < values.size) {
                if (currentDate.timeInMillis > values[nextIdx].startDate.time) {
                    nextIdx++
                    continue
                }
            }
            val currValue = values[nextIdx - 1]
            val nextOccurrence: Long = calculateNextDateOccurence(currentDate, currValue)
                    ?: if (nextIdx < values.size) {
                        nextIdx++
                        continue
                    } else break

            if (nextOccurrence > endDate.time) break
            resultList.add(Pair(Date(nextOccurrence), currValue))
            currentDate.timeInMillis = nextOccurrence

            //Log.w("XXX", "NOT END DATE? ${currentDate.timeInMillis} < ${endDate.time} = ${currentDate.timeInMillis < endDate.time}")
        } while (currentDate.timeInMillis < endDate.time)
        //Log.w("XXX", "Size resultList = ${resultList.size}")
        return Pair(resultList, movSource)

    }

    private fun calculateNextDateOccurence(currentDate: Calendar, prev: MovementSourcePrevision): Long? {
        val origTimeMillis = currentDate.timeInMillis
        val date = Calendar.getInstance(currentDate.timeZone)
        date.timeInMillis = currentDate.timeInMillis
        when (prev.frequency) {
            MovementSourcePrevision.Frequency.MONTHLY -> {
                val currentDay = date[Calendar.DAY_OF_MONTH]
                val currMonthLength = YearMonth.of(date[Calendar.YEAR], date[Calendar.MONTH] + 1).lengthOfMonth()
                val refday = min(prev.refDay!!, currMonthLength)
                if (prev.refDayUtil) {
                    val currentMonthTry = calculateBusinessDay(date, refday, prev) ?: return null
                    if (currentMonthTry <= origTimeMillis) {
                        date.add(Calendar.MONTH, 1)
                        return calculateBusinessDay(date, refday, prev)
                    }
                    return currentMonthTry
                } else {
                    date.set(Calendar.DAY_OF_MONTH, refday)
                    if (refday >= currentDay) {
                        date.add(Calendar.MONTH, 1)
                    }
                    return date.timeInMillis
                }
            }
            MovementSourcePrevision.Frequency.WEEKLY -> {
                val ld = LocalDate.of(date[Calendar.YEAR], date[Calendar.MONTH], date[Calendar.DAY_OF_MONTH])
                val refday = if (prev.refDay!! == 0) 7 else prev.refDay!!
                return GregorianCalendar.from(ld.with(TemporalAdjusters.next(DayOfWeek.of(refday))).atStartOfDay(ZoneId.of("GMT"))).timeInMillis
            }
            MovementSourcePrevision.Frequency.FIFTHLY -> {
                val currentDay = date[Calendar.DAY_OF_MONTH]
                val currMontLength = YearMonth.of(date[Calendar.YEAR], date[Calendar.MONTH] + 1).lengthOfMonth()
                val secDateMonth = min(prev.refDay!! + 15, currMontLength)

                if (currentDay < prev.refDay!!) {
                    date.set(Calendar.DAY_OF_MONTH, prev.refDay!!)
                } else if (currentDay >= prev.refDay!! && currentDay <= 15 && currentDay < secDateMonth) {
                    date.set(Calendar.DAY_OF_MONTH, secDateMonth)
                } else {
                    date.set(Calendar.DAY_OF_MONTH, prev.refDay!!)
                    date.add(Calendar.MONTH, 1)
                }
                return date.timeInMillis
            }
            MovementSourcePrevision.Frequency.YEARLY -> {
                date.set(Calendar.DAY_OF_MONTH, prev.refDay!!)
                date.set(Calendar.MONTH, prev.refMonth!!)
                while (date.timeInMillis <= origTimeMillis) {
                    date.add(Calendar.YEAR, 1)
                }
                return date.timeInMillis
            }
            MovementSourcePrevision.Frequency.DAILY -> {
                date.add(Calendar.DAY_OF_MONTH, 1)
                return date.timeInMillis
            }
            MovementSourcePrevision.Frequency.ONCE -> {
                return if (prev.startDate.time <= date.timeInMillis) null
                else prev.startDate.time
            }
            MovementSourcePrevision.Frequency.DISABLED -> return null
        }
    }

    private fun calculateBusinessDay(startDate: Calendar, refday: Int, prev: MovementSourcePrevision): Long? {
        val currentMonth = startDate[Calendar.MONTH]
        startDate.set(Calendar.DAY_OF_MONTH, 1)
        val weekDayFirst = startDate[Calendar.DAY_OF_WEEK]
        startDate.set(Calendar.DAY_OF_MONTH, refday)
        val weekDayTarget = startDate[Calendar.DAY_OF_WEEK]
        var additionalSum = (if (weekDayFirst == Calendar.SUNDAY) 1 else 0) + (prev.refDay!! / 7)
        if (additionalSum > 7) additionalSum += additionalSum / 7
        startDate.add(Calendar.DAY_OF_MONTH, additionalSum)
        if (weekDayTarget > startDate[Calendar.DAY_OF_WEEK]) startDate.add(Calendar.DAY_OF_MONTH, 1)
        if (startDate[Calendar.DAY_OF_WEEK] == Calendar.SUNDAY) startDate.add(Calendar.DAY_OF_MONTH, 1)
        if (currentMonth != startDate[Calendar.MONTH]) return null
        return startDate.timeInMillis
    }
}