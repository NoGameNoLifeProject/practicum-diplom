package ru.practicum.android.diploma.ui.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import ru.practicum.android.diploma.R

class HHToolbar : LinearLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {

        LayoutInflater.from(context)
            .inflate(R.layout.view_toolbar, this, true)

        navigationImg = findViewById(R.id.toolbarImgNavigation)
        titleTextView = findViewById(R.id.toolbarTitle)
        action2Img = findViewById(R.id.toolbarImgAction2)
        action1Img = findViewById(R.id.toolbarImgAction1)

        val params = context.obtainStyledAttributes(
            attrs,
            R.styleable.HHToolbar,
            defStyleAttr,
            defStyleRes
        )

        drawableNavigation = params.getDrawable(R.styleable.HHToolbar_drawableNavigation)
        drawableAction1 = params.getDrawable(R.styleable.HHToolbar_drawableAction1)
        drawableAction2 = params.getDrawable(R.styleable.HHToolbar_drawableAction2)
        this.titleText = params.getString(R.styleable.HHToolbar_titleText)?:""

        params.recycle()
    }

    private val navigationImg: ImageView
    private val titleTextView: TextView
    private val action2Img: ImageView
    private val action1Img: ImageView

    var drawableNavigation: Drawable? = null
        set(value) {
            field = value
            updateDrawable(navigationImg, value)
        }

    var drawableAction1: Drawable? = null
        set(value) {
            field = value
            updateDrawable(action1Img, value)
        }

    var drawableAction2: Drawable? = null
        set(value) {
            field = value
            updateDrawable(action2Img, value)
        }

    var titleText: String = ""
        set(value) {
            field = value
            titleTextView.text = value
            redrawItems()
        }

    private fun updateDrawable(view: ImageView, drawable: Drawable?) {
        if (drawable != null) {
            view.isVisible = true
            view.setImageDrawable(drawable)
        } else {
            view.isVisible = false
        }
        redrawItems()
    }

    private fun redrawItems() {
        invalidate()
        requestLayout()
    }

}
