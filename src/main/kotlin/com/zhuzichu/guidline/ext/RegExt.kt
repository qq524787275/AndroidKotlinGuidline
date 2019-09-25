package com.zhuzichu.guidline.ext

import java.util.regex.Pattern

const val REGEX_TUOFENG = "^[a-z]+([A-Z][a-z]+)\$"

fun isMatch(regex: String, string: String?): Boolean {
    return !string.isNullOrBlank() && Pattern.matches(regex, string)
}

fun String?.isTuoFeng(): Boolean {
    return isMatch(REGEX_TUOFENG, this)
}