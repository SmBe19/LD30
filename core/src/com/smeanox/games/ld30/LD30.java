package com.smeanox.games.ld30;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.smeanox.games.ld30.screens.GameScreen;

public class LD30 extends Game {

	@Override
	public void create() {
		Consts.calcScale();
		Consts.rnd = new Random();
		
		Assets.load();
		
		setScreen(new GameScreen());
	}
}
