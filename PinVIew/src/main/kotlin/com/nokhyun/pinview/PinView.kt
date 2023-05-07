package com.nokhyun.pinview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.size
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

    // TODO clear 조건 카운트 받아야함.
    private var _pinLength: Int = DEFAULT_PIN_LENGTH
    private var pinImages: DefaultPinImages? = null
    private val pinPad: DefaultPinPad = PinPadFactory.create(PinPadFactory.PinPadType.DEFAULT, context, 48.dp)
    private var key: String = ""
    var onSuccess: (() -> Unit)? = null
    var onFailure: (() -> Unit)? = null

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
            pinImages = PinImageFactory.create(PinImageFactory.PinType.DEFAULT, context, _pinLength, 56.dp)
            addView(pinImages)
            addView(pinPad)

            pinPad.setNumberPad()

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
        pinPad.setNumberListener {
            if (pinPad.pinInput.size < _pinLength) {
                pinPad.addPinCode(
                    (it as TextView).text.toString(),
                    _pinLength
                ) {
                    pinImages?.addImage(R.drawable.ic_launcher_foreground, pinPad.pinInput.size)

                    comparePinCode()
                }
            }
        }

        pinPad.setShuffleButtonListener {
            pinPad.shufflePinNumber()
        }

        pinPad.setDeleteButtonListener {
            if (pinPad.pinInput.isNotEmpty()) {
                pinPad.removePinCode()
                pinImages?.removeImage()
            }
        }
    }

    private fun comparePinCode() {
        if(pinPad.pinInput.size != _pinLength) return

        if (pinPad.comparePinCode(key, _pinLength)) {
            (onSuccess ?: throw NullPointerException("PinView onSuccess is Null")).invoke()
        } else {
            (onFailure ?: throw NullPointerException("PinView onFailure is Null")).invoke()
        }
    }

    fun initPinCodeSetting(fileName: String) {
        context?.also { ctx ->
            PinCodeSetting.init(ctx, fileName)
        }
    }

    /**
     * @param value: type is String
     * */
    fun savePinCode(key: String, value: String): Boolean {
        this.key = key
        return PinCodeSetting.savePinCode(key, value)
    }

    /**
     * @param value: type is List
     * */
    fun savePinCode(key: String, value: List<String>): Boolean {
        this.key = key
        return PinCodeSetting.savePinCode(key, value.joinToString(""))
    }

    /**
     * @param value: type is Array
     * */
    fun savePinCode(key: String, value: Array<String>): Boolean {
        this.key = key
        return PinCodeSetting.savePinCode(key, value.joinToString(""))
    }

    private fun log(msg: String) {
        Log.e(TAG, msg)
    }

    companion object {
        private const val TAG = "PinView"
        private const val DEFAULT_PIN_LENGTH: Int = 4
    }
}