package com.droidwave.stress;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.droidwave.stress.view.MirrorButton;
import com.droidwave.stress.view.StackSizeIndicator;

public class Stress extends Activity {
	private static final int DECK_SUITE_COUNT = 4;
	private static final int DECK_SIZE = 13;
	private static final int CARDS_IN_DECK = DECK_SUITE_COUNT * DECK_SIZE;
	private Player[] player = new Player[2];
	private Deck[] centerDeck = new Deck[2];
	// buttonIds[player][card]
	private int[][] buttonIds = new int[][] {
			{ R.id.Button01P1, R.id.Button02P1, R.id.Button03P1,
					R.id.Button04P1 },
			{ R.id.Button01P2, R.id.Button02P2, R.id.Button03P2,
					R.id.Button04P2 } };

	private int[] currentValue = new int[2];
	private GameMode gameMode = GameMode.SINGLEPLAYER;
	private KILevel kiLevel = KILevel.EASY;
	private static final Map<KILevel, Long> kiDelay = new HashMap<KILevel, Long>();
	static {
		kiDelay.put(KILevel.EASY, 2000l);
		kiDelay.put(KILevel.MEDIUM, 1500l);
		kiDelay.put(KILevel.HARD, 500l);
	}
	private Handler mHandler = new Handler();
	private Runnable mUpdateTimerTask = new Runnable() {
		public void run() {
			long millis = SystemClock.uptimeMillis();
			doKiStep();
			// prevent it from blocking the main thread
			int factor = 1;
			long delay = kiDelay.get(kiLevel) * factor
					- (SystemClock.uptimeMillis() - millis);
			while (delay < 0) {
				factor++;
				delay = kiDelay.get(kiLevel) * factor
						- (SystemClock.uptimeMillis() - millis);
			}
			mHandler.postDelayed(mUpdateTimerTask, delay);
		}
	};

	@Override
	public void onResume() {
		doKiStep();
		mHandler.removeCallbacks(mUpdateTimerTask);
		mHandler.postDelayed(mUpdateTimerTask, kiDelay.get(kiLevel));
		super.onResume();
	}

	private void doKiStep() {
		int playerPick;
		if (gameMode == GameMode.DEMO) {
			playerPick = (int) (Math.random() * 2);
		} else {
			playerPick = 1;
		}
		int cardPick = (int) (Math.random() * 4);
		playCard(playerPick, cardPick);
	}

	private void startKI() {
		doKiStep();
		mHandler.removeCallbacks(mUpdateTimerTask);
		mHandler.postDelayed(mUpdateTimerTask, kiDelay.get(kiLevel));
	}

