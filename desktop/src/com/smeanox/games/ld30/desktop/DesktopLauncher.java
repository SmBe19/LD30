package com.smeanox.games.ld30.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.smeanox.games.ld30.Consts;
import com.smeanox.games.ld30.LD30;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = 960;
		config.height = 540;
		
		config.title = Consts.GameName;
		
		new LwjglApplication(new LD30(), config);
	}
}
