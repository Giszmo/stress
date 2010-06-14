/**
 * 
 */
package com.droidwave.stress;

/**
 * @author leo
 * 
 */
public class Player {
	private int[] openCards = new int[4];
	private Deck deck;
	private int cardsPlayed;

	public Player() {
		setup();
		return;
	}

	public void setup() {
		cardsPlayed = 0;
		deck = new Deck(13, 4);
		for (int openCard = 0; openCard < 4; openCard++) {
			loadOpenCard(openCard);
		}
	}

	public int getCardFromDeck() {
		return deck.draw();
	}

	private void loadOpenCard(int openCardSlot) {
		if (deck.size() > 0) {
			openCards[openCardSlot] = deck.draw();
		} else {
			openCards[openCardSlot] = 0;
		}
		return;
	}

	public int readOpenCard(int openCardSlot) {
		return openCards[openCardSlot];
	}

	public void playOpenCard(int openCardSlot) {
		loadOpenCard(openCardSlot);
		cardsPlayed++;
		return;
	}

	public int getCardsPlayed() {
		return cardsPlayed;
	}

	public boolean finished() {
		for (int openCard = 0; openCard < 4; openCard++) {
			if (openCards[openCard] != 0) {
				return false;
			}
		}
		return true;
	}
}
