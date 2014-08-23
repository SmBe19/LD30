
package com.smeanox.games.ld30;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Assets {

	public static BitmapFont arial;
	
	public static void load(){
		arial = new BitmapFont(Gdx.files.internal("arial64.fnt"));
	}
}
