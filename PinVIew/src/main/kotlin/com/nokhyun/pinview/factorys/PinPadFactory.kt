package com.nokhyun.pinview.factorys

import android.content.Context
import com.nokhyun.pinview.DefaultPinPad
import com.nokhyun.pinview.interfaces.PinPad

internal object PinPadFactory {
    enum class PinPadType {
        DEFAULT
    }

    fun <T : PinPad> create(pinPadType: PinPadType, context: Context, height: Int): T {
        return when (pinPadType) {
            PinPadType.DEFAULT -> DefaultPinPad(context, height) as T
        }
    }
}