package com.zhuzichu.guidline.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiMethodCallExpression
import com.zhuzichu.guidline.ext.logi

class DataTypeConvertInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : JavaElementVisitor() {
            override fun visitMethodCallExpression(expression: PsiMethodCallExpression?) {
                super.visitMethodCallExpression(expression)
                expression ?: return
                val text = expression.text
                text.logi()
                if (text.contains("Integer.parseInt")
                    || text.contains("Double.parseDouble")
                    || text.contains("Short.parseShort")
                    || text.contains("Byte.parseByte")
                    || text.contains("Long.parseLong")
                    || text.contains("Float.parseFloat")
                    || text.contains("Boolean.parseBoolean")
                ) {
                    holder.registerProblem(expression.navigationElement, "小心有bug")
                }
            }
        }
    }
}