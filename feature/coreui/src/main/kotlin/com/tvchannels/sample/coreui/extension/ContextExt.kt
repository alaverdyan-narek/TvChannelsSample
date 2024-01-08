package com.tvchannels.sample.coreui.extension
import android.content.ActivityNotFoundException


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

fun Context.getColorCompat(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)

fun Context.getDrawableCompat(@DrawableRes resId: Int) =
    ContextCompat.getDrawable(this, resId)

fun Context.getDimen(@DimenRes resId: Int) = resources.getDimensionPixelSize(resId)

fun Context.getFont(@FontRes resId: Int) = ResourcesCompat.getFont(this, resId)

fun Context.showToast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.openLink(link: String) = try {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
} catch (exception: ActivityNotFoundException) {
    exception.printStackTrace()
}
fun Context.takeVectorDrawableOrNull(drawableId: Int): VectorDrawable? =
    ContextCompat.getDrawable(this, drawableId)?.let { drawable ->
        if (drawable is VectorDrawable) return drawable else null
    }


fun Context.decodeVectorResourceToBitmap(id: Int): Bitmap? =
    this.takeVectorDrawableOrNull(id)?.let { vector ->
        val bitmap = Bitmap.createBitmap(
            vector.intrinsicWidth,
            vector.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vector.setBounds(0, 0, canvas.width, canvas.height)
        vector.draw(canvas)
        bitmap
    }

