package com.mygdx.game.EnemyAI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.mygdx.game.Actions.MoveActionFactory;
import com.mygdx.game.Command.CommandType;
import com.mygdx.game.Manager.BattleManager;
import com.mygdx.game.Manager.MoveManager;
import com.mygdx.game.Manager.Team;
import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.IntPair;

import java.util.ArrayList;
import java.util.List;

public class EnemyAI extends Actor {
    //extends actor so that runnable actions can be added to EnemyAI (for delay)
    final BattleManager battleManager;
    final float enemyAIDelay = 3f;


    public EnemyAI(BattleManager battleManager) {
        this.battleManager = battleManager;
        this.battleManager.getStage().addActor(this);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public void MakeMove(){
        List<EnemyAIMove> allPossibleMoves = GetAllPossibleMoves();
        if (allPossibleMoves.isEmpty()){
            Gdx.app.log("EnemyAI", "no valid moves");
            //TODO: add random damage, show popup
            return;
        }
        int rnd = getRandomNumber(0, allPossibleMoves.size()-1);
        EnemyAIMove enemyAIMove = allPossibleMoves.get(rnd);
        if(enemyAIMove.commandType == CommandType.MOVE) {
            enemyAIMove.gamePiece.MoveToWithAction(MoveActionFactory.MoveActionType.JETPACKJUMP, enemyAIMove.coordinates, enemyAIDelay);
            Gdx.app.log("EnemyAI", "Enemy executed move: " + enemyAIMove.moveSet.getName());
        }else{
            if (enemyAIMove.gamePiece.HitGamePiece(battleManager.GetGamePieceAtCoordinate(enemyAIMove.coordinates))) {
                enemyAIMove.gamePiece.MoveToWithAction(MoveActionFactory.MoveActionType.JETPACKJUMP,enemyAIMove.coordinates, enemyAIDelay);
            }
            Gdx.app.log("EnemyAI", "Enemy executed move: " + enemyAIMove.moveSet.getName());
        }

        CallEndTurnAfterDelay(2f, enemyAIMove.moveSet);

    }

    private List<EnemyAIMove> GetAllPossibleMoves(){
        List<EnemyAIMove> allPossibleMoves = new ArrayList<>();
        for (GamePiece defaultPawnToMove :  battleManager.enemyGamePieces) {
            if (defaultPawnToMove.isAlive) {
                for (MoveSet moveSet : battleManager.enemyMoves) {
                    for (IntPair possibleMove : moveSet.possibleMoves) {
                        IntPair newMove = new IntPair(defaultPawnToMove.indexOnBoard.xVal + (-1 * possibleMove.xVal), defaultPawnToMove.indexOnBoard.yVal + (-1 * possibleMove.yVal));
                        if (MoveManager.IsValidEnemyMove(defaultPawnToMove, possibleMove)) {
                            if (battleManager.IsGamePieceAtBoardLocation(newMove) && battleManager.GetGamePieceAtCoordinate(newMove).team == Team.FRIENDLY) {
                                //check if piece at location AND is friendly
                                allPossibleMoves.add(new EnemyAIMove(CommandType.HIT, newMove, defaultPawnToMove, moveSet));
                                Gdx.app.log("EnemyAI", "possible move - HIT:" + newMove.xVal + "," + newMove.yVal + " with gamepiece:" + defaultPawnToMove.getName());
                            } else if (!battleManager.IsGamePieceAtBoardLocation(newMove)) {
                                //check if space is free (so enemy doesn't move on top of own piece)
                                allPossibleMoves.add(new EnemyAIMove(CommandType.MOVE, newMove, defaultPawnToMove, moveSet));
                                Gdx.app.log("EnemyAI", "possible move - MOVE:" + newMove.xVal + "," + newMove.yVal + " with gamepiece:" + defaultPawnToMove.getName());
                            }
                        }
                    }
                }
            }
        }
        return allPossibleMoves;
    }

    private void CallEndTurnAfterDelay(float delay, MoveSet moveSetUsed){
        RunnableAction runnableAction = Actions.run(new Runnable(){
            public void run(){
                CallEndTurn(moveSetUsed);
            }
        });
        SequenceAction sequenceAction = new SequenceAction(Actions.delay(delay),runnableAction);
        this.addAction(sequenceAction);

    }

    private void CallEndTurn(MoveSet moveSet){
        this.battleManager.EndEnemyTurn(moveSet);
    }

}