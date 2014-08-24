package com.smeanox.games.ld30.game;

public class Field {
	public int rails;
	public City city;
	public double capacity;
	public boolean obstacle;

	public Field() {
		rails = 0;
		city = null;
		capacity = 0;
		obstacle = false;
	}
}
