package br.com.ronistone.stonebank.domain

import com.google.gson.Gson
import java.math.BigDecimal

fun BigDecimal.isLessThan(
    value: BigDecimal
) = this.compareTo(value) == -1

fun BigDecimal.isGreaterThan(
    value: BigDecimal
) = this.compareTo(value) == 1

fun <T> String.jsonToObject(t: Class<T>): T =
    Gson().fromJson(this, t)