	@Override
	public void onPause() {
		mHandler.removeCallbacks(mUpdateTimerTask);
		super.onPause();
	}

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
		stopKI();
		// base setup
		for (int playerNumber = 0; playerNumber < 2; playerNumber++) {
			player[playerNumber] = new Player(new Deck(DECK_SIZE,
					DECK_SUITE_COUNT));
			centerDeck[playerNumber] = new Deck(0, 0);
			int randomCard = player[playerNumber].getCardFromDeck();
			setCenterStack(playerNumber, randomCard, playerNumber);
			updateInfo(playerNumber);
		}
		player[0].setColor(Color.argb(0xff, 0xff, 0x00, 0x00));
		player[1].setColor(Color.argb(0xff, 0x00, 0x00, 0xff));
		updateInfo(0);
		updateInfo(1);
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
		if (gameMode != GameMode.MULTIPLAYER) {
			startKI();
		}
	}

	private void stopKI() {
		mHandler.removeCallbacks(mUpdateTimerTask);
	}

	private void setCenterStack(int stack, int card, int playerNumber) {
		MirrorButton stackToUpdate = null;
		if (stack == 0) {
			stackToUpdate = (MirrorButton) findViewById(R.id.ButtonStack1);
			currentValue[0] = card;
		} else {
			stackToUpdate = (MirrorButton) findViewById(R.id.ButtonStack2);
			currentValue[1] = card;
		}
		centerDeck[stack].add(card);
		stackToUpdate.setNotifyColor(player[playerNumber].getColor());
		stackToUpdate.setText("" + card);
	}

	private void playerWon() {
		if (player[0].finished() && player[1].finished()) {
			toast(R.string.game_is_a_draw);
		} else if (player[0].finished()) {
			toast((getText(R.string.player_n_wins)).toString().replaceFirst(
					"%n1%", "1"));
		} else if (player[1].finished()) {
			toast((getText(R.string.player_n_wins)).toString().replaceFirst(
					"%n1%", "2"));
		}
		initGame();
	}

	private void updateInfo(int playerNumber) {
		StackSizeIndicator info = null;
		if (playerNumber == 0) {
			info = (StackSizeIndicator) findViewById(R.id.Info1);
		} else {
			info = (StackSizeIndicator) findViewById(R.id.Info2);
		}
		int remaining = player[playerNumber].remainingCards();
		info.setRemaining((float) remaining / (CARDS_IN_DECK - 4));
	}

	private void ensurePlayability() {
		boolean playable = false;
		for (int playerNumber = 0; playerNumber < 2; playerNumber++) {
			for (int cardNumber = 0; cardNumber < 4; cardNumber++) {
				for (int currentValueNumber = 0; currentValueNumber < 2; currentValueNumber++) {
					if (Math.abs(currentValue[currentValueNumber]
							- player[playerNumber].getOpenCard(cardNumber)) % 11 == 1) {
						playable = true;
					}
				}
			}
		}
		if (!playable) {
			drawNewCenterCards();
			if (player[0].finished() || player[1].finished()) {
				playerWon();
			} else {
				ensurePlayability();
			}
		}
	}

	private void drawNewCenterCards() {
		for (int playerNumber = 0; playerNumber < 2; playerNumber++) {
			int randomCard = player[playerNumber].getCardFromDeck();
			setCenterStack(playerNumber, randomCard, playerNumber);
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the currently selected menu XML resource.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.menu, menu);
		Log.d("menu", "menu initialized");
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_multiplayer:
			toast(R.string.new_multiplayer_started);
			gameMode = GameMode.MULTIPLAYER;
			initGame();
			break;
		case R.id.new_singleplayer:
			toast(R.string.new_singleplayer_started);
			gameMode = GameMode.SINGLEPLAYER;
			initGame();
			break;
		case R.id.new_demo:
			toast(R.string.new_demo_started);
			gameMode = GameMode.DEMO;
			initGame();
			break;
		case R.id.easyy:
			toast(getText(R.string.ai_set_to) + " " + getText(R.string.easy));
			break;
		case R.id.medium:
			toast(getText(R.string.ai_set_to) + " " + getText(R.string.medium));
			kiLevel = KILevel.MEDIUM;
			break;
		case R.id.hard:
			toast(getText(R.string.ai_set_to) + " " + getText(R.string.hard));
			kiLevel = KILevel.HARD;
			break;
		case R.id.submenu:
		case R.id.info:
			break;
		default:
			throw new Error("Unknown menu entry clicked: " + item.getItemId());
		}
		return true;
	}

	private void toast(int resId) {
		toast(getText(resId));
	}

	private void toast(CharSequence charSequence) {
		toast("" + charSequence);
	}

	private void toast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	private void playCard(int playerNumber, int cardNumber) {
		int cardValue = player[playerNumber].getOpenCard(cardNumber);
		boolean playedACard = false;
		if (Math.abs(currentValue[0] - cardValue) % 11 == 1) {
			setCenterStack(0, cardValue, playerNumber);
			playedACard = true;
		} else if (Math.abs(currentValue[1] - cardValue) % 11 == 1) {
			setCenterStack(1, cardValue, playerNumber);
			playedACard = true;
		}

		if (playedACard) {
			player[playerNumber].playOpenCard(cardNumber);
			int newCard = player[playerNumber].getOpenCard(cardNumber);
			Button button = (Button) findViewById(buttonIds[playerNumber][cardNumber]);
			button.setText("" + newCard);
			ensurePlayability();
			updateInfo(playerNumber);
			if (player[playerNumber].finished()) {
				playerWon();
			}
		}
	}

	private OnClickListener clickListener = new OnClickListener() {
		public void onClick(View v) {
			Button button = (Button) v;
			int playerNumber = getPlayerByButton(button);
			int cardNumber = getPlayedCardNumber(button, player[playerNumber]);
			playCard(playerNumber, cardNumber);
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