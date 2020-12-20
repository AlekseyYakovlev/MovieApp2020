package ru.spb.yakovlev.movieapp2020.ui.util

import android.graphics.Rect
import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

fun View.addSystemPadding(
    targetView: View = this,
    isConsumed: Boolean = false
) {
    doOnApplyWindowInsets { _, insets, initialPadding ->
        val sysInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

        targetView.updatePadding(
            top = initialPadding.top + sysInsets.top,
            bottom = initialPadding.bottom + sysInsets.bottom
        )
        if (isConsumed) {
            WindowInsetsCompat.Builder(insets).setInsets(
                WindowInsetsCompat.Type.systemBars(),
                Insets.of(
                    sysInsets.left,
                    0,
                    sysInsets.right,
                    0
                )
            ).build()
        } else {
            insets
        }
    }
}

fun View.addSystemTopPadding(
    targetView: View = this,
    isConsumed: Boolean = false
) {
    doOnApplyWindowInsets { _, insets, initialPadding ->
        val sysInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

        targetView.updatePadding(
            top = initialPadding.top + sysInsets.top
        )
        if (isConsumed) {
            WindowInsetsCompat.Builder(insets).setInsets(
                WindowInsetsCompat.Type.systemBars(),
                Insets.of(
                    sysInsets.left,
                    0,
                    sysInsets.right,
                    sysInsets.bottom
                )
            ).build()
        } else {
            insets
        }
    }
}

fun View.addSystemBottomPadding(
    targetView: View = this,
    isConsumed: Boolean = false
) {
    doOnApplyWindowInsets { _, insets, initialPadding ->
        val sysInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

        targetView.updatePadding(
            bottom = initialPadding.bottom + sysInsets.bottom
        )
        if (isConsumed) {
            WindowInsetsCompat.Builder(insets).setInsets(
                WindowInsetsCompat.Type.systemBars(),
                Insets.of(
                    sysInsets.left,
                    sysInsets.top,
                    sysInsets.right,
                    0
                )
            ).build()
        } else {
            insets
        }
    }
}

fun View.doOnApplyWindowInsets(block: (View, insets: WindowInsetsCompat, initialPadding: Rect) -> WindowInsetsCompat) {
    val initialPadding = recordInitialPaddingForView(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        block(v, insets, initialPadding)
    }
    requestApplyInsetsWhenAttached()
}

private fun recordInitialPaddingForView(view: View) =
    Rect(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)

private fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        ViewCompat.requestApplyInsets(this)
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                ViewCompat.requestApplyInsets(v)
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}