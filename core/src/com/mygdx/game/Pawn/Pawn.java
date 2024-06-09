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
import com.mygdx.game.Constants;
import com.mygdx.game.CoordinateBoardPair;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Actor{
    public final Board pawnBoard;
    private final TextureRegion pawnTextureRegion;
    private final Texture pawnPossibleMoveTexture;
    public Object position = new Object();
    public CoordinateBoardPair indexOnBoard;
    private final InputListener pawnInputListener = new InputListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            Pawn actor = (Pawn) event.getListenerActor();
            actor.move(new CoordinateBoardPair(0, 1));
            Gdx.app.log("pawn",event.getListenerActor().toString());
            return true;
        }
    };

    public Pawn(Board board, CoordinateBoardPair CoordinateBoardPair){
        Texture pawnTexture = new Texture(Gdx.files.internal("black_player.png"));
        this.pawnPossibleMoveTexture = new Texture(Gdx.files.internal("white_player.png"));
        pawnBoard = board;
        this.pawnTextureRegion = new TextureRegion(pawnTexture, (int) Constants.TILE_SIZE, (int)Constants.TILE_SIZE);
        this.setBounds(pawnTextureRegion.getRegionX(), pawnTextureRegion.getRegionY(),
                pawnTextureRegion.getRegionWidth(), pawnTextureRegion.getRegionHeight());
        this.setPosition(board.GetBoardTilePosition(CoordinateBoardPair).x, board.GetBoardTilePosition(CoordinateBoardPair).y);
        this.indexOnBoard = CoordinateBoardPair;
        addListener(pawnInputListener);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(pawnTextureRegion, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    public void drawPossibleMoves(Board board, List<CoordinateBoardPair> possibleMoves){
        if (!possibleMoves.isEmpty()) {
            for (CoordinateBoardPair possibleMoveCoordinateBoardPair : possibleMoves) {
                Pawn pawn = new Pawn(board, possibleMoveCoordinateBoardPair);
                pawn.setName("possiblePawn" + String.valueOf(possibleMoveCoordinateBoardPair.GetX()) + "," + String.valueOf(possibleMoveCoordinateBoardPair.GetY()));
                board.addActor(pawn);
            }
        }
    }


    public List<CoordinateBoardPair> GetPossibleMoves(){
        List<CoordinateBoardPair> possibleMoves = new ArrayList<CoordinateBoardPair>();
        if (this.indexOnBoard.GetX()>0 && this.indexOnBoard.GetY()<4){
            possibleMoves.add(new CoordinateBoardPair(this.indexOnBoard.GetX() - 1, this.indexOnBoard.GetY() + 1));
        }
        if (this.indexOnBoard.GetY()<4){
            possibleMoves.add(new CoordinateBoardPair(this.indexOnBoard.GetX(), this.indexOnBoard.GetY() + 1));
        }
        if (this.indexOnBoard.GetX()<4 && this.indexOnBoard.GetY()<4){
            possibleMoves.add(new CoordinateBoardPair(this.indexOnBoard.GetX() + 1, this.indexOnBoard.GetY() + 1));
        }
        return possibleMoves;
    }

    public void move(CoordinateBoardPair CoordinateBoardPair) {
        this.setPosition(pawnBoard.GetBoardTilePosition(CoordinateBoardPair).x, pawnBoard.GetBoardTilePosition(CoordinateBoardPair).y);
        this.indexOnBoard = CoordinateBoardPair;
    }
}
