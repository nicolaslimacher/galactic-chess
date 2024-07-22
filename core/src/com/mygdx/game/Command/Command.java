package com.mygdx.game.Command;

import com.mygdx.game.Board.Board;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.GameManager.Team;
import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.CoordinateBoardPair;

public class Command {
    final CommandType commandType;
    final GamePiece gamePiece;
    public final MoveSet moveSet;

    private final Board board;
    private final GameManager gameManager;
    final CoordinateBoardPair previousPosition;
    final CoordinateBoardPair targetPosition;
    final boolean isKing;


    //hit-specific fields
    private GamePiece targetGamePiece;
    private Team targetTeam;
    private int targetGamePiecePreviousAtk;
    private int targetGamePiecePreviousHealth;


    public Command(GamePiece gamePiece, CoordinateBoardPair targetPosition, CommandType commandType, MoveSet moveSet) {
        this.gamePiece = gamePiece;
        this.gameManager = gamePiece.gameManager;
        this.board = gamePiece.board;
        this.commandType = commandType;
        this.previousPosition = gamePiece.indexOnBoard;
        this.targetPosition = targetPosition;
        this.isKing = gamePiece.isKing;
        this.moveSet = moveSet;
        if (commandType == CommandType.HIT) {
            this.targetGamePiece = gameManager.GetGamePieceAtCoordinate(this.targetPosition);
            this.targetTeam = Team.ENEMY;
            this.targetGamePiecePreviousAtk = targetGamePiece.GetAttackPoints();
            this.targetGamePiecePreviousHealth = targetGamePiece.GetHitPoints();
        }
    }

    public void Execute() {
        if (commandType == CommandType.MOVE) {
            this.gamePiece.Move(this.targetPosition);
        }else {
                if (this.gamePiece.HitGamePiece(this.targetGamePiece)) {
                    this.gamePiece.Move(this.targetPosition);
                }

        }
        //set move select menu back to visible
        //this.gameManager.selectedMoveSet = null;
        this.gameManager.movedThisTurn = true;
        //this.gameManager.moveSelectCards.setVisible(true);
        //if (this.gameManager.getStage().getRoot().findActor("MoveConfirmationMenu") != null) {
        //    this.gameManager.getStage().getRoot().findActor("MoveConfirmationMenu").remove();
        this.gameManager.undoEndTurnMenu.EnableUndoButton();
        this.gameManager.undoEndTurnMenu.EnableEndTurnButton();

        System.out.println("Player executed move: " + this.moveSet.getName());

    }

    public void Undo() {
        if (commandType == CommandType.MOVE) {
            this.gamePiece.Teleport(this.previousPosition);
            gameManager.selectedGamePiece = this.gamePiece;
        }else{
            this.gamePiece.Teleport(this.previousPosition);
            GamePiece replacedGamePiece = new GamePiece(this.board, this.targetPosition, this.targetTeam, this.isKing, this.targetGamePiecePreviousHealth, this.targetGamePiecePreviousAtk, this.gameManager);
            this.gameManager.enemyGamePieces.add(replacedGamePiece);
            this.gameManager.getStage().addActor(replacedGamePiece);
            replacedGamePiece.addHPandAttackLabels();
            gameManager.selectedGamePiece = this.gamePiece;
        }

        this.gameManager.movedThisTurn = false;
        this.gameManager.undoEndTurnMenu.DisableUndoButton();
        this.gameManager.undoEndTurnMenu.DisableEndTurnButton();


        System.out.println("Player undid move: " + this.moveSet.getName());
    }
}
