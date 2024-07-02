package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.MyChessGame;

public class MainMenuScreen implements Screen{
    final MyChessGame game;
    OrthographicCamera camera;
    BitmapFont font;
    SpriteBatch batch;
    Texture loadingScreen;

    public MainMenuScreen(final MyChessGame game) {
        this.game = game;
        batch = new SpriteBatch();

        font = new BitmapFont(); // use libGDX's default Arial font

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        loadingScreen = new Texture(Gdx.files.internal("loading_screen.png"));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        //begin new sprite batch and draw welcome (need game. before methods)
        batch.begin();
        batch.draw(loadingScreen, 0,0);
        batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game, game.stage));
            dispose();
        }
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
