package com.nokhyun.pinview

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
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

    // TODO 수정필요
    private val pinImages = PinImages(context)
    private val pinPad = PinPad(context)

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

        addView(pinImages.create(_pinLength))
        addView(pinPad)

        pinPad.setNumberPad()

        pinPad.setNumberListener {
            if (pinPad.pinInput.size < _pinLength) {
                pinPad.addPinCode((it as TextView).text.toString(), _pinLength){
                    // TODO PinImage 추가부터 시작
                }
            }
        }

        pinPad.setShuffleButtonListener {
            pinPad.shufflePinNumber()
        }

        pinPad.setDeleteButtonListener {
            if (pinPad.pinInput.isNotEmpty()) {
                // TODO 하나씩 제거
                pinPad.removePinCode()
            }
        }
    }

    private fun log(msg: String) {
        Log.e(TAG, msg)
    }

    /** 핀 이미지 생성 */
    private class PinImages(
        private val context: Context
    ) {
        // TODO Factory 고려. 해당 클래스에 뭔가 너무 기능이 몰려있는 느낌...??
        fun create(pinLength: Int, height: Int = LayoutParams.WRAP_CONTENT): LinearLayout {
            return createLinearLayout(pinLength, height)
        }

        private fun createLinearLayout(pinLength: Int, height: Int): LinearLayout {
            log("[createLinearLayout]")
            return ComponentFactory.createLinearLayout(context) {
                orientation = HORIZONTAL
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, height)

//                for (i in 0 until pinLength) {
//                    addView(createImageView(R.drawable.ic_launcher_foreground))
//                }

                this
            }
        }

        private fun createImageView(@DrawableRes img: Int): ImageView {
            return ComponentFactory.createImageView(context) {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                    weight = 1f
                    setImageDrawable(ContextCompat.getDrawable(context, img))
                }
                imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.holo_purple))

//                _pinImageViewList.add(this)
                this
            }
        }

        private fun log(msg: String) {
            Log.e("PinPad", msg)
        }
    }

    /** Pin Pad */
    private class PinPad(
        private val context: Context
    ) : LinearLayout(context) {
        private val shufflePinCode get() = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0").shuffled()

        private val _allPinPad: MutableList<TextView> = mutableListOf()

        private val _numberPad: MutableList<TextView> = mutableListOf()
        val numberPad: List<TextView> = _numberPad

        private val _pinInput = mutableSetOf<String>() // 사용자 핀 입력 배열
        val pinInput: Set<String> = _pinInput

        private var _pinImageViewList = mutableListOf<ImageView>()  // 핀 이미지
        val pinImageViewList = _pinImageViewList

        private var _btnPinShuffle: TextView? = null
        val btnPinShuffle get() = _btnPinShuffle

        private var _btnDelete: TextView? = null
        val btnDelete get() = _btnDelete

        init {
            orientation = VERTICAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            initView()
        }

        private fun initView() {
            this.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

            // 4줄 생성
            repeat(4) {
                this.addView(getPinCodeViewGroup())
            }

            // getTextView Button position
        }

        private fun getPinCodeViewGroup(): LinearLayout {
            return LinearLayout(context).apply {
                orientation = HORIZONTAL
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            }.also { viewGroup ->
                (1..3).map {
                    ComponentFactory.createTextView(context) {
                        gravity = Gravity.CENTER
                        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                            weight = 1f
                        }
                        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24f)
                        text = it.toString()

                        this
                    }
                }.map {
                    _allPinPad.add(it)
                    viewGroup.addView(it)
                }
            }
        }

        fun setNumberPad() {
            _allPinPad.forEachIndexed { index, textView ->
                when (index) {
                    9 -> _btnPinShuffle = textView
                    11 -> _btnDelete = textView
                    else -> _numberPad.add(textView)
                }
            }

            shufflePinNumber()
            setDeleteBtn()
            setShuffleBtn()
        }

        fun shufflePinNumber() {
            _numberPad.zip(shufflePinCode) { tv: TextView, pin: String ->
                tv.text = pin
            }
        }

        fun setDeleteBtn(btnText: String = "D") {
            _btnDelete?.apply {
                text = btnText
            }
        }

        fun setShuffleBtn(btnText: String = "R") {
            _btnPinShuffle?.apply {
                text = btnText
            }
        }

        fun setNumberListener(listener: OnClickListener) {
            numberPad.forEach {
                it.setOnClickListener(listener)
            }
        }

        fun setDeleteButtonListener(listener: View.OnClickListener) {
            btnDelete?.setOnClickListener(listener)
        }

        fun setShuffleButtonListener(listener: View.OnClickListener) {
            btnPinShuffle?.setOnClickListener(listener)
        }

        fun removePinCode() {
            // TODO pinpad remove
            _pinImageViewList[_pinImageViewList.size - 1].setImageDrawable(null)
//            _pinInput.remove(_pinInput.size - 1)
        }

        fun addPinCode(pin: String, length: Int, block: (ImageView) -> Unit) {
            log("addPinCode: $pin")
            if (_pinInput.size < length) {
                val iv = ComponentFactory.createImageView(context) {
                    setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_launcher_foreground))
                    this
                }

                _pinInput.add(pin)
                _pinImageViewList.add(iv)

                block(iv)
            }

            log("_pinInput: $pinInput")
        }

        private fun log(msg: String) {
            Log.e("PinPad", msg)
        }
    }

    companion object {
        private const val TAG = "PinView"
        private const val DEFAULT_PIN_LENGTH: Int = 4
    }
}