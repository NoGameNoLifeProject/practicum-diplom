package com.practicum.android.diploma.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import ru.practicum.android.diploma.R

class SearchBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val input: EditText
    private val actionIcon: ImageView

    private var onQueryTextChanged: ((String) -> Unit)? = null
    private var onSearchIconClick: ((String) -> Unit)? = null

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.view_searchbar, this, true)

        input = findViewById(R.id.search_input)
        actionIcon = findViewById(R.id.search_icon)

        updateIcon(input.text.toString())

        input.addTextChangedListener { editable ->
            val txt = editable?.toString().orEmpty()
            updateIcon(txt)
            onQueryTextChanged?.invoke(txt)
        }

        actionIcon.setOnClickListener {
            val txt = input.text.toString()
            if (txt.isNotEmpty()) {
                input.text.clear()
            } else {
                onSearchIconClick?.invoke(txt)
            }
        }
    }

    private fun updateIcon(text: String) {
        val res = if (text.isNotEmpty()) {
            R.drawable.ic_close_24px
        } else {
            R.drawable.ic_search_24px
        }
        actionIcon.setImageResource(res)
    }

    /** Колбэк на каждое изменение текста */
    fun setOnQueryTextChangedListener(listener: (String) -> Unit) {
        onQueryTextChanged = listener
    }

    /** Колбэк на клик по лупе, когда поле пустое */
    fun setOnSearchIconClickListener(listener: (String) -> Unit) {
        onSearchIconClick = listener
    }

    /** Установить текст программно */
    fun setQuery(query: String) {
        input.setText(query)
        input.setSelection(query.length)
    }

    /** Получить текущий текст */
    fun getQuery(): String = input.text.toString()
}
