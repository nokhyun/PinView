package com.nokhyun.pinview.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

/** Imageview Setting Attribute */
data class ImageViewAttribute(
    @DrawableRes val img: Int,
    @ColorRes val color: Int,
    @ColorRes val colorTint: Int = android.R.color.transparent
)