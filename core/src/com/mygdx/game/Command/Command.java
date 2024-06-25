package com.mygdx.game.Command;

import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.Utils.CoordinateBoardPair;

public class Command {

    final GamePiece gamePiece;
    final CoordinateBoardPair previousPosition;
    final CoordinateBoardPair newPosition;

    public Command(GamePiece gamePiece, CoordinateBoardPair positionToMoveTo) {
        this.gamePiece = gamePiece;
        this.previousPosition = gamePiece.indexOnBoard;
        this.newPosition = positionToMoveTo;
    }

    public void Execute() {
    }

    public void Undo() {
    }
}
