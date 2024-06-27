package com.mygdx.game.GamePiece;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mygdx.game.Board.Board;
import com.mygdx.game.Command.Command;
import com.mygdx.game.Command.CommandType;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.Utils.Constants;
import com.mygdx.game.Utils.CoordinateBoardPair;

public class PossibleMove extends Actor {
    public final Board possiblePawnMoveBoard;
    private final TextureRegion textureRegion;
    public CoordinateBoardPair indexOnBoard;
    public GamePiece parentGamePiece;
    final GameManager gameManager;

    public PossibleMove(GamePiece parentGamePiece, CoordinateBoardPair CoordinateBoardPair, Board board){
        possiblePawnMoveBoard = board;
        this.parentGamePiece = parentGamePiece;
        this.gameManager = parentGamePiece.gameManager;
        Texture pawnTexture = parentGamePiece.textureRegion.getTexture();
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
        batch.setColor(color.r, color.g, color.b, 0.60f);
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    private final InputListener possiblePawnInputListener = new InputListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            System.out.println("Possible Move Listener Fired");
            PossibleMove possibleMove = (PossibleMove) event.getListenerActor();
            GamePiece parentGamePiece = possibleMove.parentGamePiece;
            //move piece by creating new move command
            parentGamePiece.gameManager.latestGamePieceCommand = new Command(parentGamePiece, possibleMove.indexOnBoard, CommandType.MOVE);
            parentGamePiece.gameManager.latestGamePieceCommand.Execute();
            event.getListenerActor().getParent().remove();//removes possible move drawings
            return true;
        }
    };
}
