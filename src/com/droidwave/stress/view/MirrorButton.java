package com.droidwave.stress.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;

public class MirrorButton extends Button {
	private float easing = 0;
	private int notifyColor;

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
		easing *= .99;
		int red = easing(0x00, Color.red(notifyColor), easing);
		int green = easing(0x00, Color.green(notifyColor), easing);
		int blue = easing(0x00, Color.blue(notifyColor), easing);
		int alpha = easing(0xff, Color.alpha(notifyColor), easing);
		int color = Color.argb(alpha, red, green, blue);
		setTextColor(color);
	}

	private int easing(int target, int source, float easingFactor) {
		return (int) ((1 - easingFactor) * target + easingFactor * source);
	}

	@Override
	protected void onTextChanged(CharSequence text, int start, int before,
			int after) {
		super.onTextChanged(text, start, before, after);
		easing = 1;
	}

	public void setNotifyColor(int notifyColor) {
		this.notifyColor = notifyColor;
	}

	public int getNotifyColor() {
		return notifyColor;
	}
}
