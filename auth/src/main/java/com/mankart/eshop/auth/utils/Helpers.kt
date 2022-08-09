package com.mankart.eshop.auth.utils

import android.animation.ObjectAnimator
import android.view.View

object Helpers {
    fun View.isVisible(isVisible : Boolean, duration: Long = 400) {
        ObjectAnimator
            .ofFloat(this, View.ALPHA,if (isVisible) 1f else 0f)
            .setDuration(duration)
            .start()
    }
}