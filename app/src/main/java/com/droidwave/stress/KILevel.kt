package com.droidwave.stress

enum class KILevel(val timePerCheck: Long, val nameResId: Int) {
    EASY(2000L, R.string.easy),
    MEDIUM(1500L, R.string.medium),
    HARD(500L, R.string.hard)
}
