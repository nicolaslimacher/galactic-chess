package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CustomSpriteBatch extends SpriteBatch {
    public void drawObjectOnBoard(Texture texture, Board board, float objectPositionX, float objectPositionY){
        this.draw(texture, //texture to draw
                objectPositionX, //the x-coordinate in screen space to draw
                objectPositionY, //the y-coordinate in screen space to draw
                Constants.TILE_SIZE / 2, //originX
                Constants.TILE_SIZE / 2, //originY
                Constants.TILE_SIZE, //the width in pixels to draw
                Constants.TILE_SIZE, //the height in pixels to draw
                1.0f, //scaleX
                1.0f, //scaleY
                0, //rotation
                0, //the x-coordinate in texel space
                0, //the y-coordinate in texel space
                (int) Constants.TILE_SIZE, //srcWidth - the source width in texels
                (int) Constants.TILE_SIZE, //srcHeight - the source height in texels
                false, //flipX
                false); //flipY
    }

}