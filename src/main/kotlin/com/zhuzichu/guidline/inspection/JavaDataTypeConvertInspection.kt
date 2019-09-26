package com.zhuzichu.guidline.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiMethodCallExpression
import com.zhuzichu.guidline.ext.logi


/**
 * desc: Java文件中类型转换检测  <br/>
 * author: Coffee <br/>
 * time: 2019-09-25 10:06 <br/>
 * since v1.0 <br/>
 */
class JavaDataTypeConvertInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : JavaElementVisitor() {
            override fun visitMethodCallExpression(expression: PsiMethodCallExpression?) {
                super.visitMethodCallExpression(expression)
                expression ?: return
                val text = expression.text
                text.logi()
                if (text.contains("Integer.parseInt(")
                    || text.contains("Double.parseDouble(")
                    || text.contains("Short.parseShort(")
                    || text.contains("Byte.parseByte(")
                    || text.contains("Long.parseLong(")
                    || text.contains("Float.parseFloat(")
                    || text.contains("Boolean.parseBoolean(")
                    || text.contains("Integer.valueOf(")
                    || text.contains("Double.valueOf(")
                    || text.contains("Short.valueOf(")
                    || text.contains("Byte.valueOf(")
                    || text.contains("Long.valueOf(")
                    || text.contains("Float.valueOf(")
                    || text.contains("Boolean.valueOf(")
                ) {
                    holder.registerProblem(expression, "可能会报NumberFormatException异常，请用try catch 包裹起来")
                }
            }
        }
    }
}