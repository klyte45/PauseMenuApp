package com.halkyproject.pausemenu.singletons

import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import android.os.Build
import android.support.annotation.RequiresApi
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.widget.EditText
import java.math.BigDecimal
import java.util.*


object FormatSingleton {
    fun getDecimalFormattedString(value: String, decimalSeparator: Char, thousandSeparator: Char): String {
        if (!value.equals("", ignoreCase = true)) {
            val lst = StringTokenizer(value, "$decimalSeparator")
            var str1: String = value
            var str2 = ""
            if (lst.countTokens() > 1) {
                str1 = lst.nextToken()
                str2 = lst.nextToken()
            }
            var str3 = ""
            var i = 0
            var j = -1 + str1.length
            if (str1[-1 + str1.length] == decimalSeparator) {
                j--
                str3 = "$decimalSeparator"
            }
            var k = j
            while (true) {
                if (k < 0) {
                    if (str2.isNotEmpty())
                        str3 = "$str3$decimalSeparator$str2"
                    return str3
                }
                if (i == 3 && !(k == 0 && str1[0] == '-')) {
                    str3 = "$thousandSeparator$str3"
                    i = 0
                }
                str3 = str1[k] + str3
                i++
                k--
            }
        }
        return ""
    }

    fun toBigDecimal(value: String, decimalSeparator: Char, thousandSeparator: Char): BigDecimal {
        if (!value.equals("", ignoreCase = true)) {
            return BigDecimal(value.replace("$thousandSeparator", "").replace("$decimalSeparator", ".").replace(Regex("/[^0-9.-]/"), ""))
        }
        return BigDecimal.ZERO
    }

    val FORMAT_CPF = "###.###.###-##"
    val FORMAT_CNPJ = "##.###.###/####-##"
    val FORMAT_FONE = "(###)####-#####"
    val FORMAT_CEP = "#####-###"
    val FORMAT_DATE = "##/##/####"
    val FORMAT_HOUR = "##:##"
    val FORMAT_FINANCIAL_ACCOUNT = "##.######-#"

    /**
     * Método que deve ser chamado para realizar a formatação
     *
     * @param ediTxt
     * @param mask
     * @return
     */
    fun mask(ediTxt: EditText, mask: String): TextWatcher {
        return object : TextWatcher {
            internal var isUpdating: Boolean = false
            internal var old = ""

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val str = unmask(s.toString())
                var mascara = ""
                if (isUpdating) {
                    old = str
                    isUpdating = false
                    return
                }
                var i = 0
                for (m in mask.toCharArray()) {
                    if (m != '#' && str.length > old.length) {
                        mascara += m
                        continue
                    }
                    try {
                        mascara += str.get(i)
                    } catch (e: Exception) {
                        break
                    }

                    i++
                }
                isUpdating = true
                ediTxt.setText(mascara)
                ediTxt.setSelection(mascara.length)
            }
        }
    }

    fun mask(str: String, mask: String): String {
        val mascara = StringBuilder()
        val old = ""
        var i = 0
        for (m in mask.toCharArray()) {
            if (m != '#' && str.length > old.length) {
                mascara.append(m)
                continue
            }
            try {
                mascara.append(str[i])
            } catch (e: Exception) {
                break
            }

            i++
        }

        return mascara.toString()
    }

    fun unmask(s: String): String {
        return s.replace("[.]".toRegex(), "").replace("[-]".toRegex(), "").replace("[/]".toRegex(), "").replace("[(]".toRegex(), "").replace("[ ]".toRegex(), "").replace("[:]".toRegex(), "").replace("[)]".toRegex(), "")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    internal class MoneyValueFilter(private val digits: Int, private val locale: Locale) : DigitsKeyListener(locale, true, true) {
        override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence {
            var src = source
            var st = start
            var ed = end
            val out = super.filter(src, st, ed, dest, dstart, dend)

            // if changed, replace the source
            if (out != null) {
                src = out
                st = 0
                ed = out.length
            }

            val len = ed - st

            // if deleting, source is empty
            // and deleting can't break anything
            if (len == 0) {
                return src
            }

            val dlen = dest.length
            val nf = NumberFormat.getInstance() as DecimalFormat

            // Find the position of the decimal .
            for (i in 0 until dstart) {
                if (dest[i] == '.') {
                    // being here means, that a number has
                    // been inserted after the dot
                    // check if the amount of digits is right
                    return getDecimalFormattedString(if (dlen - (i + 1) + len > digits) "" else SpannableStringBuilder(src, st, ed).toString(), nf.decimalFormatSymbols.decimalSeparator, nf.decimalFormatSymbols.groupingSeparator)
                }
            }

            for (i in st until ed) {
                if (src[i] == '.') {
                    // being here means, dot has been inserted
                    // check if the amount of digits is right
                    return if (dlen - dend + (ed - (i + 1)) > digits)
                        ""
                    else
                        break // return new SpannableStringBuilder(source,
                    // start, end);
                }
            }

            // if the dot is after the inserted part,
            // nothing can break
            return getDecimalFormattedString(SpannableStringBuilder(src, st, ed).toString(), nf.decimalFormatSymbols.decimalSeparator, nf.decimalFormatSymbols.groupingSeparator)
        }
    }
}