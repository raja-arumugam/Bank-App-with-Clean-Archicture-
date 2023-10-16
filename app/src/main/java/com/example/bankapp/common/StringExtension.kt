package com.example.bankapp.common

import java.util.regex.Matcher
import java.util.regex.Pattern

fun String.containsNumber():Boolean{
    val regex = Regex(".*\\d+.*")
    return regex.matches(this)
}

fun String.containsUpperCase():Boolean{
    val regex = Regex(".*[A-Z]+.*")
    return regex.matches(this)
}

fun String.containsSpecialChar():Boolean{
    val regex = Regex(".*[^A-Za-z\\d]+.*")
    return regex.matches(this)
}

fun String.isValidIFSC(ifsc: String?): Boolean {
    val regex = Regex("^[A-Z]{4}0[A-Z0-9]{6}$")
    return regex.matches(this)

    /*val pattern: Pattern
    val IFSC_PATTERN = "^[A-Z]{4}0[A-Z0-9]{6}$"
    pattern = Pattern.compile(IFSC_PATTERN)
    val matcher: Matcher = pattern.matcher(ifsc.toString())
    return matcher.matches()*/
}