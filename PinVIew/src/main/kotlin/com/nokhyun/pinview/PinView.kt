package com.nokhyun.pinview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.updateLayoutParams
import com.nokhyun.pinview.common.dp
import com.nokhyun.pinview.factorys.PinImageFactory
import com.nokhyun.pinview.factorys.PinPadFactory

/**
 * Created By Nokhyun90 on 2023-05-03
 * */
class PinView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var _pinLength: Int = DEFAULT_PIN_LENGTH
    private var defaultPinImages: DefaultPinImages? = null
    private val defaultPinPad: DefaultPinPad = PinPadFactory.create(PinPadFactory.PinPadType.DEFAULT, context, 48.dp)

    // TODO SharedPreference class 작업필요.

    init {
        attrs?.also {
            initView(context, it)
        }
    }

    override fun setWeightSum(weightSum: Float) {
        throw Exception("setWeightSum cannot be used.")
    }

    override fun setOrientation(orientation: Int) {
        throw Exception("setOrientation cannot be used. default orientation is vertical")
    }

    private fun initView(context: Context, attrs: AttributeSet) {
        log("[initView]")
        super.setWeightSum(1f)
        super.setOrientation(VERTICAL)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PinView)
        _pinLength = typedArray.getInt(R.styleable.PinView_pinLength, DEFAULT_PIN_LENGTH)

        typedArray.recycle()

        if (hadZeroLength(_pinLength)) {
            defaultPinImages = PinImageFactory.create(PinImageFactory.PinType.DEFAULT, context, _pinLength, 56.dp)
            addView(defaultPinImages)
            addView(defaultPinPad)

            defaultPinPad.setNumberPad()

            setListener()
            setChildLayoutParams()
        } else {
            throw IllegalArgumentException("Length must not be zero.")
        }
    }

    private fun hadZeroLength(length: Int): Boolean = length > 0

    private fun setChildLayoutParams() {
        children.forEach {
            it.updateLayoutParams<LayoutParams> {
                weight = 1f
            }
        }
    }

    private fun setListener() {
        defaultPinPad.setNumberListener {
            if (defaultPinPad.pinInput.size < _pinLength) {
                defaultPinPad.addPinCode((it as TextView).text.toString(), _pinLength) {
                    defaultPinImages?.addImage(R.drawable.ic_launcher_foreground, defaultPinPad.pinInput.size)
                }
            }
        }

        defaultPinPad.setShuffleButtonListener {
            defaultPinPad.shufflePinNumber()
        }

        defaultPinPad.setDeleteButtonListener {
            if (defaultPinPad.pinInput.isNotEmpty()) {
                defaultPinPad.removePinCode()
                defaultPinImages?.removeImage()
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