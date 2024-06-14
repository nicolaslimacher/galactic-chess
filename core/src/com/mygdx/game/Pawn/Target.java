package com.mygdx.game.Pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mygdx.game.Board.Board;
import com.mygdx.game.Utils.Constants;
import com.mygdx.game.Utils.CoordinateBoardPair;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

public class Target extends Actor {
    public final Board board;
    private final TextureRegion textureRegion;
    public CoordinateBoardPair indexOnBoard;
    public Pawn parentPawn;
    public Pawn targetPawn;

    public Target(Pawn parentPawn, CoordinateBoardPair CoordinateBoardPair, Board board, Pawn targetPawn){
        Texture texture = new Texture(Gdx.files.internal("target.png"));
        this.board = board;
        this.parentPawn = parentPawn;
        this.targetPawn = targetPawn;
        this.textureRegion = new TextureRegion(texture, (int) Constants.TILE_SIZE, (int)Constants.TILE_SIZE);
        this.setBounds(textureRegion.getRegionX(), textureRegion.getRegionY(),
                textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        this.setPosition(board.GetBoardTilePosition(CoordinateBoardPair).x, board.GetBoardTilePosition(CoordinateBoardPair).y);
        this.indexOnBoard = CoordinateBoardPair;
        addListener(targetListener);
    }

    public void draw(Batch batch, float parentAlpha){
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    private final InputListener targetListener = new InputListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            System.out.println("target has been selected");
            Target target = (Target) event.getListenerActor();
            parentPawn.HitPawn(target.targetPawn);
            parentPawn.isSelected = false;
            event.getListenerActor().getParent().remove(); //disposes group with targets and possible moves drawn
            return true;
        }
    };
}
