package com.mygdx.game.Command;

import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.Utils.CoordinateBoardPair;

public class MoveCommand extends Command{
    public MoveCommand(GamePiece gamePiece, CoordinateBoardPair positionToMoveTo) {
        super(gamePiece, positionToMoveTo);
        Execute();
        gamePiece.gameManager.selectedGamePiece = null;
    }

    @Override
    public void Execute() {
        this.gamePiece.Move(this.newPosition);
    }
    @Override
    public void Undo(){
        this.gamePiece.Move(this.previousPosition);
    }
}
