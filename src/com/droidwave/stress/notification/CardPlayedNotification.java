package com.droidwave.stress.notification;

public class CardPlayedNotification {
	private int playerNumber;
	private int cardNumber;
	private int targetNumber;
	private boolean success;

	/**
	 * @param playerNumber
	 *            player 0 or 1
	 * @param cardNumber
	 *            card 0, 1, 2 or 3
	 * @param targetNumber
	 *            target 0 or 1
	 * @param success
	 *            false if the card could not be played
	 */
	public CardPlayedNotification(int playerNumber, int cardNumber,
			int targetNumber, boolean success) {
		this.playerNumber = playerNumber;
		this.cardNumber = cardNumber;
		this.targetNumber = targetNumber;
		this.success = success;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public int getCardNumber() {
		return cardNumber;
	}

	public int getTargetNumber() {
		return targetNumber;
	}

	public boolean isSuccess() {
		return success;
	}
}
