package com.zhuzichu.guidline.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttribute
import com.zhuzichu.guidline.ext.isTuoFeng
import com.zhuzichu.guidline.ext.logi

class ResourceIdInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : XmlElementVisitor() {
            override fun visitXmlAttribute(attribute: XmlAttribute?) {
                super.visitXmlAttribute(attribute)
                attribute ?: return
                if (attribute.localName == "id") {
                    attribute.value?.let {
                        if (it.split("/")[1].isTuoFeng()) {
                            holder.registerProblem(attribute.nameElement, "请使用下划线_替代驼峰")
                        }else{
                        }
                    }
                }
            }
        }
    }
}