package com.nokhyun.pinview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
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

    private var failCount = 0
    private var initializationCount = 0
    private var pinLength: Int = DEFAULT_PIN_LENGTH

    @DrawableRes
    private var pinCodeImage: Int = R.drawable.ic_launcher_foreground
    private var pinImages: DefaultPinImages? = null
    private val pinPad: DefaultPinPad = PinPadFactory.create(PinPadFactory.PinPadType.DEFAULT, context, 48.dp)
    private var key: String = ""
    var onSuccess: (() -> Unit)? = null
    var onFailure: ((Int) -> Unit)? = null

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
        pinLength = typedArray.getInt(R.styleable.PinView_pinLength, DEFAULT_PIN_LENGTH)
        initializationCount = typedArray.getInt(R.styleable.PinView_initializationCount, DEFAULT_PIN_LENGTH)
        pinCodeImage = typedArray.getResourceId(R.styleable.PinView_pinImage, R.drawable.ic_launcher_foreground)

        typedArray.recycle()

        if (hadZeroLength(pinLength)) {
            pinImages = PinImageFactory.create(PinImageFactory.PinType.DEFAULT, context, pinLength, 56.dp)
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
            if (pinPad.pinInput.size < pinLength) {
                pinPad.addPinCode(
                    (it as TextView).text.toString(),
                    pinLength
                ) {
                    pinImages?.addImage(pinCodeImage, pinPad.pinInput.size)

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
        if (pinPad.pinInput.size != pinLength) return

        if (pinPad.comparePinCode(key, pinLength)) {
            (onSuccess ?: throw NullPointerException("PinView onSuccess is Null")).invoke()
        } else {
            failCount++
            (onFailure ?: throw NullPointerException("PinView onFailure is Null")).invoke(failCount)

            if (failCount >= initializationCount) {
                failCount = 0
                PinCodeSetting.clearPinCode()
            }
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
        failCount = 0
        return PinCodeSetting.savePinCode(key, value)
    }

    /**
     * @param value: type is List
     * */
    fun savePinCode(key: String, value: List<String>): Boolean {
        this.key = key
        failCount = 0
        return PinCodeSetting.savePinCode(key, value.joinToString(""))
    }

    /**
     * @param value: type is Array
     * */
    fun savePinCode(key: String, value: Array<String>): Boolean {
        this.key = key
        failCount = 0
        return PinCodeSetting.savePinCode(key, value.joinToString(""))
    }

    fun pinState(key: String): String {
        return if (PinCodeSetting.pinState(key)) "Your pin is saved." else "No pins are saved."
    }

    private fun log(msg: String) {
        Log.e(TAG, msg)
    }

    companion object {
        private const val TAG = "PinView"
        private const val DEFAULT_PIN_LENGTH: Int = 4
    }
}