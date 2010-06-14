package com.droidwave.stress;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Stress extends Activity {
	private static final String PLAYER_1 = "Player 1";
	private static final String PLAYER_2 = "Player 2";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// Watch for button clicks.
		int[] buttonIds = { R.id.Button01P1, R.id.Button02P1, R.id.Button03P1,
				R.id.Button04P1, R.id.Button01P2, R.id.Button02P2,
				R.id.Button03P2, R.id.Button04P2 };
		for (int buttonId : buttonIds) {
			Button button = (Button) findViewById(buttonId);
			button.setOnClickListener(clickListener);
		}
	}

	int switcherId = 0;
	private OnClickListener clickListener = new OnClickListener() {
		public void onClick(View v) {
			Button button = (Button) v;
			switcherId = (switcherId + 1) % 2;
			Button stackToUpdate = null;
			if (switcherId == 0) {
				stackToUpdate = (Button) findViewById(R.id.ButtonStack1);
			} else {
				stackToUpdate = (Button) findViewById(R.id.ButtonStack2);
			}
			stackToUpdate.setVisibility(View.VISIBLE);
			stackToUpdate.setText(button.getText());
		}

		private String getPlayerByButton(Button button) {
			String player = null;
			switch (button.getId()) {
			case R.id.Button01P1:
			case R.id.Button02P1:
			case R.id.Button03P1:
			case R.id.Button04P1:
				player = PLAYER_1;
				break;
			case R.id.Button01P2:
			case R.id.Button02P2:
			case R.id.Button03P2:
			case R.id.Button04P2:
				player = PLAYER_2;
				break;
			}
			return player;
		}
	};
}