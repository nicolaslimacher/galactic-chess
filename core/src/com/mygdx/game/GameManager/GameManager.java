package com.mygdx.game.GameManager;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.Board.Board;
import com.mygdx.game.BoardUI.MoveConfirmation;
import com.mygdx.game.BoardUI.MoveSelectCards;
import com.mygdx.game.BoardUI.TurnCounterMenu;
import com.mygdx.game.BoardUI.UndoEndTurnMenu;
import com.mygdx.game.Command.Command;
import com.mygdx.game.EnemyAI.EnemyAI;
import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.CoordinateBoardPair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameManager extends Actor{
    Stage stage;
    Board board;

    //turn
    public int turnNumber = 1;
    public TurnCounterMenu turnCounterMenu;
    public Team currentTurn;
    public boolean movedThisTurn = false;

    //game pieces
    public ArrayList<GamePiece> friendlyGamePieces;
    public ArrayList<GamePiece> enemyGamePieces;
    public GamePiece selectedGamePiece = null;
    public Command latestGamePieceCommand;
    public EnemyAI enemyAI;

    //selected Moves and GamePiece
    public MoveSet[] availableMoveSets;
    public MoveSet[] enemyMoves = new MoveSet[2];
    public MoveSet[] freeMoves = new MoveSet[1];
    public MoveSet[] myMoves = new MoveSet[2];
    public MoveSet selectedMoveSet = null;

    //menu
    public MoveSelectCards menuTable;
    public UndoEndTurnMenu undoEndTurnMenu;
    public MoveConfirmation moveConfirmation;

    public GameManager(Stage stage, Board board, ArrayList<GamePiece> friendlyGamePieces, ArrayList<GamePiece> enemyGamePieces, MoveSet[] availableMoveSets) {
        this.stage = stage;
        this.board = board;
        this.friendlyGamePieces = friendlyGamePieces;
        this.enemyGamePieces = enemyGamePieces;
        this.availableMoveSets = availableMoveSets;
        AssignStartingChemicals();
        this.menuTable = new MoveSelectCards(this);
        this.undoEndTurnMenu = new UndoEndTurnMenu();
        this.turnCounterMenu = new TurnCounterMenu(this);

        //adding actors to the stage
        stage.addActor(board);
        for (GamePiece gamePiece: this.friendlyGamePieces){
            stage.addActor(gamePiece);
            gamePiece.gameManager = this;
            gamePiece.addHPandAttackLabels();
        }
        for (GamePiece gamePiece: this.enemyGamePieces){
            stage.addActor(gamePiece);
            gamePiece.gameManager = this;
            gamePiece.addHPandAttackLabels();
        }
        this.currentTurn = Team.FRIENDLY;
        this.setName("GameManager");
        stage.addActor(this.menuTable);
        stage.addActor(this.undoEndTurnMenu);
        stage.addActor(this.turnCounterMenu);
        stage.addListener(stageInputListener);
    }

    private final InputListener stageInputListener = new InputListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            Object selectedObject = event.getTarget();
            GameManager gameManager = event.getStage().getRoot().findActor("GameManager");
            for(Actor actor:stage.getActors()){
                if(actor.getClass() == GamePiece.class) {
                    GamePiece gamePiece = (GamePiece) actor;
                    if (!gamePiece.equals(selectedObject) && gamePiece == gameManager.selectedGamePiece) {
                        gameManager.selectedGamePiece = null;
                        if (stage.getRoot().findActor("possibleMovesGroup" + gamePiece.getName()) != null){
                            stage.getRoot().findActor("possibleMovesGroup" + gamePiece.getName()).remove();
                        }

                    }
                }
            }
            return false;
        }
    };

    public Boolean IsPawnAtBoardLocation(CoordinateBoardPair coordinateBoardPair) {
        return GetPawnAtCoordinate(coordinateBoardPair) != null;
    }
    public Boolean IsEnemyPawnAtBoardLocation(CoordinateBoardPair coordinateBoardPair) {
        return GetPawnAtCoordinate(coordinateBoardPair) != null && !(GetPawnAtCoordinate(coordinateBoardPair).team == Team.FRIENDLY);
    }
    public GamePiece GetPawnAtCoordinate(CoordinateBoardPair coordinateBoardPair){
        for(Actor actor:this.getStage().getActors()){
            if(actor.getClass() == GamePiece.class) {
                GamePiece gamePiece = (GamePiece) actor;
                if (gamePiece.indexOnBoard.equals(coordinateBoardPair)) {
                    return gamePiece;
                }
            }
        }
        return null;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                       MoveSet Management                                   //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void AssignStartingChemicals () {
        List<MoveSet> availableMoveSetsAsList = Arrays.asList(availableMoveSets);
        Collections.shuffle(availableMoveSetsAsList);
        enemyMoves[0] = availableMoveSetsAsList.get(0);
        enemyMoves[1] = availableMoveSetsAsList.get(1);
        freeMoves[0] = availableMoveSetsAsList.get(2);
        myMoves[0] = availableMoveSetsAsList.get(3);
        myMoves[1] = availableMoveSetsAsList.get(4);

        System.out.println("AssignStartingChemicals() fired");
        System.out.println(enemyMoves[0].getName());

    }
}
