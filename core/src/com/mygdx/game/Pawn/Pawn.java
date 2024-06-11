package com.mygdx.game.Pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.Board.Board;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.Constants;
import com.mygdx.game.Utils.CoordinateBoardPair;
import com.mygdx.game.Utils.IntPair;

import org.w3c.dom.ranges.Range;

import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Actor{
    public final Board pawnBoard;
    public CoordinateBoardPair indexOnBoard;
    private final TextureRegion textureRegion;
    public Boolean isSelected;

    public Pawn(Board board, CoordinateBoardPair CoordinateBoardPair){
        Texture pawnTexture = new Texture(Gdx.files.internal("black_player.png"));
        pawnBoard = board;
        isSelected = false;
        this.textureRegion = new TextureRegion(pawnTexture, (int) Constants.TILE_SIZE, (int)Constants.TILE_SIZE);
        this.setBounds(textureRegion.getRegionX(), textureRegion.getRegionY(),
                textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        this.setPosition(board.GetBoardTilePosition(CoordinateBoardPair).x, board.GetBoardTilePosition(CoordinateBoardPair).y);
        this.indexOnBoard = CoordinateBoardPair;
        addListener(pawnInputListener);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    public void drawPossibleMoves(List<CoordinateBoardPair> possibleMoves){
        if (!possibleMoves.isEmpty()) {
            Stage stageToAdd = this.getStage();
            Group possibleMovesGroup = new Group();
            possibleMovesGroup.setName("possibleMovesGroup" + this.getName());
            stageToAdd.addActor(possibleMovesGroup);
            for (CoordinateBoardPair possibleMoveCoordinateBoardPair : possibleMoves) {
                PossiblePawnMove possiblePawnMove = new PossiblePawnMove(this, possibleMoveCoordinateBoardPair, this.pawnBoard);
                possiblePawnMove.setName("possiblePawnMove" + String.valueOf(possibleMoveCoordinateBoardPair.GetX()) + "," + String.valueOf(possibleMoveCoordinateBoardPair.GetY()));
                possibleMovesGroup.addActor(possiblePawnMove);
            }
        }
    }


    //todo: get possible moves based off a list of
    public List<CoordinateBoardPair> GetPossibleMoves(MoveSet moveSet){
        List<CoordinateBoardPair> possibleMoves = new ArrayList<CoordinateBoardPair>();
        for (IntPair possibleMove : moveSet.possibleMoves){
            CoordinateBoardPair newMove = new CoordinateBoardPair(this.indexOnBoard.x + possibleMove.xVal, this.indexOnBoard.y + possibleMove.yVal);
            if (isValidMove(possibleMove) && !IsPawnAtBoardLocation(newMove)){
                possibleMoves.add(newMove);
            }
        }
        return possibleMoves;
    }

    public boolean isValidMove(IntPair intPair) {
        boolean isValid = false;
        ValueRange boardXRange = ValueRange.of(0,this.pawnBoard.boardColumns-1);
        ValueRange boardYRange = ValueRange.of(0,this.pawnBoard.boardRows-1);
        if (boardXRange.isValidIntValue(this.indexOnBoard.x + intPair.xVal) && boardYRange.isValidIntValue(this.indexOnBoard.y + intPair.yVal)){
            isValid = true;
        }
        return isValid;
    }

    public void move(CoordinateBoardPair coordinateBoardPair) {
        this.setPosition(pawnBoard.GetBoardTilePosition(coordinateBoardPair).x, pawnBoard.GetBoardTilePosition(coordinateBoardPair).y);
        this.indexOnBoard = coordinateBoardPair;
        this.setName("Pawn"+String.valueOf(coordinateBoardPair.x)+","+String.valueOf(coordinateBoardPair.y));
    }

    //Input method overrides NOTE: GameScreen stage input listeners will set pawn isSelected to false
    private final InputListener pawnInputListener = new InputListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            Pawn actor = (Pawn) event.getListenerActor();
            actor.drawPossibleMoves(GetPossibleMoves(pawnBoard.selectedMoveSet));
            isSelected = true;
            return true;
        }
    };

    public Boolean IsPawnAtBoardLocation(CoordinateBoardPair coordinateBoardPair) {
        Boolean pawnAtLocation = false;
        for(Actor actor:this.getStage().getActors()){
            if(actor.getClass() == Pawn.class) {
                Pawn pawn = (Pawn) actor;
                if (pawn.indexOnBoard.equals(coordinateBoardPair)) {
                    return true;
                }
            }
        }
        return pawnAtLocation;
    }

    public void resetStageCoordinatesFromBoardLocation(){
        this.setPosition(pawnBoard.GetBoardTilePosition(this.indexOnBoard).x, pawnBoard.GetBoardTilePosition(this.indexOnBoard).y);
    }
}
