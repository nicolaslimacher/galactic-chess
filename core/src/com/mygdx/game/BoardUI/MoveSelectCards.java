package com.mygdx.game.BoardUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.MoveSets.MoveSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Displays cards for moves in Game Screen
 *
 * @author Nico Limacher
 * @since 1.0
 */

public class MoveSelectCards extends Actor {
    GameManager gameManager;
    Skin chemicalLabelSkin = new Skin(Gdx.files.internal("skins/uiskin.json"));
    Label enemyChemicalLabel, freeChemicalLabel, playerChemicalLabel;
    ArrayList<MoveCard> moveCards;
    ArrayList<Vector2> enemyMoveCardsLocations  = new ArrayList<>(2);
    ArrayList<Vector2> freeMoveCardsLocations= new ArrayList<>(1);
    ArrayList<Vector2> playerMoveCardsLocations = new ArrayList<>(2);

    public MoveSelectCards(GameManager gameManager, Stage stage) {
        this.gameManager = gameManager;
        this.moveCards = new ArrayList<>();

        enemyChemicalLabel = new Label("Enemy Chemicals" ,chemicalLabelSkin);
        enemyChemicalLabel.setPosition(MoveCardLocations.CHEMICAL_START_LOCATION_X,
                (MoveCardLocations.ENEMY_CHEMICAL_LOCATION_Y + MoveCardLocations.CHEMICAL_CARDS_HEIGHT));
        enemyChemicalLabel.setSize(MoveCardLocations.CHEMICAL_CARDS_WIDTH,
                MoveCardLocations.CHEMICAL_LABEL_HEIGHT);
        enemyChemicalLabel.setAlignment(Align.center);
        stage.addActor(enemyChemicalLabel);

        freeChemicalLabel = new Label("Free Chemicals" ,chemicalLabelSkin);
        freeChemicalLabel.setPosition(MoveCardLocations.CHEMICAL_START_LOCATION_X,
                (MoveCardLocations.FREE_CHEMICAL_LOCATION_Y + MoveCardLocations.CHEMICAL_CARDS_HEIGHT));
        freeChemicalLabel.setSize(MoveCardLocations.CHEMICAL_CARDS_WIDTH,
                MoveCardLocations.CHEMICAL_LABEL_HEIGHT);
        freeChemicalLabel.setAlignment(Align.center);
        stage.addActor(freeChemicalLabel);

        playerChemicalLabel = new Label("My Chemicals" ,chemicalLabelSkin);
        playerChemicalLabel.setPosition(MoveCardLocations.CHEMICAL_START_LOCATION_X,
                (MoveCardLocations.PLAYER_CHEMICAL_LOCATION_Y + MoveCardLocations.CHEMICAL_CARDS_HEIGHT));
        playerChemicalLabel.setSize(MoveCardLocations.CHEMICAL_CARDS_WIDTH,
                MoveCardLocations.CHEMICAL_LABEL_HEIGHT);
        playerChemicalLabel.setAlignment(Align.center);
        stage.addActor(playerChemicalLabel);


        CreateCards(stage);
        this.setName("MoveSelectCards");
    }

    public void CreateCards (Stage stage){
        UpdateCardLocationsReferences();

        if (!this.gameManager.enemyMoves.isEmpty()){
            int cardNumberIndex = 0;
            for (MoveSet moveSet : this.gameManager.enemyMoves) {
                MoveCard moveCard = new MoveCard(moveSet,
                        this.gameManager,
                        false,
                        enemyMoveCardsLocations.get(cardNumberIndex).x,
                        enemyMoveCardsLocations.get(cardNumberIndex).y);
                moveCards.add(moveCard);
                stage.addActor(moveCard);
                cardNumberIndex += 1;
            }
        }

        if (!this.gameManager.freeMove.isEmpty()){
            int enemyMoveCardNumber = gameManager.freeMove.size();
            float moveCardSegmentWidth = MoveCardLocations.CHEMICAL_CARDS_WIDTH/enemyMoveCardNumber;
            int cardNumberIndex = 0;
            for (MoveSet moveSet : this.gameManager.freeMove) {
                MoveCard moveCard = new MoveCard(moveSet,
                        this.gameManager,
                        false,
                        freeMoveCardsLocations.get(cardNumberIndex).x,
                        freeMoveCardsLocations.get(cardNumberIndex).y);
                moveCards.add(moveCard);
                stage.addActor(moveCard);
                cardNumberIndex += 1;
            }
        }

        if (!this.gameManager.playerMoves.isEmpty()){
            int cardNumberIndex = 0;
            for (MoveSet moveSet : this.gameManager.playerMoves) {
                MoveCard moveCard = new MoveCard(moveSet,
                        this.gameManager,
                        true,
                        playerMoveCardsLocations.get(cardNumberIndex).x,
                        playerMoveCardsLocations.get(cardNumberIndex).y);
                moveCards.add(moveCard);
                stage.addActor(moveCard);
                cardNumberIndex += 1;
            }
        }
    }

