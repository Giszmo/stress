package com.droidwave.stress;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Stress extends Activity {
	private static final String PLAYER_1 = "Player 1";
	private static final String PLAYER_2 = "Player 2";

	private Player[] player = new Player[2];
	private Deck[] centerDeck = new Deck[2];

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
		initGame();
	}

	private void initGame() {
		// base setup
		for (int playerNumber = 0; playerNumber < 2; playerNumber++) {
			player[playerNumber] = new Player();
			centerDeck[playerNumber] = new Deck(0, 0);
			int randomCard = player[playerNumber].getCardFromDeck();
			setCenterStack(playerNumber, randomCard);
		}
		return;
	}

	private void setCenterStack(int stack, int card) {
		Button stackToUpdate = null;
		if (stack == 0) {
			stackToUpdate = (Button) findViewById(R.id.ButtonStack1);
		} else {
			stackToUpdate = (Button) findViewById(R.id.ButtonStack2);
		}
		// stackToUpdate.setText(card); // FIXME ist integer, sollte
		// charSequence sein
		return;
	}

	private OnClickListener clickListener = new OnClickListener() {
		public void onClick(View v) {
			Button button = (Button) v;
			CharSequence playedCard = button.getText();
			int player = getPlayerByButton(button);

		}

		private int getPlayerByButton(Button button) {
			int player;
			switch (button.getId()) {
			case R.id.Button01P1:
			case R.id.Button02P1:
			case R.id.Button03P1:
			case R.id.Button04P1:
				player = 0;
				break;
			case R.id.Button01P2:
			case R.id.Button02P2:
			case R.id.Button03P2:
			case R.id.Button04P2:
				player = 1;
				break;
			}
			return player;
		}
	};
}