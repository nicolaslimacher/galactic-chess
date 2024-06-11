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

public class PossiblePawnMove extends Actor {
    public final Board possiblePawnMoveBoard;
    private final TextureRegion textureRegion;
    public CoordinateBoardPair indexOnBoard;
    public Pawn parentPawn;
    public PossiblePawnMove(Pawn parentPawn, CoordinateBoardPair CoordinateBoardPair, Board board){
        Texture pawnTexture = new Texture(Gdx.files.internal("white_player.png"));
        possiblePawnMoveBoard = board;
        this.parentPawn = parentPawn;
        this.textureRegion = new TextureRegion(pawnTexture, (int) Constants.TILE_SIZE, (int)Constants.TILE_SIZE);
        this.setBounds(textureRegion.getRegionX(), textureRegion.getRegionY(),
                textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        this.setPosition(board.GetBoardTilePosition(CoordinateBoardPair).x, board.GetBoardTilePosition(CoordinateBoardPair).y);
        this.indexOnBoard = CoordinateBoardPair;
        addListener(possiblePawnInputListener);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    private final InputListener possiblePawnInputListener = new InputListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            PossiblePawnMove possiblePawnMove = (PossiblePawnMove) event.getListenerActor();
            Pawn parentPawn = possiblePawnMove.parentPawn;
            parentPawn.move(possiblePawnMove.indexOnBoard);
            event.getListenerActor().getParent().remove();
            return true;
        }
    };

    public void resetStageCoordinatesFromBoardLocation(){
        this.setPosition(possiblePawnMoveBoard.GetBoardTilePosition(this.indexOnBoard).x, possiblePawnMoveBoard.GetBoardTilePosition(this.indexOnBoard).y);
    }
}
