package com.zhuzichu.guidline.inspection

import com.intellij.openapi.project.Project
import com.zhuzichu.guidline.ext.hump2Underline
import org.jetbrains.kotlin.idea.core.getOrCreateCompanionObject
import org.jetbrains.kotlin.idea.util.application.runWriteAction
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.psi.getOrCreateBody

class AutoCodeDelegate(private val type: String) {

    fun checkClass(klass: KtClass): Boolean {
        val className = klass.name ?: ""
        var isPass = false
        if (className.startsWith(type)) {
            val name = klass.getSuperTypeList()?.entries?.firstOrNull()?.typeAsUserType?.referencedName ?: ""
            if (name != type.plus("AnalyticsBase")) {
                isPass = true
            }
        }
        return isPass
    }

    fun createCode(
        factory: KtPsiFactory,
        klass: KtClass,
        className: String,
        type: String
    ) {
        val word = className.substring(type.length)

        val route = factory.createAnnotationEntry(
            """
                |@Route(path = $className.TARGET)
                """.trimMargin()
        )

        val target = factory.createProperty(
            """
                |const val TARGET = ViewPathConst.${className.hump2Underline().toUpperCase().drop(1)}
                """.trimMargin()
        )

        val superType1 = factory.createSuperTypeEntry(
            """
                |Contract$word.SubView
                """.trimMargin()
        )

        val superType = factory.createSuperTypeCallEntry(
            """
                |${type}AnalyticsBase<DefaultViewParamsModel, Contract$word.SubPresenter, Contract$word.SubView>()
                """.trimMargin()
        )

        val getContentViewResId = factory.createFunction(
            """
                |override fun getContentViewResId() = R.layout.${className.hump2Underline().drop(1)}
                """.trimMargin()
        ).apply {
            add(factory.createNewLine())
        }

        val createPresenter = factory.createFunction(
            """
                |override fun createPresenter() = Presenter$word(this)
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
                |override fun getViewCode() = ViewCodeConst.${className.hump2Underline().toUpperCase().drop(1)}
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