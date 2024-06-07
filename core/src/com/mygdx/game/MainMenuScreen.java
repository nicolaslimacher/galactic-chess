package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen{
    final MyChessGame game;
    OrthographicCamera camera;
    BitmapFont font;
    CustomSpriteBatch customBatch;

    public MainMenuScreen(final MyChessGame game) {
        this.game = game;
        customBatch = new CustomSpriteBatch();

        font = new BitmapFont(); // use libGDX's default Arial font

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        customBatch.setProjectionMatrix(camera.combined);

        //begin new sprite batch and draw welcome (need game. before methods)
        customBatch.begin();
        font.draw(customBatch, "Welcome to Chess!!! ", 240, 300);
        font.draw(customBatch, "Tap anywhere to begin!", 240, 250);
        customBatch.end();

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
