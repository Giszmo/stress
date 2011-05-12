package com.droidwave.stress.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Configuration extends Persistable {
	private static final Configuration INSTANCE = new Configuration();
	private static final String FILE_NAME = "config.dat";

	public static Configuration getInstance() {
		return INSTANCE;
	}

	private Configuration() {
	}

	private Difficulty difficulty;
	private boolean sound;
	private float volumeBg;

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
		save();
	}

	public boolean isSound() {
		return sound;
	}

	public void setSound(boolean sound) {
		Log.d(this.getClass().getName(), "switching sound "
				+ (sound ? "on" : "off"));
		this.sound = sound;
		save();
	}

	public float getVolumeBg() {
		return volumeBg;
	}

	public void setVolumeBg(float volumeBg) {
		Log.d(this.getClass().getName(), "setting bg volume to " + volumeBg);
		this.volumeBg = volumeBg;
		save();
	}

	@Override
	protected String getFileName() {
		return FILE_NAME;
	}

	@Override
	public void load(JSONObject jsonObject) throws JSONException {
		int delay = jsonObject.optInt("delay", 1000);
		difficulty = Difficulty.getByDelay(delay);
		sound = jsonObject.optBoolean("sound", true);
		volumeBg = 1;// TODO: ?? (float) jsonObject.optDouble("volume", 1);
	}

	@Override
	protected void save(JSONObject jsonObject) throws JSONException {
		jsonObject.put("delay", difficulty.delay);
		jsonObject.put("sound", sound);
		jsonObject.put("volume", volumeBg);
	}

	public enum Difficulty {
		EASY(2000), MEDIUM(1000), HARD(500);

		private int delay;

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
}
