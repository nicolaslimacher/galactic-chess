package com.mygdx.game.Manager;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Board.Board;
import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.IntPair;

import java.util.ArrayList;
import java.util.List;

public class MoveManager {
    public MoveManager() {
    }

    public static List<IntPair> GetPossibleMoves(GamePiece gamePiece, MoveSet moveSet) {
        List<IntPair> possibleMoves = new ArrayList<>();
        for (IntPair possibleMove : new Array.ArrayIterator<>(moveSet.possibleMoves)) {
            IntPair newMove = new IntPair(gamePiece.indexOnBoard.xVal + possibleMove.xVal, gamePiece.indexOnBoard.yVal + possibleMove.yVal);
            if (IsValidMove(gamePiece, possibleMove) && !gamePiece.gameManager.IsGamePieceAtBoardLocation(newMove)) {
                possibleMoves.add(newMove);
            }
        }
        return possibleMoves;
    }

    public static List<IntPair> GetPossibleTargets(GamePiece gamePiece, MoveSet moveSet) {
        List<IntPair> possibleMoves = new ArrayList<>();
        for (IntPair possibleMove : new Array.ArrayIterator<>(moveSet.possibleMoves)) {
            IntPair newMove = new IntPair(gamePiece.indexOnBoard.xVal + possibleMove.xVal, gamePiece.indexOnBoard.yVal + possibleMove.yVal);
            if (IsValidMove(gamePiece, possibleMove) && gamePiece.gameManager.IsTeamGamePieceAtBoardLocation(newMove, Team.ENEMY)) {
                if (gamePiece.gameManager.IsGamePieceAtBoardLocation(newMove)) {
                    possibleMoves.add(newMove);
                }
            }
        }
        return possibleMoves;
    }

    public static boolean IsValidMove(GamePiece gamePiece, IntPair intPair) {
        boolean isValid = false;
        boolean xIsValid = 0 <= gamePiece.indexOnBoard.xVal + intPair.xVal && gamePiece.indexOnBoard.xVal + intPair.xVal <= gamePiece.board.boardColumns - 1;
        boolean yIsValid = 0 <= gamePiece.indexOnBoard.yVal + intPair.yVal && gamePiece.indexOnBoard.yVal + intPair.yVal <= gamePiece.board.boardRows - 1;
        if (xIsValid && yIsValid) {
            isValid = true;
        }
        return isValid;
    }

    //due to mirrored game board from enemy perspective, math for checking is different
    public static boolean IsValidEnemyMove(GamePiece gamePiece, IntPair intPair) {
        boolean isValid = false;
        boolean xIsValid = 0 <= gamePiece.indexOnBoard.xVal + (-1 * intPair.xVal) && gamePiece.indexOnBoard.xVal + (-1 * intPair.xVal) <= gamePiece.board.boardColumns - 1;
        boolean yIsValid = 0 <= gamePiece.indexOnBoard.yVal + (-1 * intPair.yVal) && gamePiece.indexOnBoard.yVal + (-1 * intPair.yVal) <= gamePiece.board.boardRows - 1;
        if (xIsValid && yIsValid) {
            isValid = true;
        }
        return isValid;
    }
}
