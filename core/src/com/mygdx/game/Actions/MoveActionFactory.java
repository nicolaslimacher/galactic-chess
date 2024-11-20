package com.mygdx.game.Actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.Utils.IntPair;

public class MoveActionFactory {
    public enum MoveActionType{
        JETPACKJUMP, TELEPORT;
    }

    public static Action CreateMoveAction(MoveActionType type, GamePiece gamePiece, IntPair intPair, float actionDelay){
        switch (type){
            case JETPACKJUMP:
                return new JetpackJumpAction(gamePiece, intPair, actionDelay).JumpAction;
            case TELEPORT:
                MoveToAction move = new MoveToAction();
                move.setPosition(gamePiece.board.GetBoardTilePosition(intPair).x, gamePiece.board.GetBoardTilePosition(intPair).y);
                return move;
            default:
                return null;
        }
    }

}
