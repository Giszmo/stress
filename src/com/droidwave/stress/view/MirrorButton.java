/**
 * 
 */
package com.droidwave.stress.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.Button;

public class MirrorButton extends Button {
	private float easing = 0;

	public MirrorButton(Context context) {
		super(context);
	}

	public MirrorButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onDraw(Canvas canvas) {
		easingEffect();
		// save the original canvas matrix
		canvas.save();
		// draw it rotated
		float centerX = this.getWidth() / 2.0f;
		float centerY = this.getHeight() / 1.8f;
		canvas.rotate(180, centerX, centerY);
		super.onDraw(canvas);
		// restore the old matrix.
		canvas.restore();
		// draw translated
		canvas.translate(0, -5f);
		super.onDraw(canvas);
		// restore the old matrix again
		canvas.restore();
	}

	private void easingEffect() {
		easing *= .98;
		int red = 0x00;
		int green = (int) (0xff * easing);
		int blue = 0x00;
		int alpha = 0xff;
		int color = 0x00;
		color = (alpha << 24) + (red << 16) + (green << 8) + blue;
		setTextColor(color);
	}

	@Override
	protected void onTextChanged(CharSequence text, int start, int before,
			int after) {
		super.onTextChanged(text, start, before, after);
		easing = 1;
	}
}
