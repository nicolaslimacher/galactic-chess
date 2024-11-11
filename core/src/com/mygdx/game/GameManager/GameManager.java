package com.mygdx.game.GameManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    public TextureRegion smallStar;
    public TextureRegion mediumStar;

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
    public List<MoveSet> playerMoves = new ArrayList<>(2);
    public MoveSet selectedMoveSet = null;

    //menu
    public MoveSelectCards moveSelectCards;
    public UndoEndTurnMenu undoEndTurnMenu;
    public MoveConfirmation moveConfirmation;

    public GameManager(Stage stage, Board board, List<MoveSet> availableMoveSets, GameScreen gameScreen) {
        this.stage = stage;
        this.board = board;
        this.gameScreen = gameScreen;
        smallStar = GetAssetManager().get("texturePacks/battleTextures.atlas", TextureAtlas.class).findRegion("smallstar1");
        mediumStar = GetAssetManager().get("texturePacks/battleTextures.atlas", TextureAtlas.class).findRegion("mediumstar.png");
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
        Gdx.app.log("GameManager", "GameManager created.");
    }

    private final InputListener stageInputListener = new InputListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            Object selectedObject = event.getTarget();
            GameManager gameManager = GameManager.this;
            for(Actor actor:stage.getActors()){
                if(actor.getClass() == GamePiece.class) {
                    GamePiece gamePiece = (GamePiece) actor;
                    //doing this rather than just checking !gameManager.selectedGamePiece.equals(selectedObject)
                    //so that player can click menu items
                    if (!gamePiece.equals(selectedObject) && gamePiece == gameManager.selectedGamePiece) {
                        gameManager.selectedGamePiece = null;
                        if (stage.getRoot().findActor("possibleMovesGroup" + gamePiece.getName()) != null){
                            stage.getRoot().findActor("possibleMovesGroup" + gamePiece.getName()).remove();
                        }

                    }
                }
            }
            Gdx.app.log("GameManager", "Stage-level input listener received input.");
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

    public AssetManager GetAssetManager(){
        return gameScreen.GetGame().GetAssetManager();
    }

    private boolean PlayerHasAValidMove(){
        for (GamePiece gamePieceToMove :  this.friendlyGamePieces) {
            if (gamePieceToMove.isAlive) {
                for (MoveSet moveSet : this.playerMoves) {
                    for (IntPair possibleMove : moveSet.possibleMoves) {
                        if (gamePieceToMove.IsValidMove(possibleMove) && !IsTeamGamePieceAtBoardLocation(possibleMove, Team.FRIENDLY)) {
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
        playerMoves.add(availableMoveSets.get(3));
        playerMoves.add(availableMoveSets.get(4));

    }

    private void ShuffleCardsAfterPlayer(MoveSet playerMoveSetUsed){
        playerMoves.remove(playerMoveSetUsed);
        MoveSet freeMoveToAdd = freeMove.get(0);
        freeMove.remove(freeMoveToAdd);
        freeMove.add(playerMoveSetUsed);
        playerMoves.add(freeMoveToAdd);

    }

    private void ShuffleCardsAfterEnemy(MoveSet enemyMoveSetUsed){
        enemyMoves.remove(enemyMoveSetUsed);
        MoveSet freeMoveToAdd = freeMove.get(0);
        freeMove.remove(freeMoveToAdd);
        freeMove.add(enemyMoveSetUsed);
        enemyMoves.add(freeMoveToAdd);
    }

    public void DisplayPlayerMessage(String headerMessage){
        new DisplayMessage(this, headerMessage, 3f);
    }

    public void DisplayPlayerMessage(String headerMessage, String subMessage){
        new DisplayMessage(this, headerMessage, subMessage, 3f);
    }

    public void EndPlayerTurn(){
        //returning bool so EndGameScreenIfKingsDead can exit method
        //could wrap rest of call in check for enemyKingIsDead?

        //check if enemy team has a king left, then check if king has any health
        if(EndGameScreenIfKingsDead(Team.ENEMY))
            return;


        //reset and shuffle chemicals
        if (this.getStage().getRoot().findActor("MoveConfirmationMenu") != null){
            MoveConfirmation moveConf = this.getStage().getRoot().findActor("MoveConfirmationMenu");
            moveConf.ReturnToMoveSelectCards();
        }

        ShuffleCardsAfterPlayer(this.latestGamePieceCommand.moveSet);
        //update turn info
        this.turnNumber = this.turnNumber + 1;
        this.currentTurn = Team.ENEMY;
        this.movedThisTurn = false;
        this.latestGamePieceCommand = null;

        this.moveSelectCards.setVisible(true);
        this.moveSelectCards.UpdateCardLocations();
        //call AI to make turn
        undoEndTurnMenu.DisableEndTurnButton();
        undoEndTurnMenu.DisableUndoButton();

        enemyAI.MakeMove();
    }

    public void EndEnemyTurn(MoveSet enemyMoveSetUsed){

        ShuffleCardsAfterEnemy(enemyMoveSetUsed);
        this.moveSelectCards.UpdateCardLocations();

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
    }
}
