package com.droidwave.stress

import java.util.HashMap
import java.util.Random
import java.util.Timer
import java.util.TimerTask

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import com.droidwave.stress.view.MirrorButton
import com.droidwave.stress.view.StackSizeIndicator

class Stress : AppCompatActivity() {
    private var timer: Timer? = null
    private val player: Array<Player> = arrayOf(Player(Deck()), Player(Deck()))
    private val centerDeck = arrayOfNulls<Deck>(2)
    // buttonIds[player][card]
    private val buttonIds = arrayOf(intArrayOf(R.id.Button01P1, R.id.Button02P1, R.id.Button03P1, R.id.Button04P1), intArrayOf(R.id.Button01P2, R.id.Button02P2, R.id.Button03P2, R.id.Button04P2))

    private val currentValue = IntArray(2)
    private var gameMode = GameMode.SINGLEPLAYER
    private var kiLevel = KILevel.EASY

    private val clickListener = object : OnClickListener {
        override fun onClick(v: View) {
            val button = v as Button
            val playerNumber = getPlayerByButton(button)
            val cardNumber = getPlayedCardNumber(button, player[playerNumber])
            playCard(playerNumber, cardNumber)
        }

        private fun getPlayedCardNumber(button: Button, player: Player): Int {
            for (p in 0..1) {
                for (c in 0..3) {
                    if (buttonIds[p][c] == button.id) {
                        return c
                    }
                }
            }
            throw Error("Unknown Button pressed")
        }

        private fun getPlayerByButton(button: Button): Int {
            for (p in 0..1) {
                for (c in 0..3) {
                    if (buttonIds[p][c] == button.id) {
                        return p
                    }
                }
            }
            throw Error("Unknown Button pressed")
        }
    }

    public override fun onResume() {
        startKI()
        super.onResume()
    }

    private fun doKiStep() {
        val playerPick: Int
        if (gameMode == GameMode.DEMO) {
            playerPick = Random().nextInt(2)
        } else {
            playerPick = 1
        }
        val cardPick = Random().nextInt(4)
        playCard(playerPick, cardPick)
    }

