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

	private int current_value_1, current_value_2;

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

			// Button playerButton = null;
			// playerButton = (Button) findViewById(R.id.ButtonStack1);
		}
		return;
	}

	private void setCenterStack(int stack, int card) {
		Button stackToUpdate = null;
		if (stack == 0) {
			stackToUpdate = (Button) findViewById(R.id.ButtonStack1);
			current_value_1 = card;
		} else {
			stackToUpdate = (Button) findViewById(R.id.ButtonStack2);
			current_value_2 = card;
		}
		centerDeck[stack].add(card);

		stackToUpdate.setText("" + card);

		return;
	}

	private OnClickListener clickListener = new OnClickListener() {
		public void onClick(View v) {
			Button button = (Button) v;
			int playerNumber = getPlayerByButton(button);
			int cardValue = getPlayedCardValue(button, player[playerNumber]);
			if (Math.abs(current_value_1 - cardValue) % 10 == 1) {
				setCenterStack(0, cardValue);
			} else if (Math.abs(current_value_2 - cardValue) % 10 == 1) {
				setCenterStack(1, cardValue);
			}
			// (abs($i-$n) % 10 == 1);
			// put on stack & update current_value_1/2
			// remove from player

		}

		private int getPlayedCardValue(Button button, Player player) {
			switch (button.getId()) {
			case R.id.Button01P1:
				return player.getOpenCard(0);
			case R.id.Button02P1:
				return player.getOpenCard(1);
			case R.id.Button03P1:
				return player.getOpenCard(2);
			case R.id.Button04P1:
				return player.getOpenCard(3);
			case R.id.Button01P2:
				return player.getOpenCard(0);
			case R.id.Button02P2:
				return player.getOpenCard(1);
			case R.id.Button03P2:
				return player.getOpenCard(2);
			case R.id.Button04P2:
				return player.getOpenCard(3);
			}
			return 0; // unreachable!
		}

		private int getPlayerByButton(Button button) {
			int player = -1;
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