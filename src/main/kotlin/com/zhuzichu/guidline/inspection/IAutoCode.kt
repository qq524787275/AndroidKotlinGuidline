package com.zhuzichu.guidline.inspection

interface IAutoCode {
    companion object {
        const val TYPE_ACTIVITY = "Activity"
        const val TYPE_FRAGMENT = "Fragment"
        const val TYPE_FRAGMENT_DIALOG = "FragmentDialog"
    }

    fun getType(): String
}