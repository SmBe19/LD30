package com.smeanox.games.ld30.game;

import java.util.ArrayList;
import java.util.LinkedList;

import com.smeanox.games.ld30.Assets;
import com.smeanox.games.ld30.Consts;

public class G {

	public static ArrayList<Train> trains;
	public static Field[][] board;
	private static ArrayList<ArrayList<Integer>> boardGraph;
	private static ArrayList<Integer> cityPos;
	public static double money;
	public static boolean won;

	public static void init() {
		board = new Field[Consts.boardHeight][Consts.boardWidth];
		boardGraph = new ArrayList<ArrayList<Integer>>();
		trains = new ArrayList<Train>();
		cityPos = new ArrayList<Integer>();

		for (int y = 0; y < Consts.boardHeight; y++) {
			for (int x = 0; x < Consts.boardWidth; x++) {
				board[y][x] = new Field();
			}
		}

		for (int i = 0; i < Consts.boardWidth * Consts.boardHeight; i++) {
			boardGraph.add(new ArrayList<Integer>());
		}

		money = Consts.startMoney;

		do {
			for (int i = 0; i < Consts.obstacleCount; i++) {
				addObstacle();
			}
		} while (!checkObstacles());

		for (int i = 0; i < Consts.startCityCount; i++) {
			addCity();
		}

		for (int i = 0; i < Consts.startTrainCount; i++) {
			addTrain();
		}

		won = false;
	}

	private static boolean checkObstacles() {
		return true;
	}

	public static void editRail(int start, int ende, boolean delete) {
		if (start > ende) {
			editRail(ende, start, delete);
			return;
		}

		if (start < 0 || ende < 0
				|| start >= Consts.boardWidth * Consts.boardHeight
				|| ende >= Consts.boardWidth * Consts.boardHeight
				|| start == ende)
			return;

		int addStep = 1, bitAdd = 0;
		if (start / Consts.boardWidth == ende / Consts.boardWidth) {
			addStep = 1;
			bitAdd = delete ? 5 : 10;
			if (delete) {
				if (!board[start / Consts.boardWidth][start % Consts.boardWidth].obstacle) {
					board[start / Consts.boardWidth][start % Consts.boardWidth].rails &= 7;
				}
				if (!board[ende / Consts.boardWidth][ende % Consts.boardWidth].obstacle) {
					board[ende / Consts.boardWidth][ende % Consts.boardWidth].rails &= 13;
				}
			} else {
				if (!board[start / Consts.boardWidth][start % Consts.boardWidth].obstacle) {
					board[start / Consts.boardWidth][start % Consts.boardWidth].rails |= 8;
				}
				if (!board[ende / Consts.boardWidth][ende % Consts.boardWidth].obstacle) {
					board[ende / Consts.boardWidth][ende % Consts.boardWidth].rails |= 2;
				}
			}
		} else if (start % Consts.boardWidth == ende % Consts.boardWidth) {
			addStep = Consts.boardWidth;
			bitAdd = delete ? 10 : 5;
			if (delete) {
				if (!board[start / Consts.boardWidth][start % Consts.boardWidth].obstacle) {
					board[start / Consts.boardWidth][start % Consts.boardWidth].rails &= 11;
				}
				if (!board[ende / Consts.boardWidth][ende % Consts.boardWidth].obstacle) {
					board[ende / Consts.boardWidth][ende % Consts.boardWidth].rails &= 14;
				}
			} else {
				if (!board[start / Consts.boardWidth][start % Consts.boardWidth].obstacle) {
					board[start / Consts.boardWidth][start % Consts.boardWidth].rails |= 4;
				}
				if (!board[ende / Consts.boardWidth][ende % Consts.boardWidth].obstacle) {
					board[ende / Consts.boardWidth][ende % Consts.boardWidth].rails |= 1;
				}
			}
		} else {
			return;
		}
		if (delete) {
			if (!board[start / Consts.boardWidth][start % Consts.boardWidth].obstacle) {
				while (boardGraph.get(start).remove(
						new Integer(start + addStep)))
					money += Consts.moneyRemoveRail;
			}
			if (!board[ende / Consts.boardWidth][ende % Consts.boardWidth].obstacle) {
				while (boardGraph.get(ende).remove(new Integer(ende - addStep)))
					money += Consts.moneyRemoveRail;
			}
		} else {
			if (!board[start / Consts.boardWidth][start % Consts.boardWidth].obstacle) {
				if (!boardGraph.get(start).contains(
						new Integer(start + addStep))) {
					boardGraph.get(start).add(start + addStep);
					money += Consts.moneyAddRail;
				}
			}
			if (!board[ende / Consts.boardWidth][ende % Consts.boardWidth].obstacle) {
				if (!boardGraph.get(ende).contains(new Integer(ende - addStep))) {
					boardGraph.get(ende).add(ende - addStep);
					money += Consts.moneyAddRail;
				}
			}
		}

		for (int i = start + addStep; i < ende; i += addStep) {
			if (board[i / Consts.boardWidth][i % Consts.boardWidth].obstacle) {
				continue;
			}
			if (delete) {
				board[i / Consts.boardWidth][i % Consts.boardWidth].rails &= bitAdd;
				while (boardGraph.get(i).remove(new Integer(i + addStep)))
					money += Consts.moneyRemoveRail;
				while (boardGraph.get(i).remove(new Integer(i - addStep)))
					money += Consts.moneyRemoveRail;
			} else {
				board[i / Consts.boardWidth][i % Consts.boardWidth].rails |= bitAdd;
				if (!boardGraph.get(i).contains(new Integer(i + addStep))) {
					boardGraph.get(i).add(i + addStep);
					money += Consts.moneyAddRail;
				}
				if (!boardGraph.get(i).contains(new Integer(i - addStep))) {
					boardGraph.get(i).add(i - addStep);
					money += Consts.moneyAddRail;
				}
			}
		}
	}

