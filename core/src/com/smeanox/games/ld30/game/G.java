package com.smeanox.games.ld30.game;

import java.util.ArrayList;
import java.util.LinkedList;

import com.smeanox.games.ld30.Consts;

public class G {

	public static ArrayList<Train> trains;
	public static Field[][] board;
	private static ArrayList<ArrayList<Integer>> boardGraph;
	public static int money;

	public static void init() {
		board = new Field[Consts.boardHeight][Consts.boardWidth];
		boardGraph = new ArrayList<ArrayList<Integer>>();
		trains = new ArrayList<Train>();

		for (int y = 0; y < Consts.boardHeight; y++) {
			for (int x = 0; x < Consts.boardWidth; x++) {
				board[y][x] = new Field();
			}
		}

		for (int i = 0; i < Consts.boardWidth * Consts.boardHeight; i++) {
			boardGraph.add(new ArrayList<Integer>());
		}

		money = Consts.startMoney;

		int x, y;
		for (int i = 0; i < Consts.startCityCount; i++) {
			do {
				x = Consts.rnd.nextInt(Consts.boardWidth);
				y = Consts.rnd.nextInt(Consts.boardHeight);
			} while (board[y][x].city != null);

			board[y][x].city = new City();
		}
	}

	public static void addRail(int start, int ende) {
		if (start > ende) {
			addRail(ende, start);
			return;
		}

		if (start < 0 || ende < 0
				|| start >= Consts.boardWidth * Consts.boardHeight
				|| ende >= Consts.boardWidth * Consts.boardHeight)
			return;

		int addStep = 1, bitAdd = 0;
		if (start / Consts.boardWidth == ende / Consts.boardWidth) {
			addStep = 1;
			bitAdd = 10;
			board[start / Consts.boardWidth][start % Consts.boardWidth].rails |= 8;
			board[ende / Consts.boardWidth][ende % Consts.boardWidth].rails |= 2;
		} else if (start % Consts.boardWidth == ende % Consts.boardWidth) {
			addStep = Consts.boardWidth;
			bitAdd = 5;
			board[start / Consts.boardWidth][start % Consts.boardWidth].rails |= 4;
			board[ende / Consts.boardWidth][ende % Consts.boardWidth].rails |= 1;
		} else {
			return;
		}
		boardGraph.get(start).add(start + addStep);
		boardGraph.get(ende).add(ende - addStep);

		for (int i = start + addStep; i < ende; i += addStep) {
			board[i / Consts.boardWidth][i % Consts.boardWidth].rails |= bitAdd;
			boardGraph.get(i).add(i + addStep);
			boardGraph.get(i).add(i - addStep);
		}
	}

	private static int getNextLocation(int start, int destination) {
		LinkedList<Integer> q = new LinkedList<Integer>();
		int par[] = new int[boardGraph.size()];

		for (int i = 0; i < boardGraph.size(); i++) {
			par[i] = -1;
		}

		q.add(start);

		par[start] = start;

		while (!q.isEmpty()) {
			int aEl = q.peek();
			q.remove();

			for (int i = 0; i < boardGraph.get(aEl).size(); i++) {
				if (par[boardGraph.get(aEl).get(i)] == -1) {
					par[boardGraph.get(aEl).get(i)] = aEl;
					q.add(boardGraph.get(aEl).get(i));
				}
			}
		}

		if (par[destination] == -1)
			return start;

		int sol = destination;
		while (par[sol] != start) {
			sol = par[sol];
		}
		return sol;
	}

	private static boolean processTrainstation(Train train, City city) {
		if (city == null)
			return false;
		for (int i = 0; i < Consts.goodsCount; i++) {
			city.goods[i] += train.goods[i];
			train.goods[i] = 0;
		}

		double cap = train.capacity;
		while (cap >= 1) {
			for (int i = 0; i < Consts.goodsCount; i++) {
				if (city.goods[i] >= 1) {
					city.goods[i]--;
					train.goods[i]++;
					cap--;
				}
			}
		}

		return true;
	}

	public static void update(float delta) {
		for (Train train : trains) {
			if (train.route.size() == 0)
				continue;
			if (train.pause > 0) {
				train.pause -= delta * Consts.trainVelo;
			} else {
				train.progress += delta * Consts.trainVelo;
			}
			if (train.progress >= 1) {
				train.progress = 0;
				train.location = train.nextLocation;
				if (train.location == (int) train.route
						.get(train.aRouteElement)) {
					if (processTrainstation(train, board[train.location
							/ Consts.boardWidth][train.location
							% Consts.boardWidth].city)) {
						train.pause = 1;
					}
					train.aRouteElement++;
					train.aRouteElement %= train.route.size();
				}
				train.nextLocation = getNextLocation(train.location,
						train.route.get(train.aRouteElement));
			}
		}
		for (int y = 0; y < Consts.boardHeight; y++) {
			for (int x = 0; x < Consts.boardWidth; x++) {
				if (board[y][x].city != null) {
					for (int i = 0; i < Consts.goodsCount; i++) {
						board[y][x].city.goods[i] += board[y][x].city.goodsChange[i]
								* delta;
					}
				}
			}
		}
	}
}
