package com.mygdx.game.GameManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.Utils.IntPair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameManager extends Actor{
    Stage stage;
    public Board board;
    final GameScreen gameScreen;

    //background
    public Texture smallStar;
    public Texture mediumStar;

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

    public GameManager(Stage stage, Board board, List<MoveSet> availableMoveSets, GameScreen gameScreen) {
        this.stage = stage;
        this.board = board;
        this.gameScreen = gameScreen;
        smallStar = new Texture(Gdx.files.internal("smallstar1.png"));
        mediumStar = new Texture(Gdx.files.internal("mediumstar.png"));
        this.availableMoveSets = availableMoveSets;
        AssignStartingChemicals();
        this.moveSelectCards = new MoveSelectCards(this, stage);
        this.undoEndTurnMenu = new UndoEndTurnMenu();
        this.turnCounterMenu = new TurnCounterMenu(this);

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
            GameManager gameManager = GameManager.this;
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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                       GamePiece Management                                 //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public Boolean IsGamePieceAtBoardLocation(IntPair coordinates) {
        return GetGamePieceAtCoordinate(coordinates) != null;
    }
    public Boolean IsTeamGamePieceAtBoardLocation(IntPair coordinates, Team team) {
        return GetGamePieceAtCoordinate(coordinates) != null && (GetGamePieceAtCoordinate(coordinates).team == team);
    }
    public GamePiece GetGamePieceAtCoordinate(IntPair coordinateBoardPair){
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

    private boolean IsValidKingLeft(Team team){
        ArrayList<GamePiece> gamePieces = (team == Team.FRIENDLY) ? friendlyGamePieces : enemyGamePieces;
        for ( GamePiece gamePiece : gamePieces) {
            if (gamePiece.isKing && gamePiece.isAlive) {
                return true;
                //this.gameScreen.SwitchScreenEndGame();
            }
        }
        return false;
    }

    private boolean PlayerHasAValidMove(){
        for (GamePiece gamePieceToMove :  this.friendlyGamePieces) {
            if (gamePieceToMove.isAlive) {
                for (MoveSet moveSet : this.myMoves) {
                    for (IntPair possibleMove : moveSet.possibleMoves) {
                        if (gamePieceToMove.isValidMove(possibleMove) && !IsTeamGamePieceAtBoardLocation(possibleMove, Team.FRIENDLY)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean EndGameScreenIfKingsDead(Team team){
        if (IsValidKingLeft(team)){
            System.out.println("There is a valid enemy king left");

        }else{
            System.out.println("There is no valid enemy king left");
            //switch screens
            this.gameScreen.SwitchScreenEndGame();
            return true;
        }
        return false;
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

    public void DisplayPlayerMessage(String headerMessage){
        DisplayMessage message = new DisplayMessage(this, headerMessage, 3f);
    }

    public void DisplayPlayerMessage(String headerMessage, String subMessage){
        DisplayMessage message = new DisplayMessage(this, headerMessage, subMessage, 3f);
    }

    public boolean EndPlayerTurn(){
        //returning bool so EndGameScreenIfKingsDead can exit method
        //could wrap rest of call in check for enemyKingIsDead?

        //check if enemy team has a king left, then check if king has any health
        if(EndGameScreenIfKingsDead(Team.ENEMY))
            return true;


        //update turn info
        this.turnNumber = this.turnNumber + 1;
        this.currentTurn = Team.ENEMY;
        this.movedThisTurn = false;

        //reset and shuffle chemicals
        if (this.getStage().getRoot().findActor("MoveConfirmationMenu") != null)
            this.getStage().getRoot().findActor("MoveConfirmationMenu").remove();

        ShuffleCardsAfterPlayer(this.latestGamePieceCommand.moveSet);
        this.moveSelectCards.setVisible(true);
        this.moveSelectCards.UpdateCardLocations();
        this.latestGamePieceCommand = null;


        //call AI to make turn
        System.out.println("creating enemy move");
        undoEndTurnMenu.DisableEndTurnButton();
        undoEndTurnMenu.DisableUndoButton();

        MoveSet enemyMoveUsed = enemyAI.MakeMove();
        ShuffleCardsAfterEnemy(enemyMoveUsed);
        moveSelectCards.UpdateCardLocations();

        turnCounterMenu.UpdateTurn();

        //after AI see if player has any possible moves
        if(!PlayerHasAValidMove()){
            DisplayPlayerMessage("No Valid Moves", "take some damage");
            //select king
            //king takes damage
            //increase move stuck damage
        }

        //check if enemy team has a king left, then check if king has any health
        EndGameScreenIfKingsDead(Team.FRIENDLY);

        return true;
    }
}
