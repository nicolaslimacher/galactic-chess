package com.mygdx.game.GamePiece;

import com.badlogic.gdx.Gdx;
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
import com.mygdx.game.Utils.Constants;
import com.mygdx.game.Utils.CoordinateBoardPair;

public class Target extends Actor {
    public final Board board;
    private final TextureRegion textureRegion;
    public CoordinateBoardPair indexOnBoard;
    public GamePiece parentGamePiece;
    public GamePiece targetGamePiece;

    public Target(GamePiece parentGamePiece, CoordinateBoardPair CoordinateBoardPair, Board board, GamePiece targetGamePiece){
        Texture texture = new Texture(Gdx.files.internal("target.png"));
        this.board = board;
        this.parentGamePiece = parentGamePiece;
        this.targetGamePiece = targetGamePiece;
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
            Target target = (Target) event.getListenerActor();
            //parentGamePiece.HitPawn(target.targetGamePiece);
            //TODO: targetgamepiece doesnt work - find gamepiece at that location?
            parentGamePiece.gameManager.latestGamePieceCommand = new Command(parentGamePiece, target.indexOnBoard, CommandType.HIT);
            parentGamePiece.gameManager.latestGamePieceCommand.Execute();
            event.getListenerActor().getParent().remove(); //disposes group with targets and possible moves drawn
            return true;
        }
    };
}
