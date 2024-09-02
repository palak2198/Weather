package com.example.weatherapp.util

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import com.bumptech.glide.request.transition.Transition
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget

fun loadAndTintIcon(
    context: Context,
    imageView: AppCompatImageView,
    imageUrl: String,
    tintColorResId: Int
) {
    val tintColor = ContextCompat.getColor(context, tintColorResId)

    Glide.with(context)
        .asDrawable()
        .load(imageUrl)
        .into(object : CustomTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                resource.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
                imageView.setImageDrawable(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                // Handle if needed
            }
        })
}