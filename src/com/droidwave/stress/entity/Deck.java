package com.droidwave.stress.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
	public static final int SUITE_COUNT = 4;
	public static final int SUIT_SIZE = 13;
	public static final int CARD_COUNT = SUITE_COUNT * SUIT_SIZE;

	private final List<Integer> cards = new ArrayList<Integer>();

	public Deck() {
		init();
	}

	private void init() {
		for (int i = 0; i < SUITE_COUNT; i++) {
			for (int j = 0; j < SUIT_SIZE; j++) {
				cards.add(j + 1);
			}
		}
		Collections.shuffle(cards);
	}

	public int draw() {
		if (cards.size() <= 0) {
			return 0;
		}
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
	}

}
