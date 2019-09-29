package com.zhuzichu.guidline.inspection

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.zhuzichu.guidline.ext.hump2Underline
import org.jetbrains.kotlin.idea.core.getOrCreateCompanionObject
import org.jetbrains.kotlin.idea.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.idea.util.application.runWriteAction
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.psi.KtVisitorVoid
import org.jetbrains.kotlin.psi.getOrCreateBody


/**
 * desc: 生成ActivityXXX 模板  <br/>
 * author: Coffee <br/>
 * time: 2019-09-29 11:07 <br/>
 * since v1.0 <br/>
 */
class KoltinActivityAutoInspection : AbstractKotlinInspection() {

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor {
        return object : KtVisitorVoid() {
            override fun visitClass(klass: KtClass) {
                var hasSuper = false
                val className = klass.name
                if (className.isNullOrBlank())
                    return
                if (!className.startsWith("Activity"))
                    return

                val name = klass.getSuperTypeList()?.entries?.firstOrNull()?.typeAsUserType?.referencedName ?: ""

                if (name == "ActivityAnalyticsBase") {
                    hasSuper = true
                }

                if (!hasSuper) {
                    holder.registerProblem(
                        klass.nameIdentifier as PsiElement,
                        "请继承 ActivityAnalyticsBase",
                        GenerateMethod(className)
                    )
                }
            }
        }
    }

    inner class GenerateMethod(private val name: String) : LocalQuickFix {
        override fun getName(): String = "生成 Activity 模板"

        override fun getFamilyName(): String = name

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val factory = KtPsiFactory(project)
            val klass = descriptor.psiElement.parent as KtClass

            val word = name.substring("Activity".length)

            val route = factory.createAnnotationEntry(
                """
                |@Route(path = $name.TARGET)
                """.trimMargin()
            )

            val target = factory.createProperty(
                """
                |const val TARGET = ViewPathConst.${name.hump2Underline().toUpperCase().drop(1)}
                """.trimMargin()
            )

            val superType1 = factory.createSuperTypeEntry(
                """
                |Contract$word.SubView
                """.trimMargin()
            )

            val superType = factory.createSuperTypeCallEntry(
                """
                |ActivityAnalyticsBase<DefaultViewParamsModel, Contract$word.SubPresenter, Contract$word.SubView>()
                """.trimMargin()
            )

            val getContentViewResId = factory.createFunction(
                """
                |override fun getContentViewResId() = R.layout.${name.hump2Underline().drop(1)}
                """.trimMargin()
            ).apply {
                add(factory.createNewLine())
            }

            val createPresenter = factory.createFunction(
                """
                |override fun createPresenter() = Presenter${word}(this)
                """.trimMargin()
            ).apply {
                add(factory.createNewLine())
            }

            val getSub = factory.createFunction(
                """
                |override fun getSub() = this
                """.trimMargin()
            ).apply {
                add(factory.createNewLine())
            }

            val getViewCode = factory.createFunction(
                """
                |@ViewCode
                |override fun getViewCode() = ViewCodeConst.${name.hump2Underline().toUpperCase().drop(1)}
                """.trimMargin()
            ).apply {
                add(factory.createNewLine())
            }

            val getTagGAScreenName = factory.createFunction(
                """
                |override fun getTagGAScreenName() = "$word"
                """.trimMargin()
            ).apply {
                add(factory.createNewLine())
            }

            runWriteAction {
                klass.addAnnotationEntry(route)

                klass.addSuperTypeListEntry(superType)

                klass.addSuperTypeListEntry(superType1)

                klass.getOrCreateCompanionObject().getOrCreateBody()
                    .addAfter(target, klass.getOrCreateCompanionObject().getOrCreateBody().lBrace)

                klass.getOrCreateBody()
                    .addAfter(getContentViewResId, klass.getOrCreateBody().children.last())

                klass.getOrCreateBody()
                    .addAfter(factory.createNewLine(), klass.getOrCreateBody().children.last())

                klass.getOrCreateBody()
                    .addAfter(createPresenter, klass.getOrCreateBody().children.last())

                klass.getOrCreateBody()
                    .addAfter(getSub, klass.getOrCreateBody().children.last())

                klass.getOrCreateBody()
                    .addAfter(getViewCode, klass.getOrCreateBody().children.last())

                klass.getOrCreateBody()
                    .addAfter(getTagGAScreenName, klass.getOrCreateBody().children.last())

            }
        }
    }
}