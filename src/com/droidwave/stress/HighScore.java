package com.droidwave.stress;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HighScore extends Persistable {
	private static final HighScore INSTANCE = new HighScore();
	private static final String FILE_NAME = "highscore.data";

	public static HighScore getInstance() {
		return INSTANCE;
	}

	private HighScore() {
	}

	private Map<Integer, Double> scores = new HashMap<Integer, Double>();

	public void setScore(int levelId, double time) {
		scores.put(levelId, time);
		save();
	}

	public double getScore(int levelId) {
		Double score = scores.get(levelId);
		return score == null ? 0 : score;
	}

	@Override
	protected String getFileName() {
		return FILE_NAME;
	}

	@Override
	protected void load(JSONObject jsonObject) throws JSONException {
		JSONArray jsonScoreArray = jsonObject.getJSONArray("scores");
		for (int i = 0; i < jsonScoreArray.length(); i++) {
			JSONObject jsonScore = jsonScoreArray.getJSONObject(i);
			int levelId = jsonScore.getInt("levelId");
			double time = jsonScore.getDouble("time");
			setScore(levelId, time);
		}
	}

	@Override
	protected void save(JSONObject jsonObject) throws JSONException {
		JSONArray jsonScoreArray = new JSONArray();
		for (int levelId : scores.keySet()) {
			double time = scores.get(levelId);
			JSONObject jsonScore = new JSONObject();

			jsonScore.put("levelId", levelId);
			jsonScore.put("time", time);

			jsonScoreArray.put(jsonScore);
		}
		jsonObject.put("scores", jsonScoreArray);
	}
}
