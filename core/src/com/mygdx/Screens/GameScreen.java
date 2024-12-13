package com.mygdx.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.Game.Battleship;
import com.mygdx.GameInputProcessor;

import javax.swing.text.View;

public class GameScreen implements Screen {

    private final Battleship game;
    private final OrthographicCamera gamecam;
    private final Viewport gamePort;
    // Grid and assets
    private static final int GRID_SIZE = 8;
    private static final int CELL_SIZE = 80; // Size of each grid cell
    private final Rectangle[][] grid; // To track the game grid for interaction
    private final Texture cellTexture; // Texture for grid cells
    private final BitmapFont font; // Font for text rendering
    private ShapeRenderer shapeRenderer;
    private GameInputProcessor inputProcessor;

    public GameScreen(Battleship game) {
        this.game = game;
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(1280, 720, gamecam);

        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);  // Centering the viewport
        gamecam.update();

        grid = new Rectangle[GRID_SIZE][GRID_SIZE];
        cellTexture = new Texture("cell.png"); // Placeholder texture
        font = new BitmapFont(); // Default font
        shapeRenderer = new ShapeRenderer();
        inputProcessor = new GameInputProcessor(gamecam, grid);

        Gdx.input.setInputProcessor(inputProcessor);

        // Initialize the grid
        initializeGrid();
    }

    private void initializeGrid() {
        int startX = 0; // Start grid at the bottom-left corner
        int startY = 0;

        //float startX = (gamePort.getWorldWidth() - (GRID_SIZE * CELL_SIZE)) / 2; // Center horizontally
        //float startY = (gamePort.getWorldHeight() - (GRID_SIZE * CELL_SIZE)) / 2; // Center vertically

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                grid[row][col] = new Rectangle(
                        startX + col * CELL_SIZE, // X position
                        startY + row * CELL_SIZE, // Y position
                        CELL_SIZE,               // Width
                        CELL_SIZE                // Height
                );
            }
        }
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();

        // Draw the grid
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Rectangle cell = grid[row][col];
                game.batch.draw(cellTexture, cell.x, cell.y, CELL_SIZE, CELL_SIZE);

                // Draw coordinates for debugging
                font.setColor(Color.WHITE);
                font.draw(game.batch, row + "," + col, cell.x + 10, cell.y + 20);
            }
        }

        game.batch.end();

        // Draw grid lines
        shapeRenderer.setProjectionMatrix(gamecam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE); // Line color

        // Draw horizontal lines
        for (int row = 0; row <= GRID_SIZE; row++) {
            float y = grid[0][0].y + row * CELL_SIZE;
            shapeRenderer.line(grid[0][0].x, y, grid[0][GRID_SIZE - 1].x + CELL_SIZE, y);
        }

        // Draw vertical lines
        for (int col = 0; col <= GRID_SIZE; col++) {
            float x = grid[0][0].x + col * CELL_SIZE;
            shapeRenderer.line(x, grid[0][0].y, x, grid[GRID_SIZE - 1][0].y + CELL_SIZE);
        }

        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        gamecam.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        cellTexture.dispose();
        font.dispose();
    }
}
