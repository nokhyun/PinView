package com.nokhyun.pinview

import android.content.Context
import android.util.TypedValue
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.nokhyun.pinview.model.ImageViewAttribute
import com.nokhyun.pinview.model.TextViewAttribute

internal object ComponentFactory {
    fun createTextView(context: Context, attr: TextViewAttribute): TextView {
        return TextView(context).apply {
            text = attr.text
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, attr.textSize)
            setTextColor(ContextCompat.getColor(context, attr.textColor))
        }
    }

    /** dsl */
    fun createTextView(context: Context, component: TextView.() -> TextView): TextView {
        return component(TextView(context))
    }

    fun createImageView(context: Context, attr: ImageViewAttribute): ImageView {
        return ImageView(context).apply {
            setImageDrawable(ContextCompat.getDrawable(context, attr.img))
        }
    }

    /** dsl */
    fun createImageView(context: Context, component: ImageView.() -> ImageView): ImageView {
        return component(ImageView(context))
    }

    /**
     * @param orientation - default VERTICAL
     * */
    fun createLinearLayout(context: Context, orientation: Int = LinearLayout.VERTICAL): LinearLayout {
        return LinearLayout(context).apply {
            this.orientation = orientation
        }
    }

    /** dsl */
    fun createLinearLayout(context: Context, component: LinearLayout.() -> LinearLayout): LinearLayout {
        return component(LinearLayout(context))
    }
}