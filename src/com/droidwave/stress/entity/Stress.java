package com.droidwave.stress.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import android.os.Handler;
import android.os.SystemClock;
import android.widget.Button;

import com.droidwave.stress.notification.CardPlayedNotification;
import com.droidwave.stress.notification.NewCardNotification;
import com.droidwave.stress.notification.PlayerWonNotification;

public class Stress extends Observable {
	private static final Map<KILevel, Long> kiDelay = new HashMap<KILevel, Long>();
	private static final String NEW_GAME = "NEW_GAME";

	private Player[] player = new Player[2];

	private int[] currentValue = new int[2];
	private GameMode gameMode = GameMode.SINGLEPLAYER;
	private KILevel kiLevel = KILevel.EASY;
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

	public void doResume() {
		doKiStep();
		mHandler.removeCallbacks(mUpdateTimerTask);
		mHandler.postDelayed(mUpdateTimerTask, kiDelay.get(kiLevel));
	}

	private void doKiStep() {
		int playerPick;
		if (gameMode == GameMode.DEMO) {
			playerPick = (int) (Math.random() * 2);
		} else {
			playerPick = 1;
		}
		int cardPick = (int) (Math.random() * 4);
		int targetPick = (int) (Math.random() * 2);
		playCard(playerPick, cardPick, targetPick);
	}

	private void startKI() {
		doKiStep();
		mHandler.removeCallbacks(mUpdateTimerTask);
		mHandler.postDelayed(mUpdateTimerTask, kiDelay.get(kiLevel));
	}

	public void doPause() {
		mHandler.removeCallbacks(mUpdateTimerTask);
	}

	public void initGame() {
		stopKI();
		// base setup
		for (int playerNumber = 0; playerNumber < 2; playerNumber++) {
			player[playerNumber] = new Player();
			int randomCard = player[playerNumber].getCardFromDeck();
			setCenterStack(playerNumber, randomCard, playerNumber);
		}
		notifyObservers(NEW_GAME);
		ensurePlayability();
		if (gameMode != GameMode.MULTIPLAYER) {
			startKI();
		}
	}

	private void stopKI() {
		mHandler.removeCallbacks(mUpdateTimerTask);
	}

	private void playerWon() {
		if (player[0].finished() && player[1].finished()) {
			notifyObservers(new PlayerWonNotification(-1));
		} else if (player[0].finished()) {
			notifyObservers(new PlayerWonNotification(0));
		} else if (player[1].finished()) {
			notifyObservers(new PlayerWonNotification(1));
		}
		initGame();
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
			for (int playerId = 0; playerId < 2; playerId++) {
				int randomCard = player[playerId].getCardFromDeck();
				notifyObservers(new NewCardNotification(playerId));
				setCenterStack(playerId, randomCard, playerId);
			}
			if (player[0].finished() || player[1].finished()) {
				playerWon();
			} else {
				ensurePlayability();
			}
		}
	}

	// public boolean onOptionsItemSelected(MenuItem item) {
	// switch (item.getItemId()) {
	// case R.id.new_multiplayer:
	// toast(R.string.new_multiplayer_started);
	// gameMode = GameMode.MULTIPLAYER;
	// initGame();
	// break;
	// case R.id.new_singleplayer:
	// toast(R.string.new_singleplayer_started);
	// gameMode = GameMode.SINGLEPLAYER;
	// initGame();
	// break;
	// case R.id.new_demo:
	// toast(R.string.new_demo_started);
	// gameMode = GameMode.DEMO;
	// initGame();
	// break;
	// case R.id.easyy:
	// toast(getText(R.string.ai_set_to) + " " + getText(R.string.easy));
	// kiLevel = KILevel.EASY;
	// break;
	// case R.id.medium:
	// toast(getText(R.string.ai_set_to) + " " + getText(R.string.medium));
	// kiLevel = KILevel.MEDIUM;
	// break;
	// case R.id.hard:
	// toast(getText(R.string.ai_set_to) + " " + getText(R.string.hard));
	// kiLevel = KILevel.HARD;
	// break;
	// case R.id.submenu:
	// case R.id.info:
	// break;
	// default:
	// throw new Error("Unknown menu entry clicked: " + item.getItemId());
	// }
	// return true;
	// }

	public void playCard(int playerNumber, int cardNumber, int targetNumber) {
		boolean playedACard = false;
		playedACard = tryPlayCard(playerNumber, cardNumber, targetNumber);
		if (!playedACard) {
			targetNumber = (targetNumber + 1) % 2;
		}
		playedACard = tryPlayCard(playerNumber, cardNumber, targetNumber);

		if (playedACard) {
			notifyObservers(new CardPlayedNotification(playerNumber,
					cardNumber, targetNumber, true));
			player[playerNumber].playOpenCard(cardNumber);
			int newCard = player[playerNumber].getOpenCard(cardNumber);
			Button button = (Button) findViewById(buttonIds[playerNumber][cardNumber]);
			button.setText("" + newCard);
			ensurePlayability();
			updateInfo(playerNumber);
			if (player[playerNumber].finished()) {
				playerWon();
			}
		} else {
			notifyObservers(new CardPlayedNotification(playerNumber,
					cardNumber, targetNumber, false));
		}
	}

	private boolean tryPlayCard(int playerNumber, int cardNumber,
			int targetNumber) {
		int cardValue = player[playerNumber].getOpenCard(cardNumber);
		if (Math.abs(currentValue[targetNumber] - cardValue) % 11 == 1) {
			setCenterStack(targetNumber, cardValue, playerNumber);
			return true;
		}
		return false;
	}
}