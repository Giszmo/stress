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
	private int color;

	public Player() {
		setup();
		return;
	}

	public void setup() {
		cardsPlayed = 0;
		deck = new Deck(13, 1);
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

	public int getOpenCard(int openCardSlot) {
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

	public int remainingCards() {
		return deck.size();
	}

	public boolean finished() {
		if (deck.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return
	 */
	public int getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(int color) {
		this.color = color;
	}
}
