package com.mygdx.game.Command;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.Actions.MoveActionFactory;
import com.mygdx.game.Board.Board;
import com.mygdx.game.Manager.BattleManager;
import com.mygdx.game.Manager.Team;
import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.Helpers;
import com.mygdx.game.Utils.IntPair;

public class Command {
    final CommandType commandType;
    final GamePiece gamePiece;
    public final MoveSet moveSet;

    private final Board board;
    private final BattleManager battleManager;
    final IntPair previousPosition;
    final IntPair targetPosition;
    final boolean isKing;
    final int gamePieceID;


    //hit-specific fields
    private GamePiece targetGamePiece;
    private Team targetTeam;
    private int targetGamePiecePreviousAtk;
    private int targetGamePiecePreviousHealth;


    public Command(GamePiece actingGamePiece, IntPair targetPosition, CommandType commandType, MoveSet moveSet) {
        this.gamePiece = actingGamePiece;
        gamePieceID = gamePiece.gamePieceID;
        this.battleManager = gamePiece.battleManager;
        this.board = gamePiece.board;
        //this.gamePiecesID = gamePiece.GetGamePiecesID();
        this.commandType = commandType;
        this.previousPosition = gamePiece.indexOnBoard;
        this.targetPosition = targetPosition;
        this.isKing = gamePiece.isKing;
        this.moveSet = moveSet;
        if (commandType == CommandType.HIT) {
            this.targetGamePiece = battleManager.GetGamePieceAtCoordinate(this.targetPosition);
            this.targetTeam = Team.ENEMY;
            this.targetGamePiecePreviousAtk = targetGamePiece.getAttackPoints();
            this.targetGamePiecePreviousHealth = targetGamePiece.getHitPoints();
        }
        Gdx.app.log("Command", "Command created: " + commandType + " to " + targetPosition.xVal + "," + targetPosition.yVal + ".");
    }

    public void Execute() {
        if (commandType == CommandType.MOVE) {
            this.gamePiece.MoveToWithAction(MoveActionFactory.MoveActionType.JETPACKJUMP, this.targetPosition, 0f); //no delay for player move
        }else {
            if (this.gamePiece.HitGamePiece(this.targetGamePiece)) {
                this.gamePiece.MoveToWithAction(MoveActionFactory.MoveActionType.JETPACKJUMP, this.targetPosition, 0f); //no delay for player move
            }

        }
        this.battleManager.movedThisTurn = true;
        Helpers.getGameScreen().getHUD().EnableUndoButton();
        Helpers.getGameScreen().getHUD().EnableEndTurnButton();

        Gdx.app.log("Command", "Player executed move: " + this.moveSet.getName() + ".");
    }

    public void Undo() {
        //TODO: might need to replace acting game piece as well. take, for example, if ability added to reflect damage
        if (commandType == CommandType.MOVE) {
            this.gamePiece.MoveToWithAction(MoveActionFactory.MoveActionType.TELEPORT, this.previousPosition, 0f);
        }else{
            this.gamePiece.MoveToWithAction(MoveActionFactory.MoveActionType.TELEPORT, this.previousPosition, 0f);

            GamePiece replacedGamePiece = new GamePiece(this.board, this.battleManager, this.gamePieceID, this.targetPosition, this.targetTeam, this.isKing);
            replacedGamePiece.setAttackPoints(this.targetGamePiecePreviousAtk);
            replacedGamePiece.setHitPoints(this.targetGamePiecePreviousHealth);
            this.battleManager.enemyGamePieces.add(replacedGamePiece);
            this.battleManager.getStage().addActor(replacedGamePiece);
        }

        this.battleManager.movedThisTurn = false;
        Helpers.getGameScreen().getHUD().DisableUndoButton();
        Helpers.getGameScreen().getHUD().DisableEndTurnButton();

        Gdx.app.log("Command", "Player undid move: " + this.moveSet.getName() + ".");
    }
}