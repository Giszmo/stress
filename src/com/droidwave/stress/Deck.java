package com.droidwave.stress;

import java.util.Collections;
import java.util.List;

public class Deck {

	private List<Integer> cards;

	public Deck(int suitSize, int suitCount) {
		for (int i = 0; i < suitCount; i++) {
			for (int j = 0; j < suitSize; j++) {
				cards.add(j + 1);
			}
		}
		Collections.shuffle(cards);
	}

	public int draw() {
		int cardIndex = (int) Math.random() * cards.size();
		return cards.remove(cardIndex);
	}

	public int size() {
		return cards.size();
	}

	public void add(List<Integer> newDeck) {
		cards.addAll(newDeck);
		Collections.shuffle(cards);
	}

	public void add(int card) {
		cards.add(card);
		return;
	}

}
