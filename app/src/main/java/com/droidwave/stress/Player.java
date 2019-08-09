package com.droidwave.stress;

public class Player {
	private int[] openCards = new int[4];
	private Deck deck;
	private int cardsPlayed;
	private int color;

	Player(Deck deck) {
		this.deck = deck;
		setup();
	}

	private void setup() {
		cardsPlayed = 0;
		for (int openCard = 0; openCard < 4; openCard++) {
			loadOpenCard(openCard);
		}
	}

	int getCardFromDeck() {
		return deck.draw();
	}

	private void loadOpenCard(int openCardSlot) {
		if (deck.size() > 0) {
			openCards[openCardSlot] = deck.draw();
		} else {
			openCards[openCardSlot] = 0;
		}
	}

	int getOpenCard(int openCardSlot) {
		return openCards[openCardSlot];
	}

	void playOpenCard(int openCardSlot) {
		loadOpenCard(openCardSlot);
		cardsPlayed++;
	}

	public int getCardsPlayed() {
		return cardsPlayed;
	}

	int remainingCards() {
		return deck.size();
	}

	boolean finished() {
		return deck.size() == 0;
	}

	int getColor() {
		return color;
	}

	void setColor(int color) {
		this.color = color;
	}
}