	public static void addTrain() {
		Train tmpTrain = new Train();

		shuffleTrain(tmpTrain);

		tmpTrain.location = tmpTrain.nextLocation = tmpTrain.route
				.get(tmpTrain.route.size() - 1);

		trains.add(tmpTrain);
	}

	public static void addCity() {
		if (cityPos.size() == Consts.boardWidth * Consts.boardHeight
				- Consts.obstacleCount) {
			return;
		}

		int x, y;
		do {
			x = Consts.rnd.nextInt(Consts.boardWidth);
			y = Consts.rnd.nextInt(Consts.boardHeight);
		} while (board[y][x].city != null || board[y][x].obstacle);

		board[y][x].city = new City();
		cityPos.add(y * Consts.boardWidth + x);
		shuffleTrains();
	}

	public static void addObstacle() {
		int x, y;
		do {
			x = Consts.rnd.nextInt(Consts.boardWidth);
			y = Consts.rnd.nextInt(Consts.boardHeight);
		} while (board[y][x].obstacle);

		board[y][x].obstacle = true;
	}

	public static void destruction() {
		int start;
		for (int i = 0; i < Consts.destructionSize; i++) {
			start = Consts.rnd.nextInt(Consts.boardWidth * Consts.boardHeight);

			editRail(
					start,
					start
							+ (Consts.rnd.nextInt(Consts.boardWidth / 2) - Consts.boardWidth / 4)
							* (Consts.rnd.nextBoolean() ? 1 : Consts.boardWidth),
					true);
		}
	}

	public static void shuffleTrains() {
		for (Train train : trains) {
			shuffleTrain(train);
		}
	}

	private static void shuffleTrain(Train train) {
		train.route.clear();
		int stopCounts = Consts.rnd.nextInt(Consts.trainMaxStops) + 2;
		int lastCity = -1;
		int nextCity = -1;
		for (int j = 0; j < stopCounts; j++) {
			do {
				nextCity = cityPos.get(Consts.rnd.nextInt(cityPos.size()));
			} while (nextCity == lastCity);

			train.route.add(nextCity);
			lastCity = nextCity;
		}
		train.aRouteElement = 0;
	}

