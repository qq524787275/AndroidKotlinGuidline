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

/**
 * 判断字符是否是驼峰
 */
fun String?.isHump(): Boolean {
    if (this.isNullOrBlank())
        return false
    for (index in 0 until this.length) {
        if (this[index] in 'A'..'Z') {
            return true
        }
    }
    return false
}