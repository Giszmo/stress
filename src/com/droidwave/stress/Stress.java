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
		}
		Button playerButton = null;
		playerButton = (Button) findViewById(R.id.Button01P1);
		playerButton.setText("" + player[0].getOpenCard(0));

		playerButton = (Button) findViewById(R.id.Button02P1);
		playerButton.setText("" + player[0].getOpenCard(1));

		playerButton = (Button) findViewById(R.id.Button03P1);
		playerButton.setText("" + player[0].getOpenCard(2));

		playerButton = (Button) findViewById(R.id.Button04P1);
		playerButton.setText("" + player[0].getOpenCard(3));

		playerButton = (Button) findViewById(R.id.Button01P2);
		playerButton.setText("" + player[1].getOpenCard(0));

		playerButton = (Button) findViewById(R.id.Button02P2);
		playerButton.setText("" + player[1].getOpenCard(1));

		playerButton = (Button) findViewById(R.id.Button03P2);
		playerButton.setText("" + player[1].getOpenCard(2));

		playerButton = (Button) findViewById(R.id.Button04P2);
		playerButton.setText("" + player[1].getOpenCard(3));

		ensurePlayability();
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

	private void ensurePlayability() {
		int playable = 0;
		for (int playerNumber = 0; playerNumber < 2; playerNumber++) {
			for (int cardNumber = 0; cardNumber < 4; cardNumber++) {
				if (Math.abs(current_value_1
						- player[playerNumber].getOpenCard(cardNumber)) % 11 == 1
						|| Math.abs(current_value_2
								- player[playerNumber].getOpenCard(cardNumber)) % 11 == 1) {
					playable = 1;
				}
			}
		}
		if (playable == 0) {
			for (int playerNumber = 0; playerNumber < 2; playerNumber++) {
				int randomCard = player[playerNumber].getCardFromDeck();
				setCenterStack(playerNumber, randomCard);
			}
			ensurePlayability();
		}
	}

	private OnClickListener clickListener = new OnClickListener() {
		public void onClick(View v) {
			Button button = (Button) v;
			int playedACard = 0;
			int playerNumber = getPlayerByButton(button);
			int cardNumber = getPlayedCardNumber(button, player[playerNumber]);
			int cardValue = player[playerNumber].getOpenCard(cardNumber);
			if (Math.abs(current_value_1 - cardValue) % 11 == 1) {
				setCenterStack(0, cardValue);
				playedACard = 1;
			} else if (Math.abs(current_value_2 - cardValue) % 11 == 1) {
				setCenterStack(1, cardValue);
				playedACard = 1;
			}

			if (playedACard == 1) {
				player[playerNumber].playOpenCard(cardNumber);
				int newCard = player[playerNumber].getOpenCard(cardNumber);
				button.setText("" + newCard);
				ensurePlayability();
			}

			return;
		}

		private int getPlayedCardNumber(Button button, Player player) {
			switch (button.getId()) {
			case R.id.Button01P1:
				return 0;
			case R.id.Button02P1:
				return 1;
			case R.id.Button03P1:
				return 2;
			case R.id.Button04P1:
				return 3;
			case R.id.Button01P2:
				return 0;
			case R.id.Button02P2:
				return 1;
			case R.id.Button03P2:
				return 2;
			case R.id.Button04P2:
				return 3;
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