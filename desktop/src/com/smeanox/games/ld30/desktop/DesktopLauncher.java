package com.smeanox.games.ld30.desktop;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.smeanox.games.ld30.Consts;
import com.smeanox.games.ld30.LD30;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();

		config.width = (int) (gd.getDisplayMode().getWidth() * 0.9);
		config.height = (int) (gd.getDisplayMode().getHeight() * 0.9);

		config.title = Consts.GameName;

		new LwjglApplication(new LD30(), config);
	}
}
