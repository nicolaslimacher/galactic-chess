package com.mygdx.game.Board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.Utils.Constants;
import com.mygdx.game.Utils.CoordinateBoardPair;

public class Board extends Group {

    public int[][] boardGrid; // boardGrid[r][c] is the contents of row r, column c.
    public float screenWidth, screenHeight;
    public int boardRows, boardColumns;
    //menus

    //constructor for Board
    public Board(int rows, int columns) {
        boardGrid = new int[rows][columns];
        this.boardRows = rows;
        this.boardColumns = columns;
        this.setSize(screenWidth*0.8f, screenHeight*0.8f);
        sizeAndAddBoardTiles();
    }//end constructor

    //using right 4/5s of screen to leave room for menus
    private float GetTileXPosition (int columnIndex, int columnTotal){
        float columnWidth = (Constants.SCREEN_WIDTH*Constants.SCREEN_BOARD_WIDTH_RATIO) / (float)columnTotal;
        return Constants.SCREEN_BOARD_WIDTH_LEFT_OFFSET + ((1-Constants.SCREEN_BOARD_WIDTH_RATIO)/2*Constants.SCREEN_WIDTH) + (columnWidth * columnIndex) + (columnWidth / 2f) - (Constants.TILE_SIZE /2f);
    }
    private float GetTileYPosition (int rowIndex, int rowTotal){
        float rowHeight = (Constants.SCREEN_HEIGHT*Constants.SCREEN_BOARD_HEIGHT_RATIO) / (float)rowTotal;
        return ((1-Constants.SCREEN_BOARD_HEIGHT_RATIO)/2*Constants.SCREEN_HEIGHT) + (rowHeight * rowIndex) + (rowHeight / 2f) - (Constants.TILE_SIZE /2f);
    }

    public Vector2 GetBoardTilePosition (CoordinateBoardPair CoordinateBoardPair){
        //get tile
        Vector2 tileCoordinates = new Vector2();
        tileCoordinates.x = GetTileXPosition(CoordinateBoardPair.x, boardColumns);
        tileCoordinates.y = GetTileYPosition(CoordinateBoardPair.y, boardRows);
        return tileCoordinates;
    }

    public void sizeAndAddBoardTiles(){
        this.clearChildren(); //removes already drawn board tiles
        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();
        for (int row = 0; row < boardGrid.length; ++row) {
            for (int column = 0; column < boardGrid[row].length; ++column) {
                float tilePositionX = GetTileXPosition(column, boardColumns);
                float tilePositionY = GetTileYPosition(row, boardRows);
                BoardTile boardTile = new BoardTile(tilePositionX, tilePositionY, new CoordinateBoardPair(row, column));
                boardTile.setName("tile" + boardTile.CoordinateBoardPair.GetX() +","+ boardTile.CoordinateBoardPair.GetY());
                this.addActor(boardTile);
            }
        }
    }

    public GamePiece GetPawnAtCoordinate(CoordinateBoardPair coordinateBoardPair){
        GamePiece gamePieceAtCoordinate = null;
        for(Actor actor:this.getStage().getActors()){
            if(actor.getClass() == GamePiece.class) {
                GamePiece gamePiece = (GamePiece) actor;
                if (gamePiece.indexOnBoard.equals(coordinateBoardPair)) {
                    gamePieceAtCoordinate = gamePiece;
                }
            }
        }
        return gamePieceAtCoordinate;
    }
}//end Board class
