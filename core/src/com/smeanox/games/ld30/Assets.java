package com.smeanox.games.ld30;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Assets {

	public static BitmapFont arial;
	public static Sound destructionSound, deliverySound, newCitySound,
			nextLevelSound;
	public static Music backgroundMusic;
	public static boolean mute;

	public static void load() {
		arial = new BitmapFont(Gdx.files.internal("arial64.fnt"));

		destructionSound = Gdx.audio.newSound(Gdx.files
				.internal("destruction.wav"));
		deliverySound = Gdx.audio.newSound(Gdx.files.internal("delivery.wav"));
		newCitySound = Gdx.audio.newSound(Gdx.files.internal("newCity.wav"));
		nextLevelSound = Gdx.audio
				.newSound(Gdx.files.internal("nextLevel.wav"));

		backgroundMusic = Gdx.audio.newMusic(Gdx.files
				.internal("backgroundMusic.wav"));
		backgroundMusic.setLooping(true);
		backgroundMusic.setVolume(0.5f);

		mute = false;
	}

	public static void setMute(boolean m) {
		mute = m;
		if (m) {
			backgroundMusic.pause();
		} else {
			backgroundMusic.play();
		}
	}
}
