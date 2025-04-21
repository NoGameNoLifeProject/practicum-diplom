package ru.practicum.android.diploma.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import ru.practicum.android.diploma.R

class ErrorStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val errorImage: ImageView
    private val errorText: TextView

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.view_error_state, this, true)

        errorImage = findViewById(R.id.error_image)
        errorText = findViewById(R.id.error_text)
        context.withStyledAttributes(attrs, R.styleable.ErrorStateView) {
            val imgRes = getResourceId(
                R.styleable.ErrorStateView_errorImage,
                R.drawable.image_error_no_internet
            )
            errorImage.setImageResource(imgRes)
            getString(R.styleable.ErrorStateView_errorText)
                ?.let { errorText.text = it }
            val txtColor = getColor(
                R.styleable.ErrorStateView_errorTextColor,
                errorText.currentTextColor
            )
            errorText.setTextColor(txtColor)
        }
    }

    fun setErrorImage(resId: Int) {
        errorImage.setImageResource(resId)
    }

    fun setErrorText(text: String) {
        errorText.text = text
    }
}
