package com.droidwave.stress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {
	private List<Integer> cards;

	Deck(int suitSize, int suitCount) {
		cards = new ArrayList<>();
		init(suitSize, suitCount);
	}

	private void init(int suitSize, int suitCount) {
		for (int i = 0; i < suitCount; i++) {
			for (int j = 0; j < suitSize; j++) {
				cards.add(j + 1);
			}
		}
		Collections.shuffle(cards);
	}

	int draw() {
		if (cards.isEmpty()) {
			return 0;
		}
		int cardIndex = new Random().nextInt(cards.size());
		return cards.remove(cardIndex);
	}

	int size() {
		return cards.size();
	}

	public void add(List<Integer> newDeck) {
		cards.addAll(newDeck);
		Collections.shuffle(cards);
	}

	void add(int card) {
		cards.add(card);
	}
}
