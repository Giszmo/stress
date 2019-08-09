package com.droidwave.stress.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ProgressBar

class StackSizeIndicator : ProgressBar {
    private var remaining: Float = 0.toFloat()

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    public override fun onDraw(canvas: Canvas) {
        val centerX = width / 2.0f
        val centerY = height / 2.0f
        canvas.save()
        canvas.scale(remaining, 1f, centerX, centerY)
        super.onDraw(canvas)
        canvas.restore()
    }

    fun setRemaining(remaining: Float) {
        this.remaining = remaining
    }
}
