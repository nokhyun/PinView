package com.nokhyun.pinview.interfaces

import androidx.annotation.DrawableRes

interface PinImage {
    fun addImage(@DrawableRes img: Int, pinInputSize: Int)
    fun removeImage()
}