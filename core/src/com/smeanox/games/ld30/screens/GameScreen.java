package com.smeanox.games.ld30.screens;

import com.badlogic.gdx.Gdx;
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

	int aMode; // 0 = Watch, 1 = Build Rails, 2 = Configure Trains, 3 =
				// Configure Train

	public GameScreen() {
		G.init();

		// DEB

		Train tmpTrain;
		tmpTrain = new Train();
		tmpTrain.location = 16;
		tmpTrain.nextLocation = 16;
		tmpTrain.route.add(16);
		tmpTrain.route.add(4);
		G.trains.add(tmpTrain);
		tmpTrain = new Train();
		tmpTrain.location = 16;
		tmpTrain.nextLocation = 16;
		tmpTrain.route.add(24);
		tmpTrain.route.add(22);
		tmpTrain.route.add(4);
		tmpTrain.route.add(18);
		G.trains.add(tmpTrain);

		shapeRenderer = new ShapeRenderer();

		spriteBatch = new SpriteBatch();

		aMode = 0;

		railBuildStart = -1;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(Consts.backgroundColor.r, Consts.backgroundColor.g,
				Consts.backgroundColor.b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		G.update(delta);

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
				if (y * Consts.boardWidth + x == railBuildStart) {
					shapeRenderer.setColor(Consts.activeBackgroundColor);
					shapeRenderer.rect((float) (Consts.aOffsetX + x
							* Consts.fieldSize), (float) (Consts.aOffsetY + y
							* Consts.fieldSize), (float) (Consts.fieldSize),
							(float) (Consts.fieldSize));
				}

				shapeRenderer.setColor(Consts.railColor);

				renderRails(G.board[y][x].rails, x, y);

				if (G.board[y][x].city != null) {
					shapeRenderer.setColor(Consts.cityColor);
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
		shapeRenderer.setColor(Consts.trainColor);
		for (Train train : G.trains) {
			int aX, nX, aY, nY;
			aX = train.location % Consts.boardWidth;
			aY = train.location / Consts.boardWidth;
			nX = train.nextLocation % Consts.boardWidth;
			nY = train.nextLocation / Consts.boardWidth;

			shapeRenderer.rect((float) (Consts.aOffsetX + (aX + train.progress
					* (nX - aX) - Consts.trainSize / 2 + 0.5)
					* Consts.fieldSize), (float) (Consts.aOffsetY + (aY
					+ train.progress * (nY - aY) - Consts.trainSize / 2 + 0.5)
					* Consts.fieldSize),
					(float) (Consts.trainSize * Consts.fieldSize),
					(float) (Consts.trainSize * Consts.fieldSize));
		}

		// Cursor
		shapeRenderer.setColor(Consts.cursorColor);

		shapeRenderer.circle(
				(float) (Gdx.input.getX() * Consts.cursorCorrectionX),
				(float) (Consts.screenHeight - Gdx.input.getY()
						* Consts.cursorCorrectionY),
				(float) (Consts.cursorSize));

		// Menu
		shapeRenderer.setColor(Consts.menuBackgroundColor);
		shapeRenderer.rect(0,
				(float) (Consts.screenHeight - Consts.menuItemHeight),
				(float) (Consts.menuItemWidth * Consts.menuText.length),
				(float) (Consts.menuItemHeight));
		shapeRenderer.setColor(Consts.menuActiveBackgroundColor);
		shapeRenderer
				.rect((float) (Consts.menuItemWidth * aMode),
						(float) (Consts.screenHeight - Consts.menuItemHeight),
						(float) (Consts.menuItemWidth),
						(float) (Consts.menuItemHeight));

		shapeRenderer.end();

		Assets.arial.setScale((float) (Consts.menuFontSize));
		Assets.arial.setColor(Consts.menuFontColor);

		spriteBatch.begin();

		for (int i = 0; i < Consts.menuText.length; i++) {
			Assets.arial.draw(spriteBatch, Consts.menuText[i],
					(float) (Consts.menuItemWidth * i + 10),
					(float) (Consts.screenHeight - 10));
		}

		spriteBatch.end();

		updateInput();
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

	int railBuildStart;

	private void updateInput() {
		int cx, cy;
		cx = (int) (Gdx.input.getX() * Consts.cursorCorrectionX + Consts.aOffsetX);
		cy = (int) (Consts.screenHeight - Gdx.input.getY()
				* Consts.cursorCorrectionY + Consts.aOffsetY);

		if (cx > 0 && cx < Consts.menuItemWidth * Consts.menuText.length
				&& cy > Consts.screenHeight - Consts.menuItemHeight
				&& cy < Consts.screenHeight) {
			if (Gdx.input.justTouched()) {
				aMode = (int) (cx / Consts.menuItemWidth);
			}
			return;
		}

		switch (aMode) {
		case 0:
			break;
		case 1:
			if (Gdx.input.justTouched()) {
				if (railBuildStart == -1) {
					railBuildStart = getActiveCursorField();
				} else {
					G.addRail(railBuildStart, getActiveCursorField());
					railBuildStart = -1;
				}
			}
			break;
		case 2:
			break;
		case 3:
			break;
		}
	}

	private int getActiveCursorField() {
		int x, y;
		x = (int) (Gdx.input.getX() * Consts.cursorCorrectionX + Consts.aOffsetX);
		y = (int) (Consts.screenHeight - Gdx.input.getY()
				* Consts.cursorCorrectionY + Consts.aOffsetY);
		x /= Consts.fieldSize;
		y /= Consts.fieldSize;
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
