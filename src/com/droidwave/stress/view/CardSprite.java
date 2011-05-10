package com.droidwave.stress.view;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

public class CardSprite extends Sprite {
	private static TextureRegion[][] textureRegionsFront;
	private static TextureRegion[] textureRegionsBack;

	public CardSprite(int color, int value) {
		super(0, 0, textureRegionsFront[color][value]);
	}

	public CardSprite(int set) {
		super(0, 0, textureRegionsBack[set]);
	}

	public static void loadResources(Start game) {
		TextureRegionFactory.setAssetBasePath("gfx/cards");
		// a card is 64x96 pixels
		textureRegionsFront = new TextureRegion[4][13];
		textureRegionsBack = new TextureRegion[2];

		Texture texture = new Texture(1024, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		for (int set = 0; set < 2; set++) {
			textureRegionsBack[set] = TextureRegionFactory.createFromAsset(
					texture, game, "back_" + set + ".png", 64 * (13 + set), 0);
		}
		for (int color = 0; color < 4; color++) {
			for (int value = 0; value < 13; value++) {
				textureRegionsFront[color][value] = TextureRegionFactory
						.createFromAsset(texture, game, "color" + color
								+ "_value" + value + ".png", 64 * value,
								96 * color);
			}
		}

		game.getEngine().getTextureManager().loadTexture(texture);
	}
}
