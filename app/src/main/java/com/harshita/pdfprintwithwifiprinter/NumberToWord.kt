package com.harshita.pdfprintwithwifiprinter

import com.harshita.pdfprintwithwifiprinter.NumberToWord
import java.text.DecimalFormat
import kotlin.jvm.JvmStatic

object NumberToWord {
    private val tensNames = arrayOf(
        "",
        " ten",
        " twenty",
        " thirty",
        " forty",
        " fifty",
        " sixty",
        " seventy",
        " eighty",
        " ninety"
    )
    private val numNames = arrayOf(
        "",
        " one",
        " two",
        " three",
        " four",
        " five",
        " six",
        " seven",
        " eight",
        " nine",
        " ten",
        " eleven",
        " twelve",
        " thirteen",
        " fourteen",
        " fifteen",
        " sixteen",
        " seventeen",
        " eighteen",
        " nineteen"
    )

    private fun convertLessThanOneThousand(number: Int): String {
        var number = number
        var soFar: String
        if (number % 100 < 20) {
            soFar = numNames[number % 100]
            number /= 100
        } else {
            soFar = numNames[number % 10]
            number /= 10
            soFar = tensNames[number % 10] + soFar
            number /= 10
        }
        return if (number == 0) soFar else numNames[number] + " hundred" + soFar
    }

    fun convert(number: Long): String {
        // 0 to 999 999 999 999
        if (number == 0L) {
            return "zero"
        }
        var snumber = java.lang.Long.toString(number)

        // pad with "0"
        val mask = "000000000000"
        val df = DecimalFormat(mask)
        snumber = df.format(number)

        // XXXnnnnnnnnn
        val billions = snumber.substring(0, 3).toInt()
        // nnnXXXnnnnnn
        val millions = snumber.substring(3, 6).toInt()
        // nnnnnnXXXnnn
        val hundredThousands = snumber.substring(6, 9).toInt()
        // nnnnnnnnnXXX
        val thousands = snumber.substring(9, 12).toInt()
        val tradBillions: String
        tradBillions = when (billions) {
            0 -> ""
            1 -> convertLessThanOneThousand(billions)+" billion "
            else -> convertLessThanOneThousand(billions)+" billion "
        }
        var result = tradBillions
        val tradMillions: String
        tradMillions = when (millions) {
            0 -> ""
            1 -> convertLessThanOneThousand(millions)+" million "
            else -> convertLessThanOneThousand(millions)+" million "
        }
        result = result + tradMillions
        val tradHundredThousands: String
        tradHundredThousands = when (hundredThousands) {
            0 -> ""
            1 -> "one thousand "
            else -> convertLessThanOneThousand(hundredThousands)+" thousand "
        }
        result = result + tradHundredThousands
        val tradThousand: String
        tradThousand = convertLessThanOneThousand(thousands)
        result = result + tradThousand

        // remove extra spaces!
        return result.replace("^\\s+".toRegex(), "").replace("\\b\\s{2,}\\b".toRegex(), " ")
    }

    /**
     * testing
     *
     * @param args
     */
    @JvmStatic
    fun main(args: Array<String>) {
        println("*** " + convert(0))
        println("*** " + convert(1))
        println("*** " + convert(16))
        println("*** " + convert(100))
        println("*** " + convert(118))
        println("*** " + convert(200))
        println("*** " + convert(219))
        println("*** " + convert(800))
        println("*** " + convert(801))
        println("*** " + convert(1316))
        println("*** " + convert(1000000))
        println("*** " + convert(2000000))
        println("*** " + convert(3000200))
        println("*** " + convert(700000))
        println("*** " + convert(9000000))
        println("*** " + convert(9001000))
        println("*** " + convert(123456789))
        println("*** " + convert(2147483647))
        println("*** " + convert(3000000010L))
    }
}