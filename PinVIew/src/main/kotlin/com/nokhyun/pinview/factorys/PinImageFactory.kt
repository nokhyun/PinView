package com.nokhyun.pinview.factorys

import android.content.Context
import com.nokhyun.pinview.DefaultPinImages
import com.nokhyun.pinview.interfaces.PinImage

internal object PinImageFactory {
    enum class PinType {
        DEFAULT
    }

    fun <T : PinImage> create(pinType: PinType, context: Context, pinLength: Int, height: Int): T {
        return when (pinType) {
            PinType.DEFAULT -> DefaultPinImages(context, pinLength, height) as T
        }
    }
}