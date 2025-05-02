package ru.practicum.android.diploma.ui.common

import android.content.Context
import android.graphics.Color
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import ru.practicum.android.diploma.R

class DoubleHintEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val tvOuterHint: TextView
    private val etInner: EditText
    private val ivClear: ImageView

    private var hintText: String = ""
    private var hintOuterColor: Int = Color.GRAY
    private var hintOuterFocusedColor: Int = Color.BLUE

    // Listener для изменений текста
    private var onTextChangedListener: ((String) -> Unit)? = null

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.view_double_hint_edit_text, this, true)

        tvOuterHint = findViewById(R.id.tvOuterHint)
        etInner = findViewById(R.id.etInner)
        ivClear = findViewById(R.id.ivClear)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DoubleHintEditText,
            0,
            0
        ).apply {
            val hintInner = getString(R.styleable.DoubleHintEditText_hintInner)
            val hintOuter = getString(R.styleable.DoubleHintEditText_hintOuter)
            hintOuterColor = getColor(
                R.styleable.DoubleHintEditText_hintOuterColor,
                hintOuterColor
            )
            hintOuterFocusedColor = getColor(
                R.styleable.DoubleHintEditText_hintOuterFocusedColor,
                hintOuterFocusedColor
            )
            recycle()

            etInner.hint = hintInner
            if (!hintOuter.isNullOrBlank()) {
                tvOuterHint.text = hintOuter
                tvOuterHint.visibility = View.VISIBLE
                tvOuterHint.setTextColor(hintOuterColor)
            }
        }

        // Фокус меняет цвет внешнего hint
        etInner.setOnFocusChangeListener { _, hasFocus ->
            tvOuterHint.setTextColor(
                if (hasFocus) hintOuterFocusedColor else hintOuterColor
            )
        }

        // Начальное состояние: крестик скрыт
        ivClear.visibility = View.GONE

        // Слушаем изменения текста, показываем/скрываем крестик и уведомляем listener
        etInner.addTextChangedListener { editable ->
            val text = editable?.toString().orEmpty()
            ivClear.visibility = if (text.isNotEmpty()) View.VISIBLE else View.GONE
            onTextChangedListener?.invoke(text)
        }

        // Клик по иконке — очистка текста
        ivClear.setOnClickListener {
            etInner.text?.clear()
            // После очистки уведомляем listener об ""
            onTextChangedListener?.invoke("")
        }
    }

    /** Текстовое содержимое */
    var text: String?
        get() = etInner.text?.toString()
        set(value) {
            etInner.setText(value)
            val text = value.orEmpty()
            ivClear.visibility = if (text.isNotEmpty()) View.VISIBLE else View.GONE
        }

    /** Позволяет подписаться на изменения текста */
    fun setOnTextChangedListener(listener: (String) -> Unit) {
        onTextChangedListener = listener
    }

    fun addTextChangedListener(watcher: TextWatcher) {
        etInner.addTextChangedListener(watcher)
    }
}
