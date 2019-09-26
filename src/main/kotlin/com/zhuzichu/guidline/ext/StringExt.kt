package com.zhuzichu.guidline.ext

import java.util.regex.Pattern


/**
 * 驼峰转下划线
 */
fun String.hump2Underline(): String {
    val matcher = Pattern.compile("[A-Z]").matcher(this)
    val sb = StringBuffer()
    while (matcher.find()) {
        matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase())
    }
    matcher.appendTail(sb)
    return sb.toString()
}