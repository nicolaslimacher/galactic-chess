package com.mygdx.game.EnemyAI;

import com.mygdx.game.Command.CommandType;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.GameManager.Team;
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
        List<EnemyAIMove> allPossibleMoves = GetAllPossibleMoves();
        int rnd = getRandomNumber(0, allPossibleMoves.size());
        EnemyAIMove enemyAIMove = allPossibleMoves.get(rnd);
        if(enemyAIMove.commandType == CommandType.MOVE) {
            enemyAIMove.gamePiece.Move(enemyAIMove.coordinateBoardPair);
        }else{
            if (enemyAIMove.gamePiece.HitPawn(gameManager.GetPawnAtCoordinate(enemyAIMove.coordinateBoardPair))) {
                enemyAIMove.gamePiece.Move(enemyAIMove.coordinateBoardPair);
            }
        }
    }

    private List<EnemyAIMove> GetAllPossibleMoves(){
        List<EnemyAIMove> allPossibleMoves = new ArrayList<>();
        for (GamePiece gamePieceToMove :  gameManager.enemyGamePieces) {
            for (MoveSet moveSet : gameManager.availableMoveSets) {
                for (IntPair possibleMove : moveSet.possibleMoves){
                    CoordinateBoardPair newMove = new CoordinateBoardPair(gamePieceToMove.indexOnBoard.x + (-1 * possibleMove.xVal), gamePieceToMove.indexOnBoard.y + (-1 *  possibleMove.yVal));
                    if (gamePieceToMove.isValidEnemyMove(possibleMove)){
                        if (gameManager.IsPawnAtBoardLocation(newMove) && gameManager.GetPawnAtCoordinate(newMove).team == Team.FRIENDLY){
                            allPossibleMoves.add(new EnemyAIMove(CommandType.HIT, newMove, gamePieceToMove));
                        }else if (!gameManager.IsPawnAtBoardLocation(newMove)){
                            allPossibleMoves.add(new EnemyAIMove(CommandType.MOVE, newMove, gamePieceToMove));
                        }
                    }
                }
            }
        }
        return allPossibleMoves;
    }
}
