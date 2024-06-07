package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Actor {
    private final TextureRegion pawnTextureRegion;
    private final Texture pawnPossibleMoveTexture;
    public Object position = new Object();
    public CoordinatePair indexOnBoard;


    public Pawn(Board board, CoordinatePair coordinatePair){
        Texture pawnTexture = new Texture(Gdx.files.internal("black_player.png"));
        this.pawnPossibleMoveTexture = new Texture(Gdx.files.internal("white_player.png"));
        this.pawnTextureRegion = new TextureRegion(pawnTexture, (int)Constants.TILE_SIZE, (int)Constants.TILE_SIZE);
        this.setBounds(pawnTextureRegion.getRegionX(), pawnTextureRegion.getRegionY(),
                pawnTextureRegion.getRegionWidth(), pawnTextureRegion.getRegionHeight());
        this.setPosition(board.GetBoardTilePosition(coordinatePair).x, board.GetBoardTilePosition(coordinatePair).y);
        this.indexOnBoard = coordinatePair;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(pawnTextureRegion, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    public void drawPossibleMoves(Board board, List<CoordinatePair> possibleMoves){
        if (!possibleMoves.isEmpty()) {
            for (CoordinatePair possibleMoveCoordinatePair : possibleMoves) {
                Pawn pawn = new Pawn(board, possibleMoveCoordinatePair);
                pawn.setName("possiblePawn" + String.valueOf(possibleMoveCoordinatePair.GetX()) + "," + String.valueOf(possibleMoveCoordinatePair.GetY()));
                board.addActor(pawn);
            }
        }
    }

    public List<CoordinatePair> GetPossibleMoves(){
        List<CoordinatePair> possibleMoves = new ArrayList<CoordinatePair>();
        if (this.indexOnBoard.GetX()>0 && this.indexOnBoard.GetY()<4){
            possibleMoves.add(new CoordinatePair(this.indexOnBoard.GetX() - 1, this.indexOnBoard.GetY() + 1));
        }
        if (this.indexOnBoard.GetY()<4){
            possibleMoves.add(new CoordinatePair(this.indexOnBoard.GetX(), this.indexOnBoard.GetY() + 1));
        }
        if (this.indexOnBoard.GetX()<4 && this.indexOnBoard.GetY()<4){
            possibleMoves.add(new CoordinatePair(this.indexOnBoard.GetX() + 1, this.indexOnBoard.GetY() + 1));
        }
        return possibleMoves;
    }

    public void move(CoordinatePair coordinatePair, Board board) {
        this.setPosition(board.GetBoardTilePosition(coordinatePair).x, board.GetBoardTilePosition(coordinatePair).y);
        this.indexOnBoard = coordinatePair;
    }
}
