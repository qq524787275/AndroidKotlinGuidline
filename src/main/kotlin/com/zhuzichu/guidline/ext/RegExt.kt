package com.zhuzichu.guidline.ext

import java.util.regex.Pattern

/**
 * desc: 正则表达  <br/>
 * author: Coffee <br/>
 * time: 2019-09-25 9:45 <br/>
 * since v1.0 <br/>
 */

const val REGEX_Hump = "^[a-z]+([A-Z][a-z]+)\$"

fun isMatch(regex: String, string: String?): Boolean {
    return !string.isNullOrBlank() && Pattern.matches(regex, string)
}

/**
 * 判断字符是否是驼峰
 */
fun String?.isHump(): Boolean {
    return isMatch(REGEX_Hump, this)
}