package com.easysport.assistantappnewmb

import kotlin.math.abs
import kotlin.math.roundToInt

object OddsUtils {

    fun parseToDecimal(input: String, format: String): Double? {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return null
        return when (format) {
            "decimal" -> trimmed.toDoubleOrNull()?.takeIf { it > 1.0 }
            "american" -> parseAmericanToDecimal(trimmed)
            "fractional" -> parseFractionalToDecimal(trimmed)
            else -> null
        }
    }

    fun parseAutoToDecimal(input: String): Double? {
        val trimmed = input.trim()
        return when {
            trimmed.contains("/") -> parseFractionalToDecimal(trimmed)
            trimmed.startsWith("+") || (trimmed.startsWith("-") && trimmed.length > 1 && trimmed[1].isDigit()) ->
                parseAmericanToDecimal(trimmed)
            else -> trimmed.toDoubleOrNull()?.takeIf { it > 1.0 }
        }
    }

    private fun parseAmericanToDecimal(input: String): Double? {
        val value = input.toIntOrNull() ?: return null
        return when {
            value > 0 -> (value / 100.0) + 1.0
            value < 0 -> (100.0 / abs(value.toDouble())) + 1.0
            else -> null
        }
    }

    private fun parseFractionalToDecimal(input: String): Double? {
        val parts = input.split("/")
        if (parts.size != 2) return null
        val num = parts[0].trim().toDoubleOrNull() ?: return null
        val den = parts[1].trim().toDoubleOrNull() ?: return null
        if (den == 0.0) return null
        return (num / den) + 1.0
    }

    fun decimalToAmerican(decimal: Double): String {
        return if (decimal >= 2.0) {
            "+${((decimal - 1) * 100).roundToInt()}"
        } else {
            "${(-100.0 / (decimal - 1)).roundToInt()}"
        }
    }

    fun decimalToFractional(decimal: Double): String {
        val num = ((decimal - 1) * 100).roundToInt()
        val den = 100
        val g = gcd(abs(num), den)
        return "${num / g}/${den / g}"
    }

    fun impliedProbability(decimal: Double): Double = (1.0 / decimal) * 100.0

    private fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
}
