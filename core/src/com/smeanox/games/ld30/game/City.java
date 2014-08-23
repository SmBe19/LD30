package com.smeanox.games.ld30.game;

import com.smeanox.games.ld30.Consts;

public class City {
	public double goods[];
	public double goodsChange[];
	public String name;

	public City() {
		goods = new double[Consts.goodsCount];
		goodsChange = new double[Consts.goodsCount];
		name = "City";
	}
}
