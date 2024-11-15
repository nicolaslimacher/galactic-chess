package com.mygdx.game.Command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Board.Board;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.GameManager.Team;
import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.GamePiece.GamePieceFactory;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.IntPair;

public class Command {
    final CommandType commandType;
    final GamePiece gamePiece;
    public final MoveSet moveSet;

    private final Board board;
    private final GameManager gameManager;
    final IntPair previousPosition;
    final IntPair targetPosition;
    final boolean isKing;


    //hit-specific fields
    private GamePiece targetGamePiece;
    private Team targetTeam;
    private int targetGamePiecePreviousAtk;
    private int targetGamePiecePreviousHealth;


    public Command(GamePiece gamePiece, IntPair targetPosition, CommandType commandType, MoveSet moveSet) {
        this.gamePiece = gamePiece;
        this.gameManager = gamePiece.gameManager;
        this.board = gamePiece.board;
        //this.gamePiecesID = gamePiece.GetGamePiecesID();
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
        Gdx.app.log("Command", "Command created: " + commandType + " to " + targetPosition.xVal + "," + targetPosition.yVal + ".");
    }

    public void Execute() {
        if (commandType == CommandType.MOVE) {
            this.gamePiece.JetpackJump(this.targetPosition, 0f); //no delay for player move
        }else {
            if (this.gamePiece.HitGamePiece(this.targetGamePiece)) {
                this.gamePiece.JetpackJump(this.targetPosition, 0f); //no delay for player move
            }

        }
        this.gameManager.movedThisTurn = true;
        this.gameManager.undoEndTurnMenu.EnableUndoButton();
        this.gameManager.undoEndTurnMenu.EnableEndTurnButton();

        Gdx.app.log("Command", "Player executed move: " + this.moveSet.getName() + ".");
    }

    public void Undo() {
        if (commandType == CommandType.MOVE) {
            this.gamePiece.teleport(this.previousPosition);
        }else{
            this.gamePiece.teleport(this.previousPosition);

            //GamePiece replacedGamePiece = GamePieceFactory.CreateGamePiece(this.board, this.gameManager, this.targetPosition, this.targetTeam, this.isKing, this.gamePiecesID, this.targetGamePiecePreviousHealth, this.targetGamePiecePreviousAtk);
            GamePiece replacedGamePiece = new GamePiece(this.board, this.targetPosition, this.targetTeam, this.isKing, this.targetGamePiecePreviousHealth, this.targetGamePiecePreviousAtk, this.gameManager);
            this.gameManager.enemyGamePieces.add(replacedGamePiece);
            //TODO: better way to cast GamePiece to Actor?
            this.gameManager.getStage().addActor( replacedGamePiece);
            replacedGamePiece.addHPandAttackLabels();
        }

        this.gameManager.movedThisTurn = false;
        this.gameManager.undoEndTurnMenu.DisableUndoButton();
        this.gameManager.undoEndTurnMenu.DisableEndTurnButton();

        Gdx.app.log("Command", "Player undid move: " + this.moveSet.getName() + ".");
    }
}