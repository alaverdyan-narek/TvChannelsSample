package com.tvchannels.sample.coreui.extension

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updateLayoutParams
import com.tvchannels.sample.coreui.utils.KeyboardEventHandler


fun View.hideKeyboard() {
    val imm =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.getColor(@ColorRes resId: Int) = context.getColorCompat(resId)

fun View.getDrawable(@DrawableRes resId: Int) = context.getDrawableCompat(resId)

fun View.getDimen(@DimenRes resId: Int) = context.getDimen(resId)

fun View.margin(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        left?.run { leftMargin = this }
        top?.run { topMargin = this }
        right?.run { rightMargin = this }
        bottom?.run { bottomMargin = this }
    }
}

fun View.margin(all: Int) {
    margin(left = all, top = all, right = all, bottom = all)
}

fun View.updatePadding(
    left: Int = paddingLeft,
    top: Int = paddingTop,
    right: Int = paddingRight,
    bottom: Int = paddingBottom
) {
    setPadding(left, top, right, bottom)
}


fun View.addSystemPadding(
    targetView: View = this,
    isTop: Boolean = true,
    isBottom: Boolean = true,
    isIncludeIme: Boolean = true,
) {
    doOnApplyWindowInsets { view, windowInsets, initialPadding ->
        val mask =
            if (isIncludeIme) WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
            else
                WindowInsetsCompat.Type.systemBars()

        val isKeyboardVisible = ViewCompat.getRootWindowInsets(targetView)
            ?.isVisible(WindowInsetsCompat.Type.ime()) == true
        KeyboardEventHandler.keyboardCallback(isKeyboardVisible)
        val insets = windowInsets.getInsets(mask)
        targetView.updatePadding(
            top = initialPadding.top + if (isTop) insets.top else 0,
            bottom = initialPadding.bottom + if (isBottom) insets.bottom else 0,
        )

    }
}


fun View.applyFullScreen(window: Window) {
    doOnApplyWindowInsets { _, insets, initialPadding ->
        val controller = WindowInsetsControllerCompat(window, this)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH
    }
}

fun View.removeFullScreen(window: Window) {
    doOnApplyWindowInsets { _, insets, initialPadding ->
        val controller = WindowInsetsControllerCompat(window, this)
        controller.show(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH
    }
}

fun View.doOnApplyWindowInsets(block: (View, insets: WindowInsetsCompat, initialPadding: Rect) -> Unit) {
    val initialPadding = recordInitialPaddingForView(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        block(v, insets, initialPadding)
        WindowInsetsCompat.CONSUMED
    }
    requestApplyInsetsWhenAttached()
}


fun View.focusAndShowKeyboardNow() {
    val inputMethodManager =
        this.context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    requestFocus()
    inputMethodManager?.showSoftInput(this, 0)
}

fun View.showKeyboard() {
    context?.getSystemService(InputMethodManager::class.java)
        ?.showSoftInput(this, 0)
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

inline fun View.asGroupAllIndexed(onChild: (Int,View) -> Unit) {
    if (this is Group) {
        (parent as? ViewGroup)?.let { vg ->
            this.referencedIds.forEachIndexed {index,id->
                onChild(index,vg.findViewById(id))
            }
        }
    }
}