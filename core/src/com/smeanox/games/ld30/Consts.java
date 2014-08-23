package com.smeanox.games.ld30;

import com.badlogic.gdx.Gdx;

public class Consts {
	public static int devWidth = 960;
	public static int devHeight = 540;
	public static double screenScale = 1;

	
	public static void calcScale(){
		screenScale = (double)Gdx.graphics.getHeight() / devHeight;
	}
}
