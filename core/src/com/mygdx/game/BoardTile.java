package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BoardTile extends Actor {

    public final CoordinatePair coordinatePair;
    private TextureRegion boardTileTextureRegion;

    public BoardTile(float positionX, float positionY, CoordinatePair coordinatePair) {
        Texture boardTileTexture = new Texture("light_internal.png");
        this.boardTileTextureRegion = new TextureRegion(boardTileTexture, (int) Constants.TILE_SIZE, (int) Constants.TILE_SIZE);
        this.coordinatePair = coordinatePair;
        this.setPosition(positionX, positionY);
        setBounds(boardTileTextureRegion.getRegionX(), boardTileTextureRegion.getRegionY(),
                boardTileTextureRegion.getRegionWidth(), boardTileTextureRegion.getRegionHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(boardTileTextureRegion, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
