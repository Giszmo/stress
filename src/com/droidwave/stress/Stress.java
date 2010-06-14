package com.droidwave.stress;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.droidwave.stress.view.MirrorButton;

public class Stress extends Activity {
	private Player[] player = new Player[2];
	private Deck[] centerDeck = new Deck[2];

	private int current_value_1, current_value_2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		addClickListener();
		initGame();
	}

	private void addClickListener() {
		int[] buttonIds = { R.id.Button01P1, R.id.Button02P1, R.id.Button03P1,
				R.id.Button04P1, R.id.Button01P2, R.id.Button02P2,
				R.id.Button03P2, R.id.Button04P2 };
		for (int buttonId : buttonIds) {
			Button button = (Button) findViewById(buttonId);
			button.setOnClickListener(clickListener);
		}
	}

	private void initGame() {
		// base setup
		for (int playerNumber = 0; playerNumber < 2; playerNumber++) {
			player[playerNumber] = new Player();
			centerDeck[playerNumber] = new Deck(0, 0);
			int randomCard = player[playerNumber].getCardFromDeck();
			setCenterStack(playerNumber, randomCard, playerNumber);
			updateInfo(playerNumber);
		}
		player[0].setColor(Color.argb(0xff, 0xff, 0x00, 0x00));
		player[1].setColor(Color.argb(0xff, 0x00, 0x00, 0xff));

		MirrorButton playerButton = null;
		int openCardId = 0;
		for (int buttonId : new int[] { R.id.Button01P1, R.id.Button02P1,
				R.id.Button03P1, R.id.Button04P1 }) {
			playerButton = (MirrorButton) findViewById(buttonId);
			playerButton.setText("" + player[0].getOpenCard(openCardId++));
			playerButton.setNotifyColor(player[0].getColor());
		}
		openCardId = 0;
		for (int buttonId : new int[] { R.id.Button01P2, R.id.Button02P2,
				R.id.Button03P2, R.id.Button04P2 }) {
			playerButton = (MirrorButton) findViewById(buttonId);
			playerButton.setText("" + player[1].getOpenCard(openCardId++));
			playerButton.setNotifyColor(player[1].getColor());
		}

		ensurePlayability();
	}

	private void setCenterStack(int stack, int card, int playerNumber) {
		MirrorButton stackToUpdate = null;
		if (stack == 0) {
			stackToUpdate = (MirrorButton) findViewById(R.id.ButtonStack1);
			current_value_1 = card;
		} else {
			stackToUpdate = (MirrorButton) findViewById(R.id.ButtonStack2);
			current_value_2 = card;
		}
		centerDeck[stack].add(card);
		stackToUpdate.setNotifyColor(player[playerNumber].getColor());
		stackToUpdate.setText("" + card);
	}

	private void playerWon() {
		initGame();
		return;
	}

	private void updateInfo(int playerNumber) {
		MirrorButton infoButton = null;
		if (playerNumber == 0) {
			infoButton = (MirrorButton) findViewById(R.id.ButtonInfo1);
		} else {
			infoButton = (MirrorButton) findViewById(R.id.ButtonInfo2);
		}
		int played = player[playerNumber].getCardsPlayed();
		int remaining = player[playerNumber].remainingCards();
		infoButton.setText(played + " / " + remaining);
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
				setCenterStack(playerNumber, randomCard, playerNumber);
			}
			if (player[0].finished() || player[1].finished()) {
				playerWon();
			} else {
				ensurePlayability();
			}
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
				setCenterStack(0, cardValue, playerNumber);
				playedACard = 1;
			} else if (Math.abs(current_value_2 - cardValue) % 11 == 1) {
				setCenterStack(1, cardValue, playerNumber);
				playedACard = 1;
			}

			if (playedACard == 1) {
				player[playerNumber].playOpenCard(cardNumber);
				int newCard = player[playerNumber].getOpenCard(cardNumber);
				button.setText("" + newCard);
				ensurePlayability();
				updateInfo(playerNumber);
				if (player[playerNumber].finished()) {
					playerWon();
				}
			}
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