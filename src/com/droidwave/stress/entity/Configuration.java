package com.droidwave.stress.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Configuration extends Persistable {
	private static final Configuration INSTANCE = new Configuration();
	private static final String FILE_NAME = "config.dat";

	private Difficulty difficulty;
	private boolean sound;

	public static Configuration getInstance() {
		return INSTANCE;
	}

	private Configuration() {
	}

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

	@Override
	protected String getFileName() {
		return FILE_NAME;
	}

	@Override
	public void load(JSONObject jsonObject) throws JSONException {
		int delay = jsonObject.optInt("delay", 1000);
		difficulty = Difficulty.getByDelay(delay);
		sound = jsonObject.optBoolean("sound", true);
	}

	@Override
	protected void save(JSONObject jsonObject) throws JSONException {
		jsonObject.put("delay", difficulty.delay);
		jsonObject.put("sound", sound);
	}
}
