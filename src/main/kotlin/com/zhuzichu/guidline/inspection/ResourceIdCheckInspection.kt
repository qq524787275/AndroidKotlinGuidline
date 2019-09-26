package com.zhuzichu.guidline.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttribute
import com.zhuzichu.guidline.ext.hump2Underline
import com.zhuzichu.guidline.ext.isHump
import org.jetbrains.kotlin.idea.util.application.runWriteAction

/**
 * desc: xml中id驼峰检测  <br/>
 * author: Coffee <br/>
 * time: 2019-09-25 11:25 <br/>
 * since v1.0 <br/>
 */
class ResourceIdCheckInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : XmlElementVisitor() {
            override fun visitXmlAttribute(attribute: XmlAttribute?) {
                super.visitXmlAttribute(attribute)
                attribute ?: return
                if (attribute.localName == "id") {
                    attribute.value?.let {
                        val idValue = it.split("/")[1]
                        if (idValue.isHump()) {
                            holder.registerProblem(
                                attribute,
                                "请使用下划线_替代驼峰",
                                GenerateMethod(attribute, idValue)
                            )
                        } else {
                        }
                    }
                }
            }
        }
    }

    class GenerateMethod(private val attribute: XmlAttribute, private val idValue: String) : LocalQuickFix {
        override fun getName(): String = "驼峰转下划线"

        override fun getFamilyName(): String = attribute.localName

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            attribute.setValue("@+id/".plus(idValue.hump2Underline()))
            runWriteAction {
                attribute
            }
        }
    }
}