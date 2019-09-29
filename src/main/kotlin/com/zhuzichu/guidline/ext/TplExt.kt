package com.zhuzichu.guidline.ext

import freemarker.template.Configuration
import freemarker.template.TemplateExceptionHandler
import java.io.File

/**
 * desc: todo 一键生成模板文件 ActivityXXX(FragmentXXX).kt | ContractXXX.kt | PresenterXXX.kt | R.layout.activity_xxx(R.layout.fragment_xxx)  <br/>
 * author: Coffee <br/>
 * time: 2019-09-25 11:25 <br/>
 * since v1.0 <br/>
 */

private val PROJECT_PATH = System.getProperty("user.dir")
private val TEMPLATE_FILE_PATH = PROJECT_PATH.plus("/src/main/resources/template")

fun Any.getConfiguration(): Configuration {
    val cfg = Configuration(Configuration.VERSION_2_3_23)
    cfg.setDirectoryForTemplateLoading(File(TEMPLATE_FILE_PATH))
    cfg.defaultEncoding = "UTF-8"
    cfg.templateExceptionHandler = TemplateExceptionHandler.IGNORE_HANDLER
    return cfg
}