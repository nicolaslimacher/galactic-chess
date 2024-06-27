package com.mygdx.game.EnemyAI;

import com.mygdx.game.Command.CommandType;
import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.Utils.CoordinateBoardPair;

public class EnemyAIMove {
    public GamePiece gamePiece;
    public CoordinateBoardPair coordinateBoardPair;
    public CommandType commandType;

    public EnemyAIMove(CommandType commandType, CoordinateBoardPair coordinateBoardPair, GamePiece gamePiece) {
        this.commandType = commandType;
        this.coordinateBoardPair = coordinateBoardPair;
        this.gamePiece = gamePiece;
    }
}
