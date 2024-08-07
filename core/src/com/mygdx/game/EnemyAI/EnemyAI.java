package com.mygdx.game.EnemyAI;

import com.mygdx.game.Command.CommandType;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.GameManager.Team;
import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.IntPair;

import java.util.ArrayList;
import java.util.List;

public class EnemyAI {
    final GameManager gameManager;
    final float enemyAIDelay = 0.75f;

    public EnemyAI(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public MoveSet MakeMove(){
        List<EnemyAIMove> allPossibleMoves = GetAllPossibleMoves();
        if (allPossibleMoves.isEmpty()){
            System.out.println("Enemy AI: no valid moves");
            //TODO: add random damage, show popup
            return null;
        }
        int rnd = getRandomNumber(0, allPossibleMoves.size()-1);
        EnemyAIMove enemyAIMove = allPossibleMoves.get(rnd);
        if(enemyAIMove.commandType == CommandType.MOVE) {
            enemyAIMove.gamePiece.JetpackJump(enemyAIMove.coordinates, enemyAIDelay);
            System.out.println("Enemy executed move: " + enemyAIMove.moveSet.getName());
        }else{
            if (enemyAIMove.gamePiece.HitGamePiece(gameManager.GetGamePieceAtCoordinate(enemyAIMove.coordinates))) {
                enemyAIMove.gamePiece.JetpackJump(enemyAIMove.coordinates, enemyAIDelay);
            }
            System.out.println("Enemy executed move: " + enemyAIMove.moveSet.getName());
        }
        return enemyAIMove.moveSet;
    }

    private List<EnemyAIMove> GetAllPossibleMoves(){
        List<EnemyAIMove> allPossibleMoves = new ArrayList<>();
        for (GamePiece gamePieceToMove :  gameManager.enemyGamePieces) {
            if (gamePieceToMove.isAlive) {
                for (MoveSet moveSet : gameManager.enemyMoves) {
                    for (IntPair possibleMove : moveSet.possibleMoves) {
                        IntPair newMove = new IntPair(gamePieceToMove.indexOnBoard.xVal + (-1 * possibleMove.xVal), gamePieceToMove.indexOnBoard.yVal + (-1 * possibleMove.yVal));
                        if (gamePieceToMove.isValidEnemyMove(possibleMove)) {
                            if (gameManager.IsGamePieceAtBoardLocation(newMove) && gameManager.GetGamePieceAtCoordinate(newMove).team == Team.FRIENDLY) {
                                allPossibleMoves.add(new EnemyAIMove(CommandType.HIT, newMove, gamePieceToMove, moveSet));
                                System.out.println("Enemy AI: possible move - HIT:" + newMove.xVal + "," + newMove.yVal + " with gamepiece:" + gamePieceToMove.getName() );
                            } else if (!gameManager.IsGamePieceAtBoardLocation(newMove)) {
                                allPossibleMoves.add(new EnemyAIMove(CommandType.MOVE, newMove, gamePieceToMove, moveSet));
                                System.out.println("Enemy AI: possible move - MOVE:" + newMove.xVal + "," + newMove.yVal + " with gamepiece:" + gamePieceToMove.getName() );
                            }
                        }
                    }
                }
            }
        }
        return allPossibleMoves;
    }
}
