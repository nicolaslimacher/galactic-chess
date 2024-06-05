package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CustomSpriteBatch extends SpriteBatch {
    public void draw(Texture texture, float x, float y, boolean flipX, boolean flipY) {
        this.draw(texture, x, y,
                Constants.TILE_SIZE / 2, Constants.TILE_SIZE / 2,
                Constants.TILE_SIZE, Constants.TILE_SIZE,
                1.0f, 1.0f,
                0.0f,
                0, 0,
                (int) Constants.TILE_SIZE, (int) Constants.TILE_SIZE,
                flipX, flipY);

    }

    public void draw(Texture texture, float x, float y, float rotation) {
        this.draw(texture, x, y,
                Constants.TILE_SIZE / 2, Constants.TILE_SIZE / 2,
                Constants.TILE_SIZE, Constants.TILE_SIZE,
                1.0f, 1.0f,
                rotation,
                0, 0,
                (int) Constants.TILE_SIZE, (int) Constants.TILE_SIZE,
                false, false);
    }
}