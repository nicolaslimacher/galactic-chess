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
    SpriteBatch spriteBatch;

    public MainMenuScreen(final MyChessGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        //set main menu screen text
        GlyphLayout welcomeGlyph = new GlyphLayout();
        GlyphLayout tapGlyph = new GlyphLayout();
        String welcomeText = "Welcome to Drop!!!";
        String tapText = "Tap anywhere to begin!";
        welcomeGlyph.setText(font, welcomeText);
        tapGlyph.setText(font, tapText);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, welcomeGlyph, (Gdx.graphics.getWidth() - welcomeGlyph.width)/2, 100);
        game.font.draw(game.batch, tapGlyph, (Gdx.graphics.getWidth() - tapGlyph.width)/2, 100);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
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
