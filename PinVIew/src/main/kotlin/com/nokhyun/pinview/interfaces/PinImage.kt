package com.nokhyun.pinview.interfaces

import android.content.Context
import androidx.annotation.DrawableRes

interface PinImage {
//    val context: Context
//    val pinLength: Int
//    val height: Int
    fun addImage(@DrawableRes img: Int, pinInputSize: Int)
    fun removeImage()
}