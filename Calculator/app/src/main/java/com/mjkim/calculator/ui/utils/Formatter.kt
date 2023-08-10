package com.mjkim.calculator.ui.utils

fun Double.formattedString(): String =
    if (this.toInt().toDouble() == this) this.toInt().toString()
    else String.format("%.1f", this)