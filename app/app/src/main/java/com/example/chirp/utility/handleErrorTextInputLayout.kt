package com.example.chirp.utility

import android.content.Context
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.example.chirp.R
import com.google.android.material.textfield.TextInputLayout

fun handleError(
    textInputLayout: TextInputLayout,
    editText: EditText,
    value: Int,
    context: Context
) {
    editText.setTextColor(
        ContextCompat.getColor(
            context,
            R.color.red
        )
    )
    textInputLayout.error = context.getString(value)
}

fun handleNoError(textInputLayout: TextInputLayout, editText: EditText, context: Context) {
    val colorStateList =
        ContextCompat.getColorStateList(context, R.color.text_color_input)
    editText.setTextColor(
        colorStateList
    )
    textInputLayout.error = null
}