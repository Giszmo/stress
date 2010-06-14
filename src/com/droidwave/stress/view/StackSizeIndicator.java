/**
 * 
 */
package com.droidwave.stress.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * @author leo
 * 
 */
public class StackSizeIndicator extends ProgressBar {

	private float remaining;

	public StackSizeIndicator(Context context) {
		super(context);
	}

	public StackSizeIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onDraw(Canvas canvas) {
		float centerX = getWidth() / 2.0f;
		float centerY = getHeight() / 2.0f;
		canvas.save();
		canvas.scale(remaining, 1, centerX, centerY);
		super.onDraw(canvas);
		canvas.restore();
	}

	/**
	 * @param f
	 */
	public void setRemaining(float remaining) {
		this.remaining = remaining;
	}
}
