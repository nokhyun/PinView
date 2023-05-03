package com.nokhyun.pinview

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.nokhyun.pinview.interfaces.PinImage

/**
 *  핀 이미지
 *  @param pinLength - 핀 길이
 *  */
internal class DefaultPinImages(
    private val context: Context,
    private val pinLength: Int,
    height: Int
) : LinearLayout(context), PinImage {

    init {
        orientation = HORIZONTAL
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, height)

        initView()
    }

    private fun initView() {
        repeat(pinLength) {
            addView(createImageView())
        }
    }

    override fun addImage(@DrawableRes img: Int, pinInputSize: Int) {
        log("[addImage]")
        val imageViews = children.filterIsInstance<ImageView>()
        val count = imageViews.count {
            it.drawable != null
        }.let { it + 1 }

        if (pinInputSize > pinLength && count != pinInputSize) return

        imageViews.forEach { iv ->
            if (iv.drawable == null) {
                iv.setImageDrawable(ContextCompat.getDrawable(context, img))
                return
            }
        }
    }

    override fun removeImage() {
        log("[removeImage]")
        if (childCount > -1) {
            for (i in childCount - 1 downTo 0) {
                children.filterIsInstance<ImageView>().toList().let {
                    if (it[i].drawable != null) {
                        it[i].setImageDrawable(null)
                        return
                    }
                }
            }
        }
    }

    private fun createImageView(@DrawableRes img: Int? = null): ImageView {
        return ComponentFactory.createImageView(context) {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                weight = 1f
                val image = if (img == null) null else ContextCompat.getDrawable(context, img)
                setImageDrawable(image)
            }

            imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.holo_purple))
            this
        }
    }

    private fun log(msg: String) {
        Log.e(TAG, msg)
    }

    companion object {
        private const val TAG = "DefaultPinImages"
    }
}