    public void UpdateCardLocations(){
        //for each set of movesets in manager, find matching moveset and send to right location
        Gdx.app.log("MoveSelectCards", "Updating Card Locations");
        UpdateCardLocationsReferences();
        int enemyCardIndex = 0;
        for (MoveSet moveSet : gameManager.enemyMoves){
            for (MoveCard moveCard : moveCards){
                if (moveCard.moveSet == moveSet){
                    moveCard.JumpTo(enemyMoveCardsLocations.get(enemyCardIndex), 0.35f);
                    moveCard.setUnselectable();
                    enemyCardIndex += 1;
                }
            }
        }
        int freeCardIndex = 0;
        for (MoveSet moveSet : gameManager.freeMove){
            for (MoveCard moveCard : moveCards){
                if (moveCard.moveSet == moveSet){
                    moveCard.JumpTo(freeMoveCardsLocations.get(freeCardIndex), 0.3f);
                    moveCard.setUnselectable();
                    freeCardIndex += 1;
                }
            }
        }
        int playerCardIndex = 0;
        for (MoveSet moveSet : gameManager.playerMoves){
            for (MoveCard moveCard : moveCards){
                if (moveCard.moveSet == moveSet){
                    moveCard.JumpTo(playerMoveCardsLocations.get(playerCardIndex), 0.28f);
                    moveCard.setSelectable();
                    playerCardIndex += 1;
                }
            }
        }
        this.SetCardsVisibility(true);
    }

    public void SetCardsVisibility(boolean bool){
        for ( MoveCard moveCard : moveCards ) {
            moveCard.setVisible(bool);
        }
        enemyChemicalLabel.setVisible(bool);
        freeChemicalLabel.setVisible(bool);
        playerChemicalLabel.setVisible(bool);
    }

    private void UpdateCardLocationsReferences(){
        Gdx.app.log("MoveSelectCards", "Calculating new locations for cards");
        CalculateCardLocationsForRow(this.gameManager.enemyMoves, enemyMoveCardsLocations, MoveCardLocations.ENEMY_CHEMICAL_LOCATION_Y);
        CalculateCardLocationsForRow(this.gameManager.freeMove, freeMoveCardsLocations, MoveCardLocations.FREE_CHEMICAL_LOCATION_Y);
        CalculateCardLocationsForRow(this.gameManager.playerMoves, playerMoveCardsLocations, MoveCardLocations.PLAYER_CHEMICAL_LOCATION_Y);
    }

    private void CalculateCardLocationsForRow(List<MoveSet> moveSetList, List<Vector2> targetLocationList, float yLocationForRow){
        //given X amounts of movesets in enemy moves, free moves, or my moves, get x and y location for movecards
        if (!moveSetList.isEmpty()){
            int moveCardNumber = moveSetList.size();
            float moveCardSegmentWidth = MoveCardLocations.CHEMICAL_CARDS_WIDTH/ moveCardNumber;
            int cardNumberIndex = 0;
            for (MoveSet moveSet : moveSetList) {
                Vector2 location = new Vector2(
                        MoveCardLocations.CHEMICAL_START_LOCATION_X + (moveCardSegmentWidth * cardNumberIndex) + moveCardSegmentWidth/2 - MoveCardLocations.CARD_WIDTH/2,
                        yLocationForRow);
                cardNumberIndex += 1;
                targetLocationList.add(location);
            }
        }
    }

}

