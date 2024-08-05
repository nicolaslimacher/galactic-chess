package com.mygdx.game.GamePiece;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Command.CommandType;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.Utils.Constants;
import com.mygdx.game.Utils.IntPair;

public class PossibleMove extends Actor {
    private final TextureRegion textureRegion;
    public IntPair indexOnBoard;
    public GamePiece parentGamePiece;
    public GamePiece targetGamePiece;
    final GameManager gameManager;
    final CommandType type;
    final float transparency;


    public PossibleMove(GamePiece parentGamePiece, IntPair coordinates, CommandType type){
        this.parentGamePiece = parentGamePiece;
        this.type = type;
        this.gameManager = parentGamePiece.gameManager;
        if (this.type == CommandType.MOVE){
            Texture gamePieceTexture = parentGamePiece.textureRegion.getTexture();
            this.textureRegion = new TextureRegion(gamePieceTexture, (int) Constants.TILE_SIZE, (int)Constants.TILE_SIZE);
            this.transparency = 0.6f;
        }else{
            Texture texture = new Texture(Gdx.files.internal("target.png"));
            this.textureRegion = new TextureRegion(texture, (int) Constants.TILE_SIZE, (int)Constants.TILE_SIZE);
            this.transparency = 1f;
            this.targetGamePiece = gameManager.board.GetGamePieceAtCoordinate(coordinates);
        }

        this.setBounds(textureRegion.getRegionX(), textureRegion.getRegionY(),
                textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        this.setPosition(gameManager.board.GetBoardTilePosition(coordinates).x, gameManager.board.GetBoardTilePosition(coordinates).y);
        this.indexOnBoard = coordinates;
        this.setName("PossibleName" + type + coordinates.xVal + "," + coordinates.yVal);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, transparency);
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
