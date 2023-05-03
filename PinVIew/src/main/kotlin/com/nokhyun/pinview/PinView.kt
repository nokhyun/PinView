package com.nokhyun.pinview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import com.nokhyun.pinview.common.dp

/**
 * Created By Nokhyun90 on 2023-05-03
 * */
class PinView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var _pinLength: Int = DEFAULT_PIN_LENGTH
    private var pinImages: PinImages? = null
    private val defaultPinPad = DefaultPinPad(context)

    init {
        orientation = VERTICAL
        setPadding(16.dp)

        attrs?.also {
            initView(context, it)
        }
    }

    private fun initView(context: Context, attrs: AttributeSet) {
        log("[initView]")
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PinView)

        _pinLength = typedArray.getInt(R.styleable.PinView_pinLength, DEFAULT_PIN_LENGTH)

        typedArray.recycle()

        pinImages = PinImages(context, _pinLength, 56.dp)
        addView(pinImages)
        addView(defaultPinPad)

        defaultPinPad.setNumberPad()

        defaultPinPad.setNumberListener {
            if (defaultPinPad.pinInput.size < _pinLength) {
                defaultPinPad.addPinCode((it as TextView).text.toString(), _pinLength) {
                    pinImages?.addImage(R.drawable.ic_launcher_foreground, defaultPinPad.pinInput.size)
                }
            }
        }

        defaultPinPad.setShuffleButtonListener {
            defaultPinPad.shufflePinNumber()
        }

        defaultPinPad.setDeleteButtonListener {
            if (defaultPinPad.pinInput.isNotEmpty()) {
                defaultPinPad.removePinCode()
                pinImages?.removeImage()
            }
        }
    }

    private fun log(msg: String) {
        Log.e(TAG, msg)
    }

    companion object {
        private const val TAG = "PinView"
        private const val DEFAULT_PIN_LENGTH: Int = 4
    }
}