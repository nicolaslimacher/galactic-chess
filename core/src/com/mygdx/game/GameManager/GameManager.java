package com.mygdx.game.GameManager;

import com.badlogic.gdx.Gdx;
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
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameManager extends Actor{
    Stage stage;
    public Board board;

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
    public List<MoveSet> availableMoveSets;
    public List<MoveSet> enemyMoves = new ArrayList<>(2);
    public List<MoveSet> freeMove = new ArrayList<>(1);
    public List<MoveSet> myMoves = new ArrayList<>(2);;
    public MoveSet selectedMoveSet = null;

    //menu
    public MoveSelectCards moveSelectCards;
    public UndoEndTurnMenu undoEndTurnMenu;
    public MoveConfirmation moveConfirmation;

    public GameManager(Stage stage, Board board, List<MoveSet> availableMoveSets) {
        this.stage = stage;
        this.board = board;
        this.availableMoveSets = availableMoveSets;
        AssignStartingChemicals();
        this.moveSelectCards = new MoveSelectCards(this);
        this.undoEndTurnMenu = new UndoEndTurnMenu();
        this.turnCounterMenu = new TurnCounterMenu(this);

        //adding actors to the stage
//        stage.addActor(board);
//        for (GamePiece gamePiece: this.friendlyGamePieces){
//            stage.addActor(gamePiece);
//            gamePiece.gameManager = this;
//            gamePiece.addHPandAttackLabels();
//        }
//        for (GamePiece gamePiece: this.enemyGamePieces){
//            stage.addActor(gamePiece);
//            gamePiece.gameManager = this;
//            gamePiece.addHPandAttackLabels();
//        }
        this.currentTurn = Team.FRIENDLY;
        this.setName("GameManager");
        stage.addActor(this.moveSelectCards);
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
        Collections.shuffle(availableMoveSets);
        enemyMoves.add(availableMoveSets.get(0));
        enemyMoves.add(availableMoveSets.get(1));
        freeMove.add(availableMoveSets.get(2));
        myMoves.add(availableMoveSets.get(3));
        myMoves.add(availableMoveSets.get(4));

    }

    private void ShuffleCardsAfterPlayer(MoveSet playerMoveSetUsed){
        myMoves.remove(playerMoveSetUsed);
        MoveSet freeMoveToAdd = freeMove.get(0);
        freeMove.remove(freeMoveToAdd);
        freeMove.add(playerMoveSetUsed);
        myMoves.add(freeMoveToAdd);
    }

    private void ShuffleCardsAfterEnemy(MoveSet enemyMoveSetUsed){
        enemyMoves.remove(enemyMoveSetUsed);
        MoveSet freeMoveToAdd = freeMove.get(0);
        freeMove.remove(freeMoveToAdd);
        freeMove.add(enemyMoveSetUsed);
        enemyMoves.add(freeMoveToAdd);
    }

    public void EndPlayerTurn(){
        Timer enemyAITimer = new Timer("enemyAITimer", true); //running as daemon so program stops when window closed
        this.turnNumber = this.turnNumber + 1;
        this.currentTurn = Team.ENEMY;
        this.movedThisTurn = false;

        //shuffle chemicals
        ShuffleCardsAfterPlayer(this.latestGamePieceCommand.moveSet);
        this.latestGamePieceCommand = null;

        this.moveSelectCards.UpdateCards();


        //call AI to make turn
            //TODO: try timer
        TimerTask enemyAImove = new TimerTask() {
            @Override
            public void run() {
                undoEndTurnMenu.DisableEndTurnButton();
                undoEndTurnMenu.DisableUndoButton();

                MoveSet enemyMoveUsed = enemyAI.MakeMove();
                ShuffleCardsAfterEnemy(enemyMoveUsed);
                moveSelectCards.UpdateCards();

                turnCounterMenu.UpdateTurn();
            }
        };
        long enemyAIDelay = 750L;
        enemyAITimer.schedule(enemyAImove, enemyAIDelay);

        //Gdx.app.wait(500L);
        //MoveSet enemyMoveUsed = this.enemyAI.MakeMove();
//        ShuffleCardsAfterEnemy(enemyMoveUsed);
//
//        this.moveSelectCards.UpdateCards();
    }
}
