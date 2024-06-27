package com.mygdx.game.EnemyAI;

import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.CoordinateBoardPair;
import com.mygdx.game.Utils.IntPair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class EnemyAI {
    final GameManager gameManager;

    public EnemyAI(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public void MakeMove(){
        //TODO: add targets to possible moves. could update IsPawnAtLocation to only have "friendly"
        HashMap<GamePiece, CoordinateBoardPair> allPossibleMoves = GetAllPossibleMoves();
        int rnd = getRandomNumber(0, allPossibleMoves.size());
        List<GamePiece> keysAsArray = new ArrayList<GamePiece>(allPossibleMoves.keySet());
        GamePiece gamePieceToMove = keysAsArray.get(rnd);
        gamePieceToMove.Move(allPossibleMoves.get(gamePieceToMove));
    }

    private HashMap<GamePiece, CoordinateBoardPair> GetAllPossibleMoves(){
        HashMap<GamePiece, CoordinateBoardPair> allPossibleMoves = new HashMap<GamePiece, CoordinateBoardPair>();
        for (GamePiece gamePieceToMove :  gameManager.enemyGamePieces) {
            for (MoveSet moveSet : gameManager.availableMoveSets) {
                for (IntPair possibleMove : moveSet.possibleMoves){
                    CoordinateBoardPair newMove = new CoordinateBoardPair(gamePieceToMove.indexOnBoard.x + (-1 * possibleMove.xVal), gamePieceToMove.indexOnBoard.y + (-1 *  possibleMove.yVal));
                    if (gamePieceToMove.isValidEnemyMove(possibleMove) && !gameManager.IsPawnAtBoardLocation(newMove)){
                        allPossibleMoves.put(gamePieceToMove, newMove);
                    }
                }
            }
        }
        return allPossibleMoves;
    }
}
