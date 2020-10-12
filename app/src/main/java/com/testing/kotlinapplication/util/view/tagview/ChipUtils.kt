package com.testing.kotlinapplication.util.view.tagview

import android.content.Context
import android.graphics.*
import android.widget.ImageView
import com.testing.kotlinapplication.R


/**
 * Created by robert on 2/27/2017.
 */
object ChipUtils {
    const val IMAGE_ID = 0x00910518
    const val TEXT_ID = 0x00059118
    private val colors = intArrayOf(
        0xd32f2f,
        0xC2185B,
        0x7B1FA2,
        0x512DA8,
        0x303F9F,
        0x1976D2,
        0x0288D1,
        0x0097A7,
        0x00796B,
        0x388E3C,
        0x689F38,
        0xAFB42B,
        0xFBC02D,
        0xFFA000,
        0xF57C00,
        0xE64A19,
        0x5D4037,
        0x616161,
        0x455A64
    )

    fun getScaledBitmap(context: Context, bitmap: Bitmap?): Bitmap {
        val size = context.resources.getDimensionPixelSize(R.dimen.chip_height)
        return Bitmap.createScaledBitmap(bitmap!!, size, size, false)
    }

    fun getSquareBitmap(bitmap: Bitmap): Bitmap {
        val output: Bitmap
        output = if (bitmap.width >= bitmap.height) {
            Bitmap.createBitmap(
                bitmap,
                bitmap.width / 2 - bitmap.height / 2,
                0,
                bitmap.height,
                bitmap.height
            )
        } else {
            Bitmap.createBitmap(
                bitmap,
                0,
                bitmap.height / 2 - bitmap.width / 2,
                bitmap.width,
                bitmap.width
            )
        }
        return output
    }

    fun getCircleBitmap(
        context: Context,
        bitmap: Bitmap?,
        radius: Float
    ): Bitmap {
        val size = context.resources.getDimensionPixelSize(R.dimen.chip_height)
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val color = Color.RED
        val paint = Paint()
        val rect = Rect(0, 0, size, size)
        val rectF = RectF(rect)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, radius, radius, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap!!, rect, rect, paint)
        return output
    }

    fun getCircleBitmapWithText(
        context: Context,
        text: String,
        textColor: Int,
        bgColor: Int,
        radius: Float
    ): Bitmap {
        val size = context.resources.getDimensionPixelSize(R.dimen.chip_height)
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val textPaint = Paint()
        val rect = Rect(0, 0, size, size)
        val rectF = RectF(rect)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = bgColor
        canvas.drawRoundRect(rectF, radius, radius, paint)
        textPaint.color = textColor
        textPaint.strokeWidth = 30f
        textPaint.textSize = 45f
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
        textPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
        val xPos: Int
        val yPos: Int
        if (text.length == 1) {
            xPos =
                (canvas.width / 2 + (textPaint.descent() + textPaint.ascent()) / 2).toInt()
            yPos =
                (canvas.height / 2 - (textPaint.descent() + textPaint.ascent()) / 2).toInt()
        } else {
            xPos =
                (canvas.width / 3 + (textPaint.descent() + textPaint.ascent()) / 2).toInt()
            yPos =
                (canvas.height / 2 - (textPaint.descent() + textPaint.ascent()) / 2).toInt()
        }
        canvas.drawBitmap(output, rect, rect, paint)
        canvas.drawText(text, xPos.toFloat(), yPos.toFloat(), textPaint)
        return output
    }

    fun generateText(iconText: String): String {
        check(!iconText.isEmpty()) { "Icon text must have at least one symbol" }
        if (iconText.length == 1 || iconText.length == 2) {
            return iconText
        }
        val parts = iconText.split(" ").toTypedArray()
        if (parts.size == 1) {
            var text = parts[0]
            text = text.substring(0, 2)
            var f = text.substring(0, 1)
            var s = text.substring(1, 2)
            f = f.toUpperCase()
            s = s.toLowerCase()
            text = f + s
            return text
        }
        var first = parts[0]
        var second = parts[1]
        first = first.substring(0, 1)
        first = first.toUpperCase()
        second = second.substring(0, 1)
        second = second.toUpperCase()
        return first + second
    }

    fun setIconColor(icon: ImageView, color: Int) {
        val iconDrawable = icon.drawable
        icon.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        icon.setImageDrawable(iconDrawable)
    }
}