	public static void upgradeCapacity() {
		if (money + Consts.moneyMoreCapacity < 0) {
			return;
		}

		money += Consts.moneyMoreCapacity;

		Consts.capacityReloadVelo += Consts.capacityUpgradeAdd;
		Consts.maxCapacity += Consts.capacityUpgradeAddMax;
		Consts.addTrainRate += Consts.capacityUpgradeAddTrainRate;
		Consts.addCityRate += Consts.capacityUpgradeAddCityRate;
		Consts.moneyStuck *= Consts.capacityUpgradeMultStuck;
		Consts.moneySubscription *= Consts.capacityUpgradeMultSubscription;

		Consts.moneyMoreCapacity *= Consts.capacityUpgradeMultMoney;
	}

	private static int getNextLocation(int start, int destination) {
		LinkedList<Integer> q = new LinkedList<Integer>();
		int par[] = new int[boardGraph.size()];
		int cap[] = new int[boardGraph.size()];

		for (int i = 0; i < boardGraph.size(); i++) {
			par[i] = -1;
			cap[i] = 0;
		}

		q.add(start);

		par[start] = start;

		while (!q.isEmpty()) {
			int aEl = q.peek();
			q.remove();

			if (cap[aEl] < Consts.capacityReachedAddBFS
					&& board[aEl / Consts.boardWidth][aEl % Consts.boardWidth].capacity < Consts.capacityReloadVelo) {
				cap[aEl]++;
				q.add(aEl);
				continue;
			}

			int rndAdd = 0;
			if (boardGraph.get(aEl).size() > 0) {
				rndAdd = Consts.rnd.nextInt(boardGraph.get(aEl).size());
			}
			for (int i = 0; i < boardGraph.get(aEl).size(); i++) {
				if (par[boardGraph.get(aEl).get(
						(i + rndAdd) % boardGraph.get(aEl).size())] < 0) {
					par[boardGraph.get(aEl).get(
							(i + rndAdd) % boardGraph.get(aEl).size())] = aEl;
					q.add(boardGraph.get(aEl).get(
							(i + rndAdd) % boardGraph.get(aEl).size()));
				}
			}
		}

		if (par[destination] < 0)
			return start;

		int sol = destination;
		while (par[sol] != start) {
			sol = par[sol];
		}
		return sol;
	}

	private static int getDistance(int start, int ende) {
		int x, y;
		x = Math.abs((start % Consts.boardWidth) - (ende % Consts.boardWidth));
		y = Math.abs((start / Consts.boardWidth) - (ende / Consts.boardWidth));
		return x + y;
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

		money += Consts.moneyDelivered * train.distToDest;

		return true;
	}

	public static void update(float delta) {
		// Trains
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

					train.distToDest = getDistance(train.location,
							train.route.get(train.aRouteElement));
				}
				if (board[train.location / Consts.boardWidth][train.location
						% Consts.boardWidth].capacity > 1) {
					train.nextLocation = getNextLocation(train.location,
							train.route.get(train.aRouteElement));
					if (train.nextLocation != train.location) {
						board[train.location / Consts.boardWidth][train.location
								% Consts.boardWidth].capacity--;
					} else {
						money += Consts.moneyStuck;
					}
				} else {
					money += Consts.moneyStuck;
				}
			}
		}

		// Cities
		for (int y = 0; y < Consts.boardHeight; y++) {
			for (int x = 0; x < Consts.boardWidth; x++) {
				if (board[y][x].city != null) {
					for (int i = 0; i < Consts.goodsCount; i++) {
						board[y][x].city.goods[i] += board[y][x].city.goodsChange[i]
								* delta;
					}
					board[y][x].capacity += Consts.capacityReloadVelo * delta
							* 1;
				}
				board[y][x].capacity += Consts.capacityReloadVelo * delta;
				board[y][x].capacity = Math.min(board[y][x].capacity,
						Consts.maxCapacity);
			}
		}

		money += Consts.moneySubscription * delta;

		if (Consts.rnd.nextDouble() < Consts.addCityRate) {
			addCity();
			if (!Assets.mute) {
				Assets.newCitySound.play();
			}
		}
		if (Consts.rnd.nextDouble() < Consts.addTrainRate) {
			addTrain();
		}
		if (Consts.rnd.nextDouble() < Consts.destructionRate) {
			destruction();
			if (!Assets.mute) {
				Assets.destructionSound.play();
			}
		}

		if (money > Consts.moneyNextLevel) {
			won = true;
		}
	}
}
