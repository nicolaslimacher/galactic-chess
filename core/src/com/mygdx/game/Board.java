package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Board extends Group {

    public int[][] boardGrid; // boardGrid[r][c] is the contents of row r, column c.
    public Table boardTable;

    //constructor for Board
    public Board(int rows, int columns) {
        boardGrid = new int[rows][columns];
        this.setSize(Gdx.graphics.getWidth()*0.8f, Gdx.graphics.getHeight()*0.8f);
        for (int row = 0; row < boardGrid.length; ++row) {
            for (int column = 0; column < boardGrid[row].length; ++column) {
                float tilePositionX = GetTileXOrYPosition(column, columns, Gdx.graphics.getWidth(), Constants.TILE_SIZE);
                float tilePositionY = GetTileXOrYPosition(row, rows, Gdx.graphics.getHeight(), Constants.TILE_SIZE);
                BoardTile boardTile = new BoardTile(tilePositionX, tilePositionY, new CoordinatePair(row, column));
                boardTile.setName("tile" + Integer.toString(boardTile.coordinatePair.GetX())+","+Integer.toString(boardTile.coordinatePair.GetY()));
                this.addActor(boardTile);
            }
        }
    }//end constructor

    private float GetTileXOrYPosition (int columnOrRowIndex, int columnOrRowTotal, float screenWidthOrHeight, float tileWidth){
        float columnWidth = screenWidthOrHeight / (float)columnOrRowTotal;
        return (columnWidth * columnOrRowIndex) + (columnWidth / 2f) - (tileWidth /2f);
    }
//
//    public void render(CustomSpriteBatch customBatch, int[][] boardGrid) {
//        drawBoard(customBatch, boardGrid);
//    }
//
//    //draw board given boardGrid[rows][columns]
//    private void drawBoard(CustomSpriteBatch customBatch, int[][] boardGrid){
//        for (int row = 0; row < boardGrid.length; ++row) {
//            for (int column = 0; column < boardGrid[row].length; ++column) {
//                customBatch.draw(lightInternal,
//                        getTilePositionX(row, boardGrid.length),
//                        getTilePositionY(column, boardGrid[row].length));
//            }
//        }
//    }//end drawBoard
//
//    //get starting point for drawing tiles horizontally. starts left side of screen
//    private float getOriginPointX(double rowLength){
//        float row = (float)rowLength;
//        return (Constants.SCREEN_WIDTH-Constants.TILE_SIZE)/2 - 0.5f*row*Constants.TILE_SIZE + (0.5f*row - 0.5f)*Constants.TILE_BUFFER;
//    }//end getOriginPointX
//
//    //get starting point for drawing tiles horizontally. starts top side of screen
//    private float getOriginPointY(double ColumnLength){
//        float column = (float)ColumnLength;
//        return (Constants.SCREEN_HEIGHT-Constants.TILE_SIZE)/2 - 0.5f*column*Constants.TILE_SIZE + (0.5f*column - 0.5f)*Constants.TILE_BUFFER;
//    }//end getOriginPointY
//
//    // takes index and length of row to return position of that tile texture
//    public float getTilePositionX(int index, double rowLength) {
//        float indexF = (float)index;
//        if (index == 0) {
//            return getOriginPointX(rowLength);
//        }
//        return getOriginPointX(rowLength) + indexF*(Constants.TILE_SIZE + Constants.TILE_BUFFER);
//    }//end getTilePositionX
//
//    // takes index and length of column to return position of that tile texture
//    public float getTilePositionY(int index, double columnLength) {
//        float indexF = (float)index;
//        if (index == 0) {
//            return getOriginPointY(columnLength);
//        }
//        return getOriginPointY(columnLength) + indexF*(Constants.TILE_SIZE + Constants.TILE_BUFFER);
//    }//end getTilePositionY
}//end Board class
