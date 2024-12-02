package com.mygdx.game.GamePiece;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.mygdx.game.Manager.BattleManager;
import com.mygdx.game.Utils.IntPair;

public class LandingClouds extends Actor {
    TextureRegion textureRegion;
    float sidewaysMovement;
    final float animationDuration = 0.25f;

    public LandingClouds(IntPair coordinates, BattleManager battleManager, boolean isLeftClouds) {
        if (isLeftClouds){
            textureRegion = battleManager.GetAssetManager().get("texturePacks/battleTextures.atlas", TextureAtlas.class).findRegion("left_clouds");
            sidewaysMovement = -5f;
        } else {
            textureRegion = battleManager.GetAssetManager().get("texturePacks/battleTextures.atlas", TextureAtlas.class).findRegion("right_clouds");
            sidewaysMovement = 4f;
        }
        this.setWidth(64);
        this.setHeight(20);
        this.setPosition(battleManager.board.GetBoardTilePosition(coordinates).x+2f, battleManager.board.GetBoardTilePosition(coordinates).y);
        this.setBounds(battleManager.board.GetBoardTilePosition(coordinates).x+2f, battleManager.board.GetBoardTilePosition(coordinates).y, 64, 20);
        battleManager.getStage().addActor(this);
        this.toFront();

        //fadeout and movement
        ParallelAction animation = new ParallelAction(Actions.fadeOut(animationDuration, Interpolation.exp10), Actions.moveBy(sidewaysMovement, 1.5f, animationDuration));

        //remove self after moving
        RunnableAction removeLandingClouds = new RunnableAction();
        removeLandingClouds.setRunnable(LandingClouds.this::remove);

        //full action
        SequenceAction fadeInAndRemove = new SequenceAction(animation, removeLandingClouds);
        this.addAction(fadeInAndRemove);
    }

    public LandingClouds(IntPair coordinates, BattleManager battleManager){
        new LandingClouds(coordinates, battleManager, true);
        new LandingClouds(coordinates, battleManager, false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, 1f);
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
