package com.nokhyun.pinview.interfaces

import android.view.View
import android.widget.ImageView
import android.widget.TextView

interface PinPad {
    val numberPad: List<TextView>
    fun setNumberPad()
    fun shufflePinNumber()
    fun setDeleteBtn(btnText: String = "D")
    fun setShuffleBtn(btnText: String = "R")
    fun setNumberListener(listener: View.OnClickListener)
    fun setDeleteButtonListener(listener: View.OnClickListener)
    fun setShuffleButtonListener(listener: View.OnClickListener)
    fun removePinCode()
    fun addPinCode(pin: String, length: Int, block: (ImageView) -> Unit)
}