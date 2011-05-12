package com.droidwave.stress.entity;

import java.util.Observable;

public class Player extends Observable {
	private int[] openCards = new int[4];
	private Deck deck;
	private int cardsPlayed;

	public Player() {
		deck = new Deck();
		setup();
	}

	public void setup() {
		cardsPlayed = 0;
		for (int openCard = 0; openCard < 4; openCard++) {
			loadOpenCard(openCard);
		}
	}

	/**
	 * draws and removes one card from the deck
	 */
	public int getCardFromDeck() {
		return deck.draw();
	}

	private void loadOpenCard(int openCardSlot) {
		if (deck.size() > 0) {
			openCards[openCardSlot] = deck.draw();
		} else {
			openCards[openCardSlot] = 0;
		}
	}

	public int getOpenCard(int openCardSlot) {
		return openCards[openCardSlot];
	}

	public void playOpenCard(int openCardSlot) {
		loadOpenCard(openCardSlot);
		cardsPlayed++;
	}

	public int getCardsPlayed() {
		return cardsPlayed;
	}

	public int remainingCards() {
		return deck.size();
	}

	public boolean finished() {
		return deck.size() == 0;
	}
}
