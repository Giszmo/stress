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
	// buttonIds[player][card]
	private int[][] buttonIds = new int[][] {
			{ R.id.Button01P1, R.id.Button02P1, R.id.Button03P1,
					R.id.Button04P1 },
			{ R.id.Button01P2, R.id.Button02P2, R.id.Button03P2,
					R.id.Button04P2 } };

	private int current_value_1, current_value_2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		addClickListener();
		initGame();
	}

	private void addClickListener() {
		for (int[] playerButtonIds : buttonIds) {
			for (int buttonId : playerButtonIds) {
				Button button = (Button) findViewById(buttonId);
				button.setOnClickListener(clickListener);
			}
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
		for (int p = 0; p < 2; p++) {
			int openCardId = 0;
			for (int buttonId : buttonIds[p]) {
				playerButton = (MirrorButton) findViewById(buttonId);
				playerButton.setText("" + player[p].getOpenCard(openCardId++));
				playerButton.setNotifyColor(player[p].getColor());
			}
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
			for (int p = 0; p < 2; p++) {
				for (int c = 0; c < 4; c++) {
					if (buttonIds[p][c] == button.getId()) {
						return c;
					}
				}
			}
			throw new Error("Unknown Button pressed");
		}

		private int getPlayerByButton(Button button) {
			for (int p = 0; p < 2; p++) {
				for (int c = 0; c < 4; c++) {
					if (buttonIds[p][c] == button.getId()) {
						return p;
					}
				}
			}
			throw new Error("Unknown Button pressed");
		}
	};
}