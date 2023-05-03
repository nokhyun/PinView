package com.nokhyun.pinview

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.nokhyun.pinview.interfaces.PinPad

/**
 *  TextPinPad
 *  */
internal class DefaultPinPad(
    private val context: Context,
    height: Int = LayoutParams.WRAP_CONTENT
) : LinearLayout(context), PinPad {
    private val shufflePinCode get() = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0").shuffled()

    private val _allPinPad: MutableList<TextView> = mutableListOf()

    private val _numberPad: MutableList<TextView> = mutableListOf()
    override val numberPad: List<TextView> = _numberPad

    private val _pinInput = mutableListOf<String>() // 사용자 핀 입력 배열
    val pinInput: List<String> = _pinInput

    private var _btnPinShuffle: TextView? = null
    val btnPinShuffle get() = _btnPinShuffle

    private var _btnDelete: TextView? = null
    val btnDelete get() = _btnDelete

    init {
        orientation = VERTICAL
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        initView(height)
    }

    private fun initView(height: Int) {
        this.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        repeat(4) {
            this.addView(getPinCodeViewGroup(height))
        }
    }

    private fun getPinCodeViewGroup(height: Int): LinearLayout {
        return LinearLayout(context).apply {
            orientation = HORIZONTAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }.also { viewGroup ->
            (1..3).map {
                ComponentFactory.createTextView(context) {
                    gravity = Gravity.CENTER
                    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, height).apply {
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

    override fun setNumberPad() {
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

    override fun shufflePinNumber() {
        _numberPad.zip(shufflePinCode) { tv: TextView, pin: String ->
            tv.text = pin
        }
    }

    override fun setDeleteBtn(btnText: String) {
        _btnDelete?.apply {
            text = btnText
        }
    }

    override fun setShuffleBtn(btnText: String) {
        _btnPinShuffle?.apply {
            text = btnText
        }
    }

    override fun setNumberListener(listener: OnClickListener) {
        numberPad.forEach {
            it.setOnClickListener(listener)
        }
    }

    override fun setDeleteButtonListener(listener: OnClickListener) {
        btnDelete?.setOnClickListener(listener)
    }

    override fun setShuffleButtonListener(listener: OnClickListener) {
        btnPinShuffle?.setOnClickListener(listener)
    }

    override fun removePinCode() {
        _pinInput.remove(_pinInput.last())
    }

    override fun addPinCode(pin: String, length: Int, block: (ImageView) -> Unit) {
        if (_pinInput.size < length) {
            val iv = ComponentFactory.createImageView(context) {
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_launcher_foreground))
                this
            }

            _pinInput.add(pin)

            block(iv)
        }
    }

    private fun log(msg: String) {
        Log.e(TAG, msg)
    }

    companion object {
        private const val TAG = "DefaultPinPad"
    }
}