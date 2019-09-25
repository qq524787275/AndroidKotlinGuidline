package com.zhuzichu.guidline.inspection

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.zhuzichu.guidline.ext.logi
import org.jetbrains.kotlin.idea.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtVisitorVoid

class KotlinDataTypeConvertInspection : AbstractKotlinInspection() {

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor {
        return object : KtVisitorVoid() {
            override fun visitCallExpression(expression: KtCallExpression) {
                super.visitCallExpression(expression)
                val text = expression.text
                text.logi()
                if (text.contains("toDouble")
                    || text.contains("toInt")
                    || text.contains("toShort")
                    || text.contains("toByte")
                    || text.contains("toLong")
                    || text.contains("toFloat")
                    || text.contains("toBoolean")
                ) {
                    holder.registerProblem(expression.navigationElement, "小心有bug")
                }
            }
        }
    }
}