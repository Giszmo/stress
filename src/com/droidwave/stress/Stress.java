package com.droidwave.stress;

import android.app.Activity;
import android.os.Bundle;

public class Stress extends Activity {

	private Deck[] centerStack = new Deck[2];

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}
}