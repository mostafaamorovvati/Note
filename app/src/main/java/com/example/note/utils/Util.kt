package com.example.note.utils

import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation

fun log(message: String) {
    Log.d(LOG_TAG, message)
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