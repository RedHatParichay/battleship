package com.mygdx.Game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.Helpers.Constants;
import com.mygdx.Players.RandomAIPlayer;

public class Battleship extends ApplicationAdapter {
	private ShapeRenderer shapeRenderer; 	// Instantiate shape renderer
	private SpriteBatch batch; // Instantiate sprite batch
	private BitmapFont font; // Instantiate bitmap font
	private OrthographicCamera camera;	// Instantiate camera
	private RandomAIPlayer aiPlayer;	// Instantiate AI player
	private int[][] playerGrid;	// Instantiate player grid
	private int[][] aiGrid;		// Instantiate AI grid
	private int[] shipSizes;	// Array to store ship size
	private boolean placingShips = true;	// Boolean to store if player has finished placing ships
	private boolean playerTurn = true;	// Initialise player as currently playing
	private boolean gameOver = false;	// Boolean to store game state

	@Override
	public void create() {
		shapeRenderer = new ShapeRenderer();	// Initialise shape renderer
		batch = new SpriteBatch();				// Initialise sprite batch
		font = new BitmapFont();				// Initialise font
		font.setColor(Color.WHITE);				// Initialise font colour
		// Initialise camera
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(false);	// Start y-axis at the bottom-left
		aiPlayer = new RandomAIPlayer();

		playerGrid = new int[Constants.GRID_SIZE][Constants.GRID_SIZE];
		aiGrid = new int[Constants.GRID_SIZE][Constants.GRID_SIZE];
		shipSizes = new int[]{2, 2, 3, 3, 4};
		aiGrid = aiPlayer.placeShipsRandomly(shipSizes);	// Place ships randomly for AI
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		drawGameBoard(); //	Draw player and AI grids
		drawGridLines(); // Draw grids
		drawText();		//	Draw instruction text

		if (!gameOver) handleInput();	// If the game is not over, keep handling input
	}

	private void drawGameBoard() {
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		for (int row = 0; row < Constants.GRID_SIZE; row++) {
			for (int col = 0; col < Constants.GRID_SIZE; col++) {

				// Player grid
				if (playerGrid[row][col] == 1) shapeRenderer.setColor(Color.BLUE);
				else if (playerGrid[row][col] == 2) shapeRenderer.setColor(Color.RED);
				else if (playerGrid[row][col] == 3) shapeRenderer.setColor(Color.GRAY);
				else shapeRenderer.setColor(Color.WHITE);
				shapeRenderer.rect(col * Constants.CELL_SIZE, row * Constants.CELL_SIZE, Constants.CELL_SIZE, Constants.CELL_SIZE);
				//shapeRenderer.rect(col * Constants.CELL_SIZE, y, Constants.CELL_SIZE, Constants.CELL_SIZE);

				// AI grid
				int aiCol = col + Constants.AI_GRID_OFFSET;
				if (aiGrid[row][col] == 2) shapeRenderer.setColor(Color.RED); // Hit AI ship
				else if (aiGrid[row][col] == 3) shapeRenderer.setColor(Color.GRAY); // Miss
				else shapeRenderer.setColor(Color.WHITE); // Hidden AI ships
				shapeRenderer.rect(aiCol * Constants.CELL_SIZE, row * Constants.CELL_SIZE, Constants.CELL_SIZE, Constants.CELL_SIZE);
			}
		}

		shapeRenderer.end();
	}

	private void drawGridLines() {
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.BLACK);

		for (int i = 0; i <= Constants.GRID_SIZE; i++) {
			int xOffset = Constants.AI_GRID_OFFSET * Constants.CELL_SIZE;

			// Horizontal lines
			int y = i * Constants.CELL_SIZE;
			shapeRenderer.line(0, y, Constants.GRID_SIZE * Constants.CELL_SIZE, y);
			shapeRenderer.line(xOffset, y, xOffset + Constants.GRID_SIZE * Constants.CELL_SIZE, y);

			// Vertical lines
			int x = i * Constants.CELL_SIZE;
			shapeRenderer.line(x, 0, x, Constants.GRID_SIZE * Constants.CELL_SIZE);
			shapeRenderer.line(xOffset + x, 0, xOffset + x, Constants.GRID_SIZE * Constants.CELL_SIZE);
		}

		shapeRenderer.end();
	}

	private void drawText() {
		batch.begin();
		font.draw(batch, "Player Grid", 50, 450);
		font.draw(batch, "Enemy Grid", 550, 450);
		// If the player is still placing ship, keep printing prompt
		if (placingShips) font.draw(batch, "Place Ships (Left-click). Press SPACE to Start!", 50, 500);
		// If the game is over, check and print who won
		else if (gameOver) font.draw(batch, (!playerTurn ? "AI WinS!" : "You Win!"), 300, 500);
		else font.draw(batch, (playerTurn ? "Your Turn" : "AI's Turn"), 300, 500);
		batch.end();
	}

	private void handleInput() {	// Method to handle mouse input
		if (Gdx.input.justTouched()) {	//
			int x = Gdx.input.getX();
			// Flip y-coordinate
			int y = Gdx.graphics.getHeight() - Gdx.input.getY();
			int col = x / Constants.CELL_SIZE;	// Convert pixel coordinates to grid coordinates
			int row = y / Constants.CELL_SIZE;	// Convert pixel coordinates to grid coordinates

			// Prevent placing ships after the game starts
			if (placingShips && col < Constants.GRID_SIZE && row < Constants.GRID_SIZE && playerGrid[row][col] == 0) {
				playerGrid[row][col] = 1;  // Player places ships
			}
			else if (!placingShips && playerTurn && col >= Constants.AI_GRID_OFFSET && col < Constants.AI_GRID_OFFSET + Constants.GRID_SIZE && row < Constants.GRID_SIZE) {
				int aiCol = col - Constants.AI_GRID_OFFSET; // Adjust for AI's grid position

				// If the position has already been hit or missed, do nothing
				if (aiGrid[row][aiCol] == 2 || aiGrid[row][aiCol] == 3) {
					System.out.println("This position has already been hit!");
					return;
				}

				// Check if the player has hit or missed a ship on the AI grid
				if (aiGrid[row][aiCol] == 1) {
					aiGrid[row][aiCol] = 2; // Hit AI ship (turn red)
				} else {
					aiGrid[row][aiCol] = 3; // Miss (turn gray)
				}

				playerTurn = false; // End player's turn
				checkGameOver();
				int[] result = aiPlayer.aiMove(playerGrid); // AI attacks back
				if (!gameOver) playerGrid[result[0]][result[1]] = result[2];
				playerTurn = true;
			}
		}

		// Space key to start the game after placing ships
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			placingShips = false; // Start game
		}
	}

	private void checkGameOver() {	// Method to check if the game is over
		boolean playerAlive = false, aiAlive = false;

		// Check if any ships are still present on the player's grid
		for (int row = 0; row < Constants.GRID_SIZE; row++) {
			for (int col = 0; col < Constants.GRID_SIZE; col++) {
				if (playerGrid[row][col] == 1) {
					playerAlive = true; // Player has at least one ship left
				}
				if (aiGrid[row][col] == 1) {
					aiAlive = true; // AI has at least one ship left
				}
			}
		}

		// If either the player or AI has no ships left, the game is over
		if (!playerAlive || !aiAlive) {
			gameOver = true;
		}
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
		batch.dispose();
		font.dispose();
	}
}
