package com.halkyproject.pausemenu.singletons

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.os.AsyncTask
import com.halkyproject.pausemenu.database.MainDatabase
import com.halkyproject.pausemenu.database.entities.GeneralConfig
import java.util.*


class ConfigSingleton private constructor() {
    companion object {
        private var _instance: ConfigSingleton? = null
        fun getInstance(): ConfigSingleton {
            if (_instance == null) {
                _instance = ConfigSingleton()
            }
            return _instance as ConfigSingleton
        }

        private const val KEY_DT_NASC = "dtNasc";
        private const val KEY_HR_NASC = "hrNasc";
        private const val KEY_TZ_NASC = "TZNasc";

        private class RunUpdateNascDate : AsyncTask<Calendar, Void, Void>() {
            override fun doInBackground(vararg value: Calendar): Void? {
                if (value.isNotEmpty()) {
                    val db = MainDatabase.getInstance().generalConfigDao()
                    val cal = value[0];
                    db.insertAll(
                            GeneralConfig(KEY_DT_NASC, (cal.get(Calendar.YEAR) * 10000 + cal.get(Calendar.MONTH) * 100 + cal.get(Calendar.DAY_OF_MONTH)).toString())
                    )
                    cachedBirthDate?.set(Calendar.YEAR, cal.get(Calendar.YEAR))
                    cachedBirthDate?.set(Calendar.MONTH, cal.get(Calendar.MONTH))
                    cachedBirthDate?.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH))
                }
                return null
            }
        }

        private class RunUpdateNascHour : AsyncTask<Calendar, Void, Void>() {
            override fun doInBackground(vararg value: Calendar): Void? {
                if (value.isNotEmpty()) {
                    val db = MainDatabase.getInstance().generalConfigDao()
                    val cal = value[0];
                    db.insertAll(
                            GeneralConfig(KEY_HR_NASC, (cal.get(Calendar.HOUR_OF_DAY) * 100 + cal.get(Calendar.MINUTE)).toString())
                    )
                    cachedBirthDate?.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY))
                    cachedBirthDate?.set(Calendar.MINUTE, cal.get(Calendar.MINUTE))
                }
                return null
            }
        }

        private class RunUpdateNascTimeZone : AsyncTask<TimeZone, Void, Void>() {
            override fun doInBackground(vararg value: TimeZone): Void? {
                if (value.isNotEmpty()) {
                    val db = MainDatabase.getInstance().generalConfigDao()
                    val tz = value[0];
                    db.insertAll(
                            GeneralConfig(KEY_TZ_NASC, tz.id)
                    )

                    val oldOffset = cachedBirthDate?.timeZone?.rawOffset ?: 0
                    cachedBirthDate?.timeZone = TimeZone.getTimeZone(tz.id)
                    val deltaOffset = oldOffset - (cachedBirthDate?.timeZone?.rawOffset ?: 0)
                    cachedBirthDate?.timeInMillis = (cachedBirthDate?.timeInMillis ?: 0) + deltaOffset
                }
                return null
            }
        }

        private var cachedBirthDate: Calendar? = null
    }

    fun getBirthDateCache(): Calendar? {
        return cachedBirthDate;
    }

    fun getBirthDate(): LiveData<Calendar> {
        val mMediatorLiveData = MediatorLiveData<Calendar>()
        if (cachedBirthDate == null) {
            val result = Calendar.getInstance()
            val db = MainDatabase.getInstance().generalConfigDao()
            val dtNasc = db.getByKey(KEY_DT_NASC)
            val hrNasc = db.getByKey(KEY_HR_NASC)
            val tzNasc = db.getByKey(KEY_TZ_NASC)
            mMediatorLiveData.addSource(dtNasc) { dt ->
                if (dt != null) {
                    val dtNascValue = dt.value.toInt()
                    result.set(dtNascValue / 10000, (dtNascValue / 100) % 100, dtNascValue % 100)
                }
                mMediatorLiveData.removeSource(dtNasc)
                mMediatorLiveData.setValue(result)
            }
            mMediatorLiveData.addSource(hrNasc) { hr ->
                if (hr != null) {
                    val hrNascValue = hr.value.toInt()
                    result.set(Calendar.HOUR_OF_DAY, hrNascValue / 100)
                    result.set(Calendar.MINUTE, hrNascValue % 100)
                }
                result.set(Calendar.SECOND, 0)
                cachedBirthDate = result
                mMediatorLiveData.removeSource(hrNasc)
                mMediatorLiveData.setValue(result)
            }
            mMediatorLiveData.addSource(tzNasc) { tz ->
                if (tz?.value != null) {
                    val oldOffset = result.timeZone.rawOffset
                    result.timeZone = TimeZone.getTimeZone(tz.value)
                    val deltaOffset = oldOffset - result.timeZone.rawOffset
                    result.timeInMillis = result.timeInMillis + deltaOffset
                }
                cachedBirthDate = result
                mMediatorLiveData.removeSource(tzNasc)
                mMediatorLiveData.setValue(result)
            }

            return mMediatorLiveData
        } else {
            mMediatorLiveData.value = cachedBirthDate
            return mMediatorLiveData
        }
    }


    fun setBirthDate(value: Calendar) {
        RunUpdateNascDate().execute(value)
    }

    fun setBirthHour(value: Calendar) {
        RunUpdateNascHour().execute(value)
    }


    fun setBirthTimeZone(value: TimeZone) {
        RunUpdateNascTimeZone().execute(value)
    }

}