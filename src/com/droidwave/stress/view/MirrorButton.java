/**
 * 
 */
package com.droidwave.stress.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.Button;

public class MirrorButton extends Button {
	public MirrorButton(Context context) {
		super(context);
	}

	public MirrorButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onDraw(Canvas canvas) {
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
}
