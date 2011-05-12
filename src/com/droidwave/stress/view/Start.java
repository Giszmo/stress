package com.droidwave.stress.view;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.ui.activity.LayoutGameActivity;

import android.view.Display;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.droidwave.stress.R;
import com.droidwave.stress.entity.Configuration;
import com.droidwave.stress.entity.HighScore;
import com.droidwave.stress.entity.SoundSystem;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class Start extends LayoutGameActivity implements IOnSceneTouchListener {
	public static final int LAYER_BACKGROUND = 0;
	public static final int LAYER_CARDS = 1;
	public static final int LAYER_CONTROLS = 2;

	public float WIDTH;
	public float HEIGHT;

	private Scene scene;
	private AdView adView;

	@Override
	public Engine onLoadEngine() {
		Configuration.getInstance().load(this);
		SoundSystem.getInstance().init(this);

		WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();

		WIDTH = display.getWidth();
		HEIGHT = display.getHeight() - 75;

		Camera camera = new Camera(0, 0, WIDTH, HEIGHT);
		final EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(WIDTH,
						HEIGHT), camera);
		return new Engine(engineOptions);
	}

	@Override
	public void onLoadResources() {
		// HUD for splash screen *and* touchEvent coordinates to be calculated
		// correctly (weird)
		// one layer for the splash screen. one layer for the LevelMenuAsset.
		mEngine.getCamera().setHUD(new HUD(2));

		// load the resources for each Asset independently
		CardSprite.loadResources(this);
	}

	@Override
	public Scene onLoadScene() {
		// TODO: Leo 2011-03-04 performance-wise this logger is bad as it seams
		// to produce garbage that is never collected. (Didn't test it myself.
		// Read that on the forum.)
		mEngine.registerUpdateHandler(new FPSLogger());
		scene = new Scene(4);
		// scene.setOnSceneTouchListener(this);

		return scene;
	}

	// interface implementation
	public void onLoadComplete() {

		adView = (AdView) findViewById(R.id.ad);
		HighScore.getInstance().load(this);
	}

	@Override
	public void onGamePaused() {
		super.onGamePaused();
		SoundSystem.getInstance().stopBg();
	}

	@Override
	public void onGameResumed() {
		super.onGameResumed();
		SoundSystem.getInstance().playBg(R.raw.card);
		AdRequest adRequest = new AdRequest();
		adRequest.setTesting(true);
		adView.loadAd(adRequest);
	}

	// public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent
	// event) {
	// return TableAsset.onSceneTouchEvent(pScene, event);
	// }

	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			return onKeyMenu();
		}
		return super.onKeyDown(keyCode, event);
	}

	private boolean onKeyMenu() {
		// TODO Auto-generated method stub
		return false;
	}

	// Overrides for LayoutGameActivity
	@Override
	protected int getLayoutID() {
		return R.layout.main;
	}

	@Override
	protected int getRenderSurfaceViewID() {
		return R.id.rendersurfaceview;
	}

	// /Overrides for LayoutGameActivity

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return false;
	}
}
