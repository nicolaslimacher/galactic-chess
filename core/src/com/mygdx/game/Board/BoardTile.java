package com.mygdx.game.Board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.game.Utils.Constants;
import com.mygdx.game.Utils.IntPair;

public class BoardTile extends Actor {

    public final IntPair coordinates;
    private final TextureRegion boardTileTextureRegion;

    public BoardTile(float positionX, float positionY, IntPair coordinates) {
        Texture boardTileTexture = new Texture("light_internal.png");
        this.boardTileTextureRegion = new TextureRegion(boardTileTexture, (int) Constants.TILE_SIZE, (int) Constants.TILE_SIZE);
        this.coordinates = coordinates;
        setBounds(boardTileTextureRegion.getRegionX(), boardTileTextureRegion.getRegionY(),
                boardTileTextureRegion.getRegionWidth(), boardTileTextureRegion.getRegionHeight());
        this.setPosition(positionX, positionY);
        this.setX(positionX);
        this.setY(positionY);

        float pathY = MathUtils.random(1f, 4f);
        float duration = MathUtils.random(5f, 8f);
        int firstDirection = MathUtils.random(0,1);
        if (firstDirection == 0){
            this.addAction(Actions.moveBy(0f, -(pathY/2), duration, Interpolation.linear));
            this.addAction(
                    Actions.forever(
                            Actions.sequence(
                                    Actions.moveBy(0f, pathY, duration, Interpolation.linear),
                                    Actions.moveBy(0f, -pathY, duration, Interpolation.linear)
                            )
                    )
            );
        }else {
            this.addAction(Actions.moveBy(0f, -(pathY/2), duration, Interpolation.linear));
            this.addAction(
                    Actions.forever(
                            Actions.sequence(
                                    Actions.moveBy(0f, -pathY, duration, Interpolation.linear),
                                    Actions.moveBy(0f, pathY, duration, Interpolation.linear)
                            )
                    )
            );
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(boardTileTextureRegion, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    public void BounceWhenLandedOn(){
        this.addAction(
                Actions.sequence(
                        Actions.moveBy(0f, -6f, 0.5f),
                        Actions.moveBy(0f, 6f, 0.75f, Interpolation.pow4)
                )
        );
        Gdx.app.log("BoardTile", "I am " + this.getName() + " and I am bouncing.");
    }
}
