package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyChessGame extends Game{
    public SpriteBatch batch;
    public CustomSpriteBatch customBatch;
    public BitmapFont font;
    public Board board;

    public void create() {
        batch = new SpriteBatch();
        customBatch = new CustomSpriteBatch();
        board = new Board();
        font = new BitmapFont(); // use libGDX's default Arial font
        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

}