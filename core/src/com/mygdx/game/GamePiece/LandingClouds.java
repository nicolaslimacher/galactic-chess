package com.mygdx.game.GamePiece;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.Utils.CoordinateBoardPair;

public class LandingClouds extends Actor {
    Texture texture;
    float sidewaysMovement;
    final float animationDuration = 0.25f;

    public LandingClouds(CoordinateBoardPair coordinateBoardPair, GameManager gameManager, boolean isLeftClouds) {
        if (isLeftClouds){
            texture = new Texture(Gdx.files.internal("left_clouds.png"));
            sidewaysMovement = -5f;
        } else {
            texture = new Texture(Gdx.files.internal("right_clouds.png"));
            sidewaysMovement = 4f;
        }
        this.setWidth(64);
        this.setHeight(20);
        this.setPosition(gameManager.board.GetBoardTilePosition(coordinateBoardPair).x+2f, gameManager.board.GetBoardTilePosition(coordinateBoardPair).y);
        this.setBounds(gameManager.board.GetBoardTilePosition(coordinateBoardPair).x+2f, gameManager.board.GetBoardTilePosition(coordinateBoardPair).y, 64, 20);
        gameManager.getStage().addActor(this);
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

    public LandingClouds(CoordinateBoardPair coordinateBoardPair, GameManager gameManager){
        new LandingClouds(coordinateBoardPair, gameManager, true);
        new LandingClouds(coordinateBoardPair, gameManager, false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, 1f);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }
}
