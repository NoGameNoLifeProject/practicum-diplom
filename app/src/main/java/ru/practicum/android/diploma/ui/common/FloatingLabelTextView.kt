package ru.practicum.android.diploma.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import ru.practicum.android.diploma.R

class FloatingLabelTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val tvFloatingHint: TextView
    private val etContent: EditText
    private val ivIcon: ImageView

    private var hintText: String = ""
    @DrawableRes
    private var iconEmptyRes: Int = R.drawable.ic_arrow_forward_24px
    @DrawableRes
    private var iconFilledRes: Int = R.drawable.ic_clear_24px

    private var primaryClickListener: OnClickListener? = null

    init {
        inflate(context, R.layout.view_floating_label_textview, this)

        tvFloatingHint = findViewById(R.id.tvFloatingHint)
        etContent = findViewById(R.id.etContent)
        ivIcon = findViewById(R.id.ivIcon)

        context.obtainStyledAttributes(attrs, R.styleable.FloatingLabelTextView, defStyle, 0)
            .apply {
                hintText = getString(R.styleable.FloatingLabelTextView_flt_hint) ?: ""
                iconEmptyRes = getResourceId(
                    R.styleable.FloatingLabelTextView_flt_iconEmpty,
                    iconEmptyRes
                )
                iconFilledRes = getResourceId(
                    R.styleable.FloatingLabelTextView_flt_iconFilled,
                    iconFilledRes
                )
                recycle()
            }

        // начальное состояние
        etContent.hint = hintText
        ivIcon.setImageResource(iconEmptyRes)

        // кликаем по полю (текстовой части)
        etContent.setOnClickListener {
            primaryClickListener?.onClick(this)
        }

        // кликаем по иконке
        ivIcon.setOnClickListener {
            if (etContent.text.isNullOrEmpty()) {
                primaryClickListener?.onClick(this)
            } else {
                setText(null)
            }
        }

        // подстраиваем hint/иконку после первого layout
        post {
            updateFloatingLabel()
            updateIcon()
        }
    }

    fun setText(text: String?) {
        etContent.setText(text)
        updateFloatingLabel()
        updateIcon()
    }

    fun getText(): String = etContent.text.toString()

    private fun updateFloatingLabel() {
        val hasText = !etContent.text.isNullOrEmpty()
        if (hasText) {
            tvFloatingHint.apply {
                text = hintText
                visibility = View.VISIBLE
            }
            etContent.hint = ""
        } else {
            tvFloatingHint.visibility = View.GONE
            etContent.hint = hintText
        }
    }

    private fun updateIcon() {
        val hasText = !etContent.text.isNullOrEmpty()
        ivIcon.setImageResource(if (hasText) iconFilledRes else iconEmptyRes)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        primaryClickListener = l
    }
}
