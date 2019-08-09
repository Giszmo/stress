package com.droidwave.stress.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet

import androidx.appcompat.widget.AppCompatButton

class MirrorButton : AppCompatButton {
    private var easing = 0f
    var notifyColor: Int = 0

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    public override fun onDraw(canvas: Canvas) {
        easingEffect()
        // save the original canvas matrix
        canvas.save()
        // draw it rotated
        val centerX = this.width / 2.0f
        val centerY = this.height / 1.65f
        canvas.rotate(180f, centerX, centerY)
        super.onDraw(canvas)
        // restore the old matrix.
        canvas.restore()
        canvas.save()
        // draw translated
        canvas.translate(0f, -25f)
        super.onDraw(canvas)
        // restore the old matrix again
        canvas.restore()
    }

    private fun easingEffect() {
        easing *= .99f
        val red = easing(0x00, Color.red(notifyColor), easing)
        val green = easing(0x00, Color.green(notifyColor), easing)
        val blue = easing(0x00, Color.blue(notifyColor), easing)
        val alpha = easing(0xff, Color.alpha(notifyColor), easing)
        val color = Color.argb(alpha, red, green, blue)
        setTextColor(color)
    }

    private fun easing(target: Int, source: Int, easingFactor: Float): Int {
        return ((1 - easingFactor) * target + easingFactor * source).toInt()
    }

    override fun onTextChanged(text: CharSequence, start: Int, before: Int, after: Int) {
        super.onTextChanged(text, start, before, after)
        easing = 1f
    }
}
