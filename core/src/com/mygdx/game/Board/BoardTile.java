package com.mygdx.game.Board;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Constants;
import com.mygdx.game.CoordinateBoardPair;

public class BoardTile extends Actor {

    public final CoordinateBoardPair CoordinateBoardPair;
    private final TextureRegion boardTileTextureRegion;
    Vector2 localPos= new Vector2();

    public BoardTile(float positionX, float positionY, CoordinateBoardPair CoordinateBoardPair) {
        Texture boardTileTexture = new Texture("light_internal.png");
        this.boardTileTextureRegion = new TextureRegion(boardTileTexture, (int) Constants.TILE_SIZE, (int) Constants.TILE_SIZE);
        this.CoordinateBoardPair = CoordinateBoardPair;
        setBounds(boardTileTextureRegion.getRegionX(), boardTileTextureRegion.getRegionY(),
                boardTileTextureRegion.getRegionWidth(), boardTileTextureRegion.getRegionHeight());
        this.setPosition(positionX, positionY);
        this.setX(positionX);
        this.setY(positionY);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(boardTileTextureRegion, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
