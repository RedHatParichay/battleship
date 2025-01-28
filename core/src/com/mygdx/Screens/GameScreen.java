package com.mygdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.Objects.Board;
import com.mygdx.Game.Battleship;
import com.mygdx.Players.AIPlayer;
import com.mygdx.Players.Player;

public class GameScreen implements Screen {

    private Battleship game;
    private Stage stage;
    private Skin skin;
    private Board board;
    private AIPlayer aiPlayer;
    private Player player;

    public GameScreen(Battleship game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.board = new Board();
        this.aiPlayer = new AIPlayer(board);
        this.player = new Player(board);
        setupUI();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

    }

    @Override
    public void resize(int width, int height) {

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

    }
}
