package com.smeanox.games.ld30.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.smeanox.games.ld30.Assets;
import com.smeanox.games.ld30.Consts;
import com.smeanox.games.ld30.game.G;
import com.smeanox.games.ld30.game.Train;

public class GameScreen implements Screen {

	ShapeRenderer shapeRenderer;
	SpriteBatch spriteBatch;

	int railBuildStart, aRailBuild;

	int aMode; // 0 = Watch, 1 = Build Rails, 2 = Configure Trains, 3 =
				// Configure Train

	public GameScreen() {

		shapeRenderer = new ShapeRenderer();

		spriteBatch = new SpriteBatch();

		Gdx.input.setInputProcessor(new InputProcessor() {

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer,
					int button) {
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer,
					int button) {
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				Consts.fieldSize -= amount * Consts.zoomVelo;
				Consts.fieldSize = Math.max(Consts.fieldSize, 1);
				return true;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				return false;
			}

			@Override
			public boolean keyDown(int keycode) {
				return false;
			}
		});

		init();
	}

	public void init() {
		Consts.resetValues();

		G.init();

		aMode = 1;

		railBuildStart = -1;

		Consts.fieldSize = Consts.screenWidth / Consts.fieldsPerWidth;
	}

	@Override
	public void render(float delta) {
		G.update(delta);
		if (G.won) {
			Consts.boardWidth *= 2;
			Consts.boardHeight *= 2;
			Consts.moneyNextLevel *= 4;
			Consts.fieldsPerWidth = Consts.boardWidth;
			init();
			return;
		}

		Gdx.gl.glClearColor(Consts.backgroundColor.r, Consts.backgroundColor.g,
				Consts.backgroundColor.b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Consts.gridColor);

		for (int y = 0; y < Consts.boardHeight; y++) {
			for (int x = 0; x < Consts.boardWidth; x++) {
				shapeRenderer.rect((float) (Consts.aOffsetX + x
						* Consts.fieldSize), (float) (Consts.aOffsetY + y
						* Consts.fieldSize), (float) (Consts.fieldSize),
						(float) (Consts.fieldSize));
			}
		}

		shapeRenderer.end();

		shapeRenderer.begin(ShapeType.Filled);

		// Rails & Cities
		for (int y = 0; y < Consts.boardHeight; y++) {
			for (int x = 0; x < Consts.boardWidth; x++) {

				// Building
				if ((railBuildStart != -1 && aRailBuild != -1 && ((railBuildStart
						/ Consts.boardWidth == aRailBuild / Consts.boardWidth
						&& y == railBuildStart / Consts.boardWidth
						&& x <= Math.max(railBuildStart % Consts.boardWidth,
								aRailBuild % Consts.boardWidth) && x >= Math
						.min(railBuildStart % Consts.boardWidth, aRailBuild
								% Consts.boardWidth)) || (railBuildStart
						% Consts.boardWidth == aRailBuild % Consts.boardWidth
						&& x == railBuildStart % Consts.boardWidth
						&& y <= Math.max(railBuildStart / Consts.boardWidth,
								aRailBuild / Consts.boardWidth) && y >= Math
						.min(railBuildStart / Consts.boardWidth, aRailBuild
								/ Consts.boardWidth))))
						|| (y * Consts.boardWidth + x == railBuildStart)) {
					shapeRenderer.setColor(Consts.activeBackgroundColor);
					shapeRenderer.rect((float) (Consts.aOffsetX + x
							* Consts.fieldSize), (float) (Consts.aOffsetY + y
							* Consts.fieldSize), (float) (Consts.fieldSize),
							(float) (Consts.fieldSize));
				}

				shapeRenderer
						.setColor(G.board[y][x].capacity > Consts.capacityReloadVelo ? Consts.railColor
								: Consts.railFullColor);

				renderRails(G.board[y][x].rails, x, y);

				if (G.board[y][x].obstacle) {
					shapeRenderer.setColor(Consts.blockedBackgroundColor);
					shapeRenderer.rect((float) (Consts.aOffsetX + x
							* Consts.fieldSize), (float) (Consts.aOffsetY + y
							* Consts.fieldSize), (float) (Consts.fieldSize),
							(float) (Consts.fieldSize));
				}

				if (G.board[y][x].city != null) {
					shapeRenderer
							.setColor(G.board[y][x].rails == 0 ? Consts.cityLonelyColor
									: Consts.cityColor);
					shapeRenderer
							.rect((float) (Consts.aOffsetX + (x + 0.5 - Consts.citySize / 2)
									* Consts.fieldSize),
									(float) (Consts.aOffsetY + (y + 0.5 - Consts.citySize / 2)
											* Consts.fieldSize),
									(float) (Consts.citySize * Consts.fieldSize),
									(float) (Consts.citySize * Consts.fieldSize));
				}
			}
		}

		// Trains
		for (Train train : G.trains) {
			int aX, nX, aY, nY;
			aX = train.location % Consts.boardWidth;
			aY = train.location / Consts.boardWidth;
			nX = train.nextLocation % Consts.boardWidth;
			nY = train.nextLocation / Consts.boardWidth;

			shapeRenderer
					.setColor(train.nextLocation == train.location ? Consts.trainStuckColor
							: Consts.trainColor);

			shapeRenderer.rect((float) (Consts.aOffsetX + (aX + train.progress
					* (nX - aX) - Consts.trainSize / 2 + 0.5)
					* Consts.fieldSize), (float) (Consts.aOffsetY + (aY
					+ train.progress * (nY - aY) - Consts.trainSize / 2 + 0.5)
					* Consts.fieldSize),
					(float) (Consts.trainSize * Consts.fieldSize),
					(float) (Consts.trainSize * Consts.fieldSize));
		}

		// Cursor
		if (Consts.cursorActive) {
			shapeRenderer.setColor(Consts.cursorColor);

			shapeRenderer.circle(
					(float) (Gdx.input.getX() * Consts.cursorCorrectionX),
					(float) (Consts.screenHeight - Gdx.input.getY()
							* Consts.cursorCorrectionY),
					(float) (Consts.cursorSize));
		}

		// Menu
		if (Consts.menuActive) {
			shapeRenderer.setColor(Consts.menuBackgroundColor);
			shapeRenderer.rect(0,
					(float) (Consts.screenHeight - Consts.menuItemHeight),
					(float) (Consts.menuItemWidth * Consts.menuText.length),
					(float) (Consts.menuItemHeight));
			shapeRenderer.setColor(Consts.menuActiveBackgroundColor);
			shapeRenderer.rect((float) (Consts.menuItemWidth * aMode),
					(float) (Consts.screenHeight - Consts.menuItemHeight),
					(float) (Consts.menuItemWidth),
					(float) (Consts.menuItemHeight));
		}

		// Upgrade
		shapeRenderer
				.setColor(G.money + Consts.moneyMoreCapacity < 0 ? Consts.menuActiveBackgroundColor
						: Consts.menuBackgroundColor);

		shapeRenderer.rect(
				(float) (Consts.screenWidth - Consts.upgradeItemWidth),
				(float) (Consts.screenHeight - Consts.menuItemHeight),
				(float) (Consts.upgradeItemWidth),
				(float) (Consts.menuItemHeight));
		shapeRenderer.end();

		Assets.arial.setScale((float) (Consts.menuFontSize));
		Assets.arial.setColor(Consts.menuFontColor);

		spriteBatch.begin();

		if (Consts.menuActive) {
			for (int i = 0; i < Consts.menuText.length; i++) {
				Assets.arial.draw(spriteBatch, Consts.menuText[i],
						(float) (Consts.menuItemWidth * i + 10),
						(float) (Consts.screenHeight - 10));
			}
		}

		Assets.arial.draw(spriteBatch, "Upgrade ($ "
				+ (-(int) Consts.moneyMoreCapacity) + ")",
				(float) (Consts.screenWidth - Consts.upgradeItemWidth + 10),
				(float) (Consts.screenHeight - 10));

		Assets.arial.draw(spriteBatch, "$ " + Math.round(G.money)
				+ " (Goal: $ " + Math.round(Consts.moneyNextLevel)
				+ ") | Capacity: " + Math.round(Consts.capacityReloadVelo)
				+ "/s", 10, (float) (Consts.menuFontSize * 75));

		spriteBatch.end();

		updateInput(delta);
	}

	private void renderRails(int rails, int x, int y) {
		for (int i = 0; i < 4; i++) {
			if ((rails & (1 << i)) != 0) {
				if (i % 2 == 0) {
					for (int j = 0; j < 2; j++) {
						shapeRenderer
								.rect((float) (Consts.aOffsetX + (x + 0.5 + (j == 0 ? -Consts.railTotSize / 2
										: Consts.railTotSize / 2
												- Consts.railSize))
										* Consts.fieldSize),
										(float) (Consts.aOffsetY + (y + (i == 0 ? 0
												: 0.5 - Consts.railTotSize / 2))
												* Consts.fieldSize),
										(float) (Consts.railSize * Consts.fieldSize),
										(float) ((0.5 + Consts.railTotSize / 2) * Consts.fieldSize));
					}
				} else {
					for (int j = 0; j < 2; j++) {
						shapeRenderer
								.rect((float) (Consts.aOffsetX + (x + (i == 1 ? 0
										: 0.5 - Consts.railTotSize / 2))
										* Consts.fieldSize),
										(float) (Consts.aOffsetY + (y + 0.5 + (j == 0 ? -Consts.railTotSize / 2
												: Consts.railTotSize / 2
														- Consts.railSize))
												* Consts.fieldSize),
										(float) ((0.5 + Consts.railTotSize / 2) * Consts.fieldSize),
										(float) (Consts.railSize * Consts.fieldSize));
					}
				}
			}
		}
	}

	private void updateInput(float delta) {
		int cx, cy;
		cx = (int) (Gdx.input.getX() * Consts.cursorCorrectionX - Consts.aOffsetX);
		cy = (int) (Consts.screenHeight - Gdx.input.getY()
				* Consts.cursorCorrectionY - Consts.aOffsetY);

		if (Gdx.input.isKeyPressed(Keys.F5)) {
			init();
		}

		if (Gdx.input.isKeyPressed(Keys.F9)
				&& Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
			G.money += Math.abs(G.money / 10);
		}

		// Menu
		if (Consts.menuActive
				&& cx + Consts.aOffsetX > 0
				&& cx + Consts.aOffsetX < Consts.menuItemWidth
						* Consts.menuText.length
				&& cy + Consts.aOffsetY > Consts.screenHeight
						- Consts.menuItemHeight
				&& cy + Consts.aOffsetY < Consts.screenHeight) {
			if (Gdx.input.justTouched()) {
				aMode = (int) ((cx + Consts.aOffsetX) / Consts.menuItemWidth);
			}
			return;
		}

		if (cx + Consts.aOffsetX > Consts.screenWidth - Consts.upgradeItemWidth
				&& cx + Consts.aOffsetX < Consts.screenWidth
				&& cy + Consts.aOffsetY > Consts.screenHeight
						- Consts.menuItemHeight
				&& cy + Consts.aOffsetY < Consts.screenHeight) {
			if (Gdx.input.justTouched()) {
				G.upgradeCapacity();
			}
			return;
		}

		if (Gdx.input.getX() < Consts.scrollMargin) {
			Consts.aOffsetX += Consts.scrollVelo * delta;
			Consts.aOffsetX = Math.min(Consts.aOffsetX, Consts.boardMargin);
		}
		if (Gdx.input.getX() > Gdx.graphics.getWidth() - Consts.scrollMargin) {
			Consts.aOffsetX -= Consts.scrollVelo * delta;
			Consts.aOffsetX = Math
					.min(Math.max(Consts.aOffsetX,
							-(Consts.boardWidth * Consts.fieldSize
									- Consts.screenWidth + Consts.boardMargin)),
							Consts.boardMargin);
		}
		if (Gdx.input.getY() < Consts.scrollMargin) {
			Consts.aOffsetY -= Consts.scrollVelo * delta;
			Consts.aOffsetY = Math.min(Math.max(Consts.aOffsetY,
					-(Consts.boardHeight * Consts.fieldSize
							- Consts.screenHeight + Consts.boardMargin)),
					Consts.boardMargin);
		}
		if (Gdx.input.getY() > Gdx.graphics.getHeight() - Consts.scrollMargin) {
			Consts.aOffsetY += Consts.scrollVelo * delta;
			Consts.aOffsetY = Math.min(Consts.aOffsetY, Consts.boardMargin);
		}

		switch (aMode) {
		case 0:
			break;
		case 1:
			if (Gdx.input.justTouched()) {
				if (railBuildStart == -1) {
					railBuildStart = getActiveCursorField(cx, cy);
				} else {
					G.editRail(railBuildStart, getActiveCursorField(cx, cy),
							Gdx.input.isButtonPressed(Buttons.RIGHT));
					railBuildStart = -1;
				}
			}
			if (railBuildStart != -1) {
				aRailBuild = getActiveCursorField(cx, cy);
				if (aRailBuild / Consts.boardWidth != railBuildStart
						/ Consts.boardWidth
						&& aRailBuild % Consts.boardWidth != railBuildStart
								% Consts.boardWidth) {
					aRailBuild = -1;
				}
			}
			break;
		case 2:
			break;
		case 3:
			break;
		}
	}

	private int getActiveCursorField(int cx, int cy) {
		int x, y;
		x = (int) (cx / Consts.fieldSize);
		y = (int) (cy / Consts.fieldSize);
		if (x < 0 || x >= Consts.boardWidth || y < 0 || y >= Consts.boardHeight) {
			return -1;
		}
		return y * Consts.boardWidth + x;
	}

	@Override
	public void resize(int width, int height) {
		Consts.cursorCorrectionX = (double) Consts.screenWidth
				/ Gdx.graphics.getWidth();
		Consts.cursorCorrectionY = (double) Consts.screenHeight
				/ Gdx.graphics.getHeight();
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
