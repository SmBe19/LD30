package com.smeanox.games.ld30;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class Consts {
	public static String GameName = "ISP Simulator 3000";

	public static int devWidth = 960;
	public static int devHeight = 540;
	public static int screenWidth, screenHeight;
	public static double screenScale = 1;
	public static double cursorCorrectionX = 1;
	public static double cursorCorrectionY = 1;

	public static double fieldSize;
	public static double fieldsPerWidth = 8;
	public static double zoomVelo = 1;

	public static void calcScale() {
		screenScale = (double) Gdx.graphics.getHeight() / devHeight;
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

		menuItemWidth *= screenScale;
		menuItemHeight *= screenScale;

		menuFontSize *= screenScale;
	}

	public static void resetValues() {
		trainVelo = 2;
		capacityReloadVelo = 1;
		maxCapacity = 25;

		obstacleCount = boardWidth * boardWidth / 32;

		addCityRate = 0.0005;
		addTrainRate = 0.003;
		destructionRate = 0.0005;
		destructionSize = boardWidth / 7;

		moneyAddRail = -10;
		moneyRemoveRail = 10;
		moneySubscription = -2;
		moneyStuck = -0.5;
		moneyDelivered = 2;
		moneyMoreCapacity = -2000;

	}

	public static Random rnd;

	public static int boardWidth = 8;
	public static int boardHeight = 4;
	public static int goodsCount = 4;
	public static String goodsNames[] = new String[] { "A", "B", "C", "D" };

	public static int startMoney = 250;
	public static int startCityCount = 3;
	public static int startTrainCount = 1;
	public static int trainMaxStops = 16;
	public static double capacityReachedAddBFS = 10;
	public static double capacityUpgradeAdd = 1;
	public static double capacityUpgradeAddMax = 50;
	public static double capacityUpgradeMultStuck = 2;
	public static double capacityUpgradeMultSubscription = 2;
	public static double capacityUpgradeAddTrainRate = 0.002;
	public static double capacityUpgradeAddCityRate = 0.0005;
	public static double capacityUpgradeMultMoney = 2;

	public static double addCityRate;
	public static double addTrainRate;
	public static double destructionRate;
	public static double destructionSize;
	public static double trainVelo;
	public static double capacityReloadVelo;
	public static double maxCapacity;
	public static int obstacleCount;

	// Money
	public static double moneyAddRail;
	public static double moneyRemoveRail;
	public static double moneySubscription;
	public static double moneyStuck;
	public static double moneyDelivered;
	public static double moneyMoreCapacity;
	public static double moneyNextLevel = 4000;

	public static double scrollMargin = 50;
	public static double scrollVelo = 250;
	public static double boardMargin = 25;

	// Appearance
	public static Color backgroundColor = Color.OLIVE;
	public static Color blockedBackgroundColor = Color.DARK_GRAY;
	public static Color activeBackgroundColor = Color.GREEN;
	public static Color gridColor = Color.LIGHT_GRAY;
	public static Color cityColor = Color.BLUE;
	public static Color cityLonelyColor = Color.RED;
	public static Color railColor = Color.BLACK;
	public static Color railFullColor = Color.RED;
	public static Color trainColor = Color.GREEN;
	public static Color trainStuckColor = Color.ORANGE;
	public static Color cursorColor = Color.RED;
	public static Color menuBackgroundColor = Color.BLACK;
	public static Color menuActiveBackgroundColor = Color.GRAY;
	public static Color menuFontColor = Color.YELLOW;

	public static double citySize = 0.5;
	public static double railTotSize = 0.2;
	public static double railSize = 0.05;
	public static double trainSize = 0.2;
	public static double cursorSize = 10;
	public static double menuItemWidth = 170;
	public static double upgradeItemWidth = 400;
	public static double menuItemHeight = 50;

	public static double menuFontSize = 0.5;

	public static boolean menuActive = false;
	public static boolean cursorActive = false;

	public static String menuText[] = new String[] { "Sim", "Build", "Manage" };

	public static double aOffsetX = 0, aOffsetY = 0;
}
