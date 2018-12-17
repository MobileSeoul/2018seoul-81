package com.jhmk.stampme.Animation

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation


class SimpleViewFadeAnimation {
    private val mDuration = 300

    companion object {
        private val TAG = SimpleViewFadeAnimation::class.java.simpleName
    }

    fun startAnimation(view_first: View, view_second: View) {
        val fadeOut: Animation
        val fadeIn: Animation
        fadeOut = AlphaAnimation(1.0f, 0.0f)
        fadeOut.setInterpolator(AccelerateInterpolator())
        fadeOut.setDuration(mDuration.toLong())

        fadeIn = AlphaAnimation(0.0f, 1.0f)
        fadeIn.setInterpolator(AccelerateInterpolator())
        fadeIn.setDuration(mDuration.toLong())

        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation) {
                view_first.visibility = View.INVISIBLE
                view_second.startAnimation(fadeIn)
            }

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationStart(animation: Animation) {}
        })

        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                view_second.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        view_first.startAnimation(fadeOut)
    }
}