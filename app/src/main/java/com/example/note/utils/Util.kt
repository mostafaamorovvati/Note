package com.example.note.utils

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun log(message: String) {
    Log.d(LOG_TAG, message)
}

@BindingAdapter("loadImage")
fun loadImage(view: ImageView, imageUri: String?) {
    Glide
        .with(view.context)
        .load(Uri.parse(imageUri))
        .into(view)
}


fun repeatAnimation(): Animation {
    return ScaleAnimation(
        1f, 1.1f, 1f, 1.1f,
        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
    ).apply {
        duration = 150.toLong()
        repeatMode = Animation.REVERSE
        repeatCount = 1
    }
}

fun showAnimation(): Animation {
    return ScaleAnimation(
        0f, 1f, 0f, 1f,
        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
    ).apply {
        duration = 150.toLong()
    }
}

fun hideAnimation(view: View): Animation {
    return ScaleAnimation(
        1f, 0f, 1f, 0f,
        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
    ).apply {
        duration = 150.toLong()
        setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                view.gone()
            }

            override fun onAnimationStart(animation: Animation?) {}
        })
    }
}
