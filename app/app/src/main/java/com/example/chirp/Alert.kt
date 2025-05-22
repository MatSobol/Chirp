package com.example.chirp

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

fun successAlert(context: Context, view: View, message: String) {
    val snackBar = Snackbar.make(
        view,
        message,
        Snackbar.LENGTH_SHORT
    )
    snackBar.setTextColor(ContextCompat.getColor(context, R.color.success_text))
    val params = snackBar.view.layoutParams as (FrameLayout.LayoutParams)
    params.setMargins(16, 48, 16, 0)
    params.width = FrameLayout.LayoutParams.WRAP_CONTENT
    params.height = FrameLayout.LayoutParams.WRAP_CONTENT
    params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
    snackBar.view.layoutParams = params
    snackBar.view.background = ContextCompat.getDrawable(context, R.drawable.success)
    val textView: TextView =
        snackBar.view.findViewById(com.google.android.material.R.id.snackbar_text)
    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.success_icon, 0, 0, 0)
    textView.compoundDrawablePadding = 12
    snackBar.show()
}

fun failAlert(context: Context, view: View, message: String) {
    val snackBar = Snackbar.make(
        view,
        message,
        Snackbar.LENGTH_SHORT
    )
    snackBar.setTextColor(ContextCompat.getColor(context, R.color.white))
    val params = snackBar.view.layoutParams as (FrameLayout.LayoutParams)
    params.setMargins(16, 48, 16, 0)
    params.width = FrameLayout.LayoutParams.WRAP_CONTENT
    params.height = FrameLayout.LayoutParams.WRAP_CONTENT
    params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
    snackBar.view.layoutParams = params
    snackBar.view.background = ContextCompat.getDrawable(context, R.drawable.fail)
    val textView: TextView =
        snackBar.view.findViewById(com.google.android.material.R.id.snackbar_text)
    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fail_icon, 0, 0, 0)
    textView.compoundDrawablePadding = 12
    snackBar.show()
}