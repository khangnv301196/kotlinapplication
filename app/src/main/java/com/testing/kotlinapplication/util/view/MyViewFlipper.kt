package com.testing.kotlinapplication.util.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.ViewFlipper

class MyViewFlipper(context: Context?, attrs: AttributeSet?) : ViewFlipper(context, attrs) {

    var paint = Paint()
    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        var width = width
        var margin = 2.0f
        var radius = 15.0f
        var cx = (width / 2) - ((radius + margin) * 2 * childCount / 2)
        var cy = height.toFloat() - 15
        canvas?.save()

        for (i in 0..childCount - 1) {
            if (i == getDisplayedChild()) {
                paint.setColor(Color.WHITE)
                canvas?.drawCircle(cx, cy, radius, paint);

            } else {
                paint.setColor(Color.GRAY)
                canvas?.drawCircle(cx, cy, radius, paint)
            }
            cx += 2 * (radius + margin)
        }
        canvas?.restore()
    }

}
