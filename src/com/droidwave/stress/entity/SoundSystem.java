package com.droidwave.stress.entity;

import java.util.HashMap;
import java.util.Map;

import com.droidwave.stress.R;
import com.droidwave.stress.R.raw;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

public class SoundSystem {
	private static Context context;

	private SoundPool fxSoundPool;// for sound effects
	private Map<Integer, Integer> fxSoundPoolMap;
	private MediaPlayer bgMediaPlayer;// for background music
	private Configuration configuration;

	private SoundSystem() {
	}

	private static final SoundSystem INSTANCE = new SoundSystem();

	public static SoundSystem getInstance() {
		return INSTANCE;
	}

	public void init(Context context) {
		configuration = Configuration.getInstance();
		Log.d(SoundSystem.class.getName(), "init");
		SoundSystem.context = context;
		fxSoundPool = new SoundPool(15, AudioManager.STREAM_MUSIC, 100);
		fxSoundPoolMap = new HashMap<Integer, Integer>();
		initFxSounds();
	}

	/**
	 * @param sound
	 *            the R.raw.id of a sound file that should be played in the
	 *            background in a loop. This file may be big.
	 */
	public void playBg(int sound, boolean looping) {
		stopBg();
		bgMediaPlayer = MediaPlayer.create(context, sound);
		bgMediaPlayer.setLooping(looping);
		bgMediaPlayer.start();
		setVolume();
	}

	public void setVolume() {
		if (bgMediaPlayer != null) {
			float volume;
			if (configuration.isSound()) {
				volume = configuration.getVolumeBg();
			} else {
				volume = 0;
			}
			bgMediaPlayer.setVolume(volume, volume);
		}
	}

	public void playBg(int sound) {
		playBg(sound, true);
	}

	/**
	 * @param sound
	 *            the R.raw.id of a sound file that should play once. The sum of
	 *            all files played via this method is limited to about 1MB.
	 */
	public void playFx(int sound) {
		if (!fxSoundPoolMap.containsKey(sound)) {
			initFxSound(sound);
		}
		if (configuration.isSound()) {
			fxSoundPool.play(fxSoundPoolMap.get(sound), 1, 1, 1, 0, 1);
		}
	}

	private void initFxSounds() {
		Log.d(SoundSystem.class.getName(), "initializing sounds");
		for (int i : new int[] { R.raw.card }) {
			fxSoundPoolMap.put(i, fxSoundPool.load(context, i, 1));
		}
	}

	private void initFxSound(int sound) {
		fxSoundPoolMap.put(sound, fxSoundPool.load(context, sound, 1));
	}

	public void stopBg() {
		if (bgMediaPlayer != null) {
			bgMediaPlayer.stop();
		}
	}

	public void stopFx(int resId) {
		fxSoundPool.stop(fxSoundPoolMap.get(resId));
	}
}
