package com.zhuzichu.guidline.inspection

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.kotlin.idea.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.psi.KtVisitorVoid

class KotlinFragmentDialogAutoInspection : AbstractKotlinInspection(), IAutoCode {
    override fun getType(): String = IAutoCode.TYPE_FRAGMENT_DIALOG

    private val autoCodeDelegate by lazy { AutoCodeDelegate(getType()) }

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor {
        return object : KtVisitorVoid() {
            override fun visitClass(klass: KtClass) {
                super.visitClass(klass)
                if (autoCodeDelegate.checkClass(klass)) {
                    holder.registerProblem(
                        klass.nameIdentifier as PsiElement,
                        "请继承 FragmentDialogAnalyticsBase",
                        GenerateMethod(klass)
                    )
                }
            }
        }
    }


    inner class GenerateMethod(private val klass: KtClass) : LocalQuickFix {

        private val className = klass.name!!

        override fun getName(): String = "生成 FragmentDialog 模板"

        override fun getFamilyName(): String = className

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val factory = KtPsiFactory(project)
            autoCodeDelegate.createCode(factory, klass, className, getType())
        }
    }
}