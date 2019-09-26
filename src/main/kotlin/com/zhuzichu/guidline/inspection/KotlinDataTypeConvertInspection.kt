package com.zhuzichu.guidline.inspection

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.zhuzichu.guidline.ext.logi
import org.jetbrains.kotlin.idea.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.psi.*

/**
 * desc: Kt文件中类型转换检测  <br/>
 * author: Coffee <br/>
 * time: 2019-09-25 10:07 <br/>
 * since v1.0 <br/>
 */
class KotlinDataTypeConvertInspection : AbstractKotlinInspection() {

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor {
        return object : KtVisitorVoid() {
            override fun visitCallExpression(expression: KtCallExpression) {
                super.visitCallExpression(expression)
                val text = expression.name
                if (text.isNullOrBlank())
                    return
                text.logi()
                if (text.contains("toDouble()")
                    || text.contains("toInt()")
                    || text.contains("toShort()")
                    || text.contains("toByte()")
                    || text.contains("toLong()")
                    || text.contains("toFloat()")
                    || text.contains("toBoolean()")
                ) {
                    holder.registerProblem(
                        expression,
                        "可能会报NumberFormatException异常，请用try catch 包裹起来"
                    )
                }
            }
        }
    }
}