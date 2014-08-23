package com.smeanox.games.ld30.game;

import java.util.ArrayList;

import com.smeanox.games.ld30.Consts;

public class Train {
	public double capacity;
	public double goods[];

	public ArrayList<Integer> route;
	public int aRouteElement;
	public int location, nextLocation;
	public double progress;
	public double pause;

	public Train() {
		goods = new double[Consts.goodsCount];

		route = new ArrayList<Integer>();
	}

}
