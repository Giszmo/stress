/**
 * 
 */
package com.droidwave.stress.entity;

public enum Difficulty {
	EASY(2000), MEDIUM(1000), HARD(500);

	int delay;

	Difficulty(int delay) {
		this.delay = delay;
	}

	public static Difficulty getByDelay(int delayMs) {
		int smallestError = Integer.MAX_VALUE;
		Difficulty difficulty = null;
		for (Difficulty d : Difficulty.values()) {
			int error = Math.abs(d.delay - delayMs);
			if (error < smallestError) {
				difficulty = d;
				smallestError = error;
			}
		}
		return difficulty;
	}

	public int getDelay() {
		return delay;
	}
}