    private fun startKI() {
        timer = Timer()
        timer!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                this@Stress.runOnUiThread { doKiStep() }
            }
        }, kiLevel.timePerCheck, kiLevel.timePerCheck)
    }

    public override fun onPause() {
        stopKI()
        super.onPause()
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        addClickListener()
        initGame()
    }

    private fun addClickListener() {
        for (playerButtonIds in buttonIds) {
            for (buttonId in playerButtonIds) {
                val button = findViewById<Button>(buttonId)
                button.setOnClickListener(clickListener)
            }
        }
    }

    private fun initGame() {
        stopKI()
        // base setup
        for (playerNumber in 0..1) {
            player[playerNumber] = Player(Deck())
            centerDeck[playerNumber] = Deck()
            val randomCard = player[playerNumber].cardFromDeck
            setCenterStack(playerNumber, randomCard, playerNumber)
            updateInfo(playerNumber)
        }
        player[0].color = Color.argb(0xff, 0xff, 0x00, 0x00)
        player[1].color = Color.argb(0xff, 0x00, 0x00, 0xff)
        updateInfo(0)
        updateInfo(1)
        var playerButton: MirrorButton? = null
        for (p in 0..1) {
            var openCardId = 0
            for (buttonId in buttonIds[p]) {
                playerButton = findViewById(buttonId)
                playerButton!!.text = "" + player[p].getOpenCard(openCardId++)
                playerButton.notifyColor = player[p].color
            }
        }
        ensurePlayability()
        if (gameMode != GameMode.MULTIPLAYER) {
            startKI()
        }
    }

    private fun stopKI() {
        if (timer == null) {
            return
        }
        timer!!.cancel()
        timer = null
    }

    private fun setCenterStack(stack: Int, card: Int, playerNumber: Int) {
        val stackToUpdate: MirrorButton?
        if (stack == 0) {
            stackToUpdate = findViewById(R.id.ButtonStack1)
            currentValue[0] = card
        } else {
            stackToUpdate = findViewById(R.id.ButtonStack2)
            currentValue[1] = card
        }
        centerDeck[stack]!!.add(card)
        stackToUpdate!!.notifyColor = player[playerNumber].color
        stackToUpdate.text = "" + card
    }

    private fun playerWon() {
        if (player[0].finished() && player[1].finished()) {
            toast(R.string.game_is_a_draw)
        } else if (player[0].finished()) {
            toast(getText(R.string.player_n_wins).toString().replaceFirst("%n1%".toRegex(), "1"))
        } else if (player[1].finished()) {
            toast(getText(R.string.player_n_wins).toString().replaceFirst("%n1%".toRegex(), "2"))
        }
        initGame()
    }

    private fun updateInfo(playerNumber: Int) {
        var info: StackSizeIndicator? = null
        if (playerNumber == 0) {
            info = findViewById(R.id.Info1)
        } else {
            info = findViewById(R.id.Info2)
        }
        val remaining = player[playerNumber].remainingCards()
        info!!.setRemaining(remaining.toFloat() / (Deck.CARDS_IN_DECK - 4))
    }

    private fun ensurePlayability() {
        var playable = false
        for (playerNumber in 0..1) {
            for (cardNumber in 0..3) {
                for (currentValueNumber in 0..1) {
                    if (Math.abs(currentValue[currentValueNumber] - player[playerNumber].getOpenCard(cardNumber)) % 11 == 1) {
                        playable = true
                    }
                }
            }
        }
        if (!playable) {
            drawNewCenterCards()
            if (player[0].finished() || player[1].finished()) {
                playerWon()
            } else {
                ensurePlayability()
            }
        }
    }

    private fun drawNewCenterCards() {
        for (playerNumber in 0..1) {
            val randomCard = player[playerNumber].cardFromDeck
            setCenterStack(playerNumber, randomCard, playerNumber)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the currently selected menu XML resource.
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        Log.d("menu", "menu initialized")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.new_multiplayer -> {
                toast(R.string.new_multiplayer_started)
                gameMode = GameMode.MULTIPLAYER
                initGame()
            }
            R.id.new_singleplayer -> {
                toast(R.string.new_singleplayer_started)
                gameMode = GameMode.SINGLEPLAYER
                initGame()
            }
            R.id.new_demo -> {
                toast(R.string.new_demo_started)
                gameMode = GameMode.DEMO
                initGame()
            }
            R.id.easyy -> {
                toast(getText(R.string.ai_set_to).toString() + " " + getText(R.string.easy))
                kiLevel = KILevel.EASY
            }
            R.id.medium -> {
                toast(getText(R.string.ai_set_to).toString() + " " + getText(R.string.medium))
                kiLevel = KILevel.MEDIUM
            }
            R.id.hard -> {
                toast(getText(R.string.ai_set_to).toString() + " " + getText(R.string.hard))
                kiLevel = KILevel.HARD
            }
            R.id.submenu, R.id.info -> {
            }
            else -> throw Error("Unknown menu entry clicked: " + item.itemId)
        }
        return true
    }

    private fun toast(resId: Int) {
        toast(getText(resId))
    }

    private fun toast(charSequence: CharSequence) {
        toast("" + charSequence)
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun playCard(playerNumber: Int, cardNumber: Int) {
        val cardValue = player[playerNumber].getOpenCard(cardNumber)
        var playedACard = false
        if (Math.abs(currentValue[0] - cardValue) % 11 == 1) {
            setCenterStack(0, cardValue, playerNumber)
            playedACard = true
        } else if (Math.abs(currentValue[1] - cardValue) % 11 == 1) {
            setCenterStack(1, cardValue, playerNumber)
            playedACard = true
        }

        if (playedACard) {
            player[playerNumber].playOpenCard(cardNumber)
            val newCard = player[playerNumber].getOpenCard(cardNumber)
            val button = findViewById<Button>(buttonIds[playerNumber][cardNumber])
            button.text = "" + newCard
            ensurePlayability()
            updateInfo(playerNumber)
            if (player[playerNumber].finished()) {
                playerWon()
            }
        }
    }
}