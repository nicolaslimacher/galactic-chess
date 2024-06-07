package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Actor {
    final private Texture pawnTexture;
    final private TextureRegion pawnTextureRegion;
    // private Texture pawnPossibleMoveTexture;
    public Object position = new Object();
    public float boardPositionX, boardPositionY;
    public int positionX, positionY;


    public Pawn(int positionX, int positionY){
        this.pawnTexture = new Texture(Gdx.files.internal("black_player.png"));
        this.pawnTextureRegion = new TextureRegion(this.pawnTexture, (int)Constants.TILE_SIZE, (int)Constants.TILE_SIZE);
//        this.pawnPossibleMoveTexture = new Texture(Gdx.files.internal("white_player.png"));
        this.setPosition(positionX, positionY);
        setBounds(pawnTextureRegion.getRegionX(), pawnTextureRegion.getRegionY(),
                pawnTextureRegion.getRegionWidth(), pawnTextureRegion.getRegionHeight());
    }
//    public void render(CustomSpriteBatch customBatch, Board board) {
//        drawPawnOnBoard(customBatch, board);
//    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(pawnTextureRegion, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

//    public void drawPawnOnBoard(CustomSpriteBatch customBatch, Board board){
//        customBatch.drawObjectOnBoard(
//                this.pawnTexture, //texture
//                board, //board
//                board.getTilePositionX(this.positionX, board.boardGrid.length), //xposition
//                board.getTilePositionY(this.positionY, board.boardGrid[positionY].length)); //yposition
//    }

//    public void drawPossibleMoves(CustomSpriteBatch customBatch, Board board, List<CoordinatePair> possibleMoves){
//        if (!possibleMoves.isEmpty()) {
//            for (CoordinatePair possibleMoveCoordinatePair : possibleMoves) {
//                customBatch.drawObjectOnBoard(
//                        this.pawnPossibleMoveTexture, //possible move texture,
//                        board, //board
//                        possibleMoveCoordinatePair.GetX(), //xpos
//                        possibleMoveCoordinatePair.GetY()); //ypos
//            }
//        }
//    }

//    public List<CoordinatePair> GetPossibleMoves(){
//        List<CoordinatePair> possibleMoves = new ArrayList<CoordinatePair>();
//        if (this.positionX>0 && this.positionY<4){
//            possibleMoves.add(new CoordinatePair(this.positionX - 1, this.positionY + 1));
//        }
//        if (this.positionY<4){
//            possibleMoves.add(new CoordinatePair(this.positionX, this.positionY + 1));
//        }
//        if (this.positionX<4 && this.positionY<4){
//            possibleMoves.add(new CoordinatePair(this.positionX + 1, this.positionY + 1));
//        }
//        return possibleMoves;
//    }

    public void move(int newPositionX, int newPositionY) {
        this.positionX = newPositionX;
        this.positionY = newPositionY;
    }
}
