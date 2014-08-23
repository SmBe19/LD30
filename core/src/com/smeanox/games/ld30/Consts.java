package com.smeanox.games.ld30;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class Consts {
	public static String GameName = "TrainSim";

	public static int devWidth = 960;
	public static int devHeight = 540;
	public static int screenWidth, screenHeight;
	public static double screenScale = 1;
	public static double cursorCorrectionX = 1;
	public static double cursorCorrectionY = 1;

	public static double fieldSize;
	public static double fieldsPerWidth = 8;

	public static void calcScale() {
		screenScale = (double) Gdx.graphics.getHeight() / devHeight;
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

		fieldSize = screenScale * devWidth / fieldsPerWidth;

		menuItemWidth *= screenScale;
		menuItemHeight *= screenScale;

		menuFontSize *= screenScale;
	}

	public static Random rnd;

	public static int boardWidth = 8;
	public static int boardHeight = 8;
	public static int goodsCount = 4;
	public static String goodsNames[] = new String[] { "A", "B", "C", "D" };

	public static int startMoney = 1000;
	public static int startCityCount = 4;
	public static double trainVelo = 1;

	public static double scrollMargin = 100;
	public static double scrollVelo = 250;
	public static double boardMargin = 100;

	// Appearance
	public static Color backgroundColor = Color.OLIVE;
	public static Color activeBackgroundColor = Color.GREEN;
	public static Color gridColor = Color.WHITE;
	public static Color cityColor = Color.BLUE;
	public static Color railColor = Color.BLACK;
	public static Color trainColor = Color.ORANGE;
	public static Color cursorColor = Color.RED;
	public static Color menuBackgroundColor = Color.BLACK;
	public static Color menuActiveBackgroundColor = Color.GRAY;
	public static Color menuFontColor = Color.ORANGE;

	public static double citySize = 0.5;
	public static double railTotSize = 0.2;
	public static double railSize = 0.05;
	public static double trainSize = 0.2;
	public static double cursorSize = 10;
	public static double menuItemWidth = 170;
	public static double menuItemHeight = 50;

	public static double menuFontSize = 0.5;

	public static String menuText[] = new String[] { "Sim", "Build", "Manage" };

	public static double aOffsetX = 0, aOffsetY = 0;
}
