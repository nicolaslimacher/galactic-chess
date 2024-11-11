package com.mygdx.game.EnemyAI;

import com.mygdx.game.Command.CommandType;
import com.mygdx.game.GamePiece.DefaultPawn;
import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.IntPair;

public class EnemyAIMove {
    public GamePiece gamePiece;
    public IntPair coordinates;
    public CommandType commandType;
    public MoveSet moveSet;

    public EnemyAIMove(CommandType commandType, IntPair coordinates, GamePiece gamePiece, MoveSet moveSet) {
        this.commandType = commandType;
        this.coordinates = coordinates;
        this.gamePiece = gamePiece;
        this.moveSet = moveSet;
    }
}
