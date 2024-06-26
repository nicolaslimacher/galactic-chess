package com.mygdx.game.Command;

import com.mygdx.game.Board.Board;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.GameManager.Team;
import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.Utils.CoordinateBoardPair;

public class Command {
    final CommandType commandType;
    final GamePiece gamePiece;

    private final Board board;
    private final GameManager gameManager;
    final CoordinateBoardPair previousPosition;
    final CoordinateBoardPair targetPosition;


    //hit-specific fields
    private GamePiece targetGamePiece;
    private Team targetTeam;
    private int targetGamePiecePreviousAtk;
    private int targetGamePiecePreviousHealth;


    public Command(GamePiece gamePiece, CoordinateBoardPair targetPosition, CommandType commandType) {
        this.gamePiece = gamePiece;
        this.gameManager = gamePiece.gameManager;
        this.board = gamePiece.pawnBoard;
        this.commandType = commandType;
        this.previousPosition = gamePiece.indexOnBoard;
        this.targetPosition = targetPosition;
        if (commandType == CommandType.HIT) {
            this.targetGamePiece = gameManager.GetPawnAtCoordinate(this.targetPosition);
            this.targetTeam = gamePiece.team;
            this.targetGamePiecePreviousAtk = targetGamePiece.GetAttackPoints();
            this.targetGamePiecePreviousHealth = targetGamePiece.GetHitPoints();
        }
    }

    public void Execute() {
        System.out.println("CommandType: " + commandType);
        if (commandType == CommandType.MOVE) {
            System.out.println("executing Move");
            this.gamePiece.Move(this.targetPosition);
            gameManager.selectedGamePiece = null;
        }else {
                System.out.println("executing Hit");
                if (this.gamePiece.HitPawn(this.targetGamePiece)) {
                    this.gamePiece.Move(this.targetPosition);
                }
                gameManager.selectedGamePiece = null;

        }
        //set move select menu back to visible
        this.gameManager.selectedMoveSet = null;
        this.gameManager.menuTable.setVisible(true);
        if (this.gameManager.getStage().getRoot().findActor("MoveConfirmationMenu") != null) {
            System.out.println("MoveConfirmationMenu is not null");
            this.gameManager.getStage().getRoot().findActor("MoveConfirmationMenu").remove();
            //this.pawnBoard.menuTable = null;
        }

    }

    public void Undo() {
        if (commandType == CommandType.MOVE) {
            System.out.println("Move Command: undo fired");
            this.gamePiece.Move(this.previousPosition);
            gameManager.selectedGamePiece = this.gamePiece;
        }else{
            System.out.println("Hit Command: undo fired");
            //TODO: do i need to re-add pawn to GameManager.enemyGamePieces? printScreen list of enemyGamePieces to check if duplicated
            this.gamePiece.Move(this.previousPosition);
            GamePiece replacedGamePiece = new GamePiece(this.board, this.targetPosition, this.targetTeam, this.targetGamePiecePreviousHealth, this.targetGamePiecePreviousAtk, this.gameManager);
            this.gameManager.enemyGamePieces.add(replacedGamePiece);
            this.gameManager.getStage().addActor(replacedGamePiece);
            replacedGamePiece.addHPandAttackLabels();
            gameManager.selectedGamePiece = this.gamePiece;
        }

    }
}
