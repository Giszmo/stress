package com.droidwave.stress

class Player(private val deck: Deck) {
    private val openCards = IntArray(4)
    var cardsPlayed: Int = 0
        private set
    internal var color: Int = 0

    internal val cardFromDeck: Int
        get() = deck.draw()

    init {
        for (cardIndex in 0..3) {
            loadOpenCard(cardIndex)
        }
    }

    private fun loadOpenCard(openCardSlot: Int) {
        if (deck.size() > 0) {
            openCards[openCardSlot] = deck.draw()
        } else {
            openCards[openCardSlot] = 0
        }
    }

    internal fun getOpenCard(openCardSlot: Int) = openCards[openCardSlot]

    internal fun playOpenCard(openCardSlot: Int) {
        loadOpenCard(openCardSlot)
        cardsPlayed++
    }

    internal fun remainingCards() = deck.size()

    internal fun finished() = deck.size() == 0
}
