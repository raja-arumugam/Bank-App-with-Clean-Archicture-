package com.example.bankapp.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.bankapp.R

fun ImageView.loadImage(url: String) {
    if (url.isNotEmpty()) {
        Glide.with(this)
            .load(url)
            .placeholder(context.circularProgressDrawable())
            .into(this)
    }
}

fun Context.circularProgressDrawable(): Drawable {
    return CircularProgressDrawable(this).apply {
        strokeWidth = 7f
        centerRadius = 60f
        setColorSchemeColors(
            androidx.core.content.ContextCompat.getColor(
                this@circularProgressDrawable,
                R.color.green
            )
        )
        start()
    }
}