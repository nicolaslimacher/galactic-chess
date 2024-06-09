package com.mygdx.game.Board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.mygdx.game.Constants;
import com.mygdx.game.CoordinateBoardPair;

public class Board extends Group {

    public int[][] boardGrid; // boardGrid[r][c] is the contents of row r, column c.
    public float screenWidth, screenHeight;
    public int boardRows, boardColumns;

    //constructor for Board
    public Board(int rows, int columns) {
        boardGrid = new int[rows][columns];
        this.boardRows = rows;
        this.boardColumns = columns;
        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();
        this.setSize(screenWidth*0.8f, screenHeight*0.8f);
        for (int row = 0; row < boardGrid.length; ++row) {
            for (int column = 0; column < boardGrid[row].length; ++column) {
                float tilePositionX = GetTileXOrYPosition(column, columns, screenWidth, Constants.TILE_SIZE);
                float tilePositionY = GetTileXOrYPosition(row, rows, screenHeight, Constants.TILE_SIZE);
                BoardTile boardTile = new BoardTile(tilePositionX, tilePositionY, new CoordinateBoardPair(row, column));
                boardTile.setName("tile" + Integer.toString(boardTile.CoordinateBoardPair.GetX())+","+Integer.toString(boardTile.CoordinateBoardPair.GetY()));
                //System.out.println(boardTile.getName()+ " y coord should be: " + Float.toString(tilePositionY));
                //System.out.println(boardTile.getName()+ " y coord: " + Float.toString(boardTile.getY()));
                this.addActor(boardTile);
            }
        }
    }//end constructor

    private float GetTileXOrYPosition (int columnOrRowIndex, int columnOrRowTotal, float screenWidthOrHeight, float tileWidth){
        float columnWidth = screenWidthOrHeight / (float)columnOrRowTotal;
        return (columnWidth * columnOrRowIndex) + (columnWidth / 2f) - (tileWidth /2f);
    }

    public Vector2 GetBoardTilePosition (CoordinateBoardPair CoordinateBoardPair){
        //get tile
        Vector2 tileCoordinates = new Vector2();
        tileCoordinates.x = GetTileXOrYPosition(CoordinateBoardPair.x, boardColumns, screenWidth, Constants.TILE_SIZE);
        tileCoordinates.y = GetTileXOrYPosition(CoordinateBoardPair.y, boardRows, screenHeight, Constants.TILE_SIZE);
        return tileCoordinates;
    }
}//end Board class
