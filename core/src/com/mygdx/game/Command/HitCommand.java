package com.mygdx.game.Command;

import com.mygdx.game.Board.Board;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.GameManager.Team;
import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.Utils.CoordinateBoardPair;

public class HitCommand extends Command{
    //info to recreate pawn if undo
    private final GamePiece targetGamePiece;
    private final Board board;
    private final CoordinateBoardPair targetPosition;
    private final GameManager gameManager;
    private final Team team;
    private final int targetGamePiecePreviousAtk;
    private final int targetGamePiecePreviousHealth;

    public HitCommand(GamePiece gamePiece, CoordinateBoardPair positionToMoveTo, GamePiece targetGamePiece) {
        super(gamePiece, positionToMoveTo);
        this.targetGamePiece = targetGamePiece;
        this.board = targetGamePiece.pawnBoard;
        this.targetPosition = targetGamePiece.indexOnBoard;
        this.gameManager = targetGamePiece.gameManager;
        this.team = targetGamePiece.team;
        this.targetGamePiecePreviousAtk = targetGamePiece.GetAttackPoints();
        this.targetGamePiecePreviousHealth = targetGamePiece.GetHitPoints();
        Execute();
        gamePiece.gameManager.selectedGamePiece = null;

    }

    @Override
    public void Execute() {
        if(this.gamePiece.HitPawn(this.targetGamePiece)) {
            this.gamePiece.Move(this.targetPosition);
        }
    }
    @Override
    public void Undo(){
        //TODO: when i add types of pawns, need to recreate that specific pawn
        //TODO: do i need to re-add pawn to GameManager.enemyGamePieces? printScreen list of enemyGamePieces to check if duplicated
        this.gamePiece.Move(this.previousPosition);
        GamePiece replacedGamePiece = new GamePiece(this.board, this.targetPosition, this.team, this.targetGamePiecePreviousHealth, this.targetGamePiecePreviousAtk, this.gameManager);
        this.gameManager.enemyGamePieces.add(replacedGamePiece);
        this.gameManager.getStage().addActor(replacedGamePiece);
    }
}
