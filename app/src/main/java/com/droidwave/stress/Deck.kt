package com.droidwave.stress

import java.util.ArrayList
import java.util.Random

class Deck {
    private val cards: MutableList<Int> = ArrayList()

    init {
        for (i in 0 until DECK_SUITE_COUNT) {
            for (j in 0 until DECK_SIZE) {
                cards.add(j + 1)
            }
        }
        cards.shuffle()
    }

    internal fun draw(): Int {
        if (cards.isEmpty()) {
            return 0
        }
        val cardIndex = Random().nextInt(cards.size)
        return cards.removeAt(cardIndex)
    }

    internal fun size() = cards.size

    internal fun add(card: Int) {
        cards.add(card)
    }

    companion object {
        val DECK_SUITE_COUNT = 4
        val DECK_SIZE = 13
        val CARDS_IN_DECK = DECK_SUITE_COUNT * DECK_SIZE
    }
}
