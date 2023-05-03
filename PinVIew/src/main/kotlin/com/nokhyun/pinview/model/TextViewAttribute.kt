package com.nokhyun.pinview.model

import androidx.annotation.ColorRes

/** TextView Setting Attribute */
data class TextViewAttribute(
    val textSize: Float,
    val text: String,
    @ColorRes val textColor: Int = android.R.color.black
)
