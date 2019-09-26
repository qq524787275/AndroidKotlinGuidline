package com.zhuzichu.guidline.ext

import com.intellij.openapi.diagnostic.Logger

/**
 * desc: Log日志  <br/>
 * author: Coffee <br/>
 * time: 2019-09-25 9:34 <br/>
 * since v1.0 <br/>
 */

val LOG = Logger.getInstance("zzc")

fun Any.logi() {
    LOG.info(this.toString())
}