package com.droidwave.stress.view;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

public class CardSprite extends Sprite {
	private static TextureRegion[][] textureRegionsFront;
	private static TextureRegion[] textureRegionsBack;

	/**
	 * Front side of the according card.
	 * 
	 * @param color
	 *            color is a value between 0 and 3
	 * @param value
	 *            value is a value between 0 and 12
	 */
	public CardSprite(int color, int value) {
		super(0, 0, textureRegionsFront[color][value]);
	}

	/**
	 * Back side of a card.
	 * 
	 * @param set
	 *            set is either 0 or 1
	 */
	public CardSprite(int set) {
		super(0, 0, textureRegionsBack[set]);
	}

	public static void loadResources(Start game) {
		TextureRegionFactory.setAssetBasePath("gfx/cards");
		// a card is 64x96 pixels.
		// my rummy deck here also has cards of 9cm x 6cm
		textureRegionsFront = new TextureRegion[4][13];
		textureRegionsBack = new TextureRegion[2];
		Texture texture = new Texture(1024, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		for (int color = 0; color < 4; color++) {
			for (int value = 0; value < 13; value++) {
				textureRegionsFront[color][value] = TextureRegionFactory
						.createFromAsset(texture, game, "color" + color
								+ "_value" + value + ".png", 64 * value,
								96 * color);
			}
		}
		for (int set = 0; set < 2; set++) {
			textureRegionsBack[set] = TextureRegionFactory.createFromAsset(
					texture, game, "back_" + set + ".png", 64 * (13 + set), 0);
		}
		game.getEngine().getTextureManager().loadTexture(texture);
	}
}
