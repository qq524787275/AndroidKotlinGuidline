package com.zhuzichu.guidline.inspection

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.kotlin.idea.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.idea.util.application.runWriteAction
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.psi.KtVisitorVoid
import org.jetbrains.kotlin.psi.getOrCreateBody


/**
 * desc: 生成ContractXXX 模板  <br/>
 * author: Coffee <br/>
 * time: 2019-09-29 10:07 <br/>
 * since v1.0 <br/>
 */
class KotlinContractAutoInspecton : AbstractKotlinInspection() {
    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor {
        return object : KtVisitorVoid() {
            override fun visitClass(klass: KtClass) {
                super.visitClass(klass)
                var hasInterface = false
                val className = klass.name
                if (className.isNullOrBlank())
                    return
                if (!klass.isInterface() || !className.startsWith("Contract"))
                    return
                var count = 0
                klass.body?.children?.forEach {
                    if (it is KtClass && it.isInterface() && (it.name == "SubView" || it.name == "SubPresenter")) {
                        count++
                    }
                }
                if (count == 2) {
                    hasInterface = true
                }
                if (!hasInterface) {
                    holder.registerProblem(
                        klass.nameIdentifier as PsiElement,
                        "请添加 SubView SubPresenter 接口",
                        GenerateMethod(className)
                    )
                }
            }
        }
    }

    inner class GenerateMethod(private val name: String) : LocalQuickFix {
        override fun getName(): String = "生成 SubView SubPresenter 接口"

        override fun getFamilyName(): String = name

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val factory = KtPsiFactory(project)
            val klass = descriptor.psiElement.parent as KtClass

            val subView = factory.createClass(
                """
                |interface SubView : IBaseView.ISubView {
                |
                |}
                """.trimMargin()
            )

            val subPresenter = factory.createClass(
                """
                |interface SubPresenter : IBasePresenter.ISubPresenter{
                |
                |}
                """.trimMargin()
            )

            runWriteAction {
                klass.getOrCreateBody().addAfter(subView, klass.getOrCreateBody().lBrace)
                klass.getOrCreateBody().addAfter(subPresenter, klass.getOrCreateBody().lBrace)
            }
        }
    }
}