package com.mygdx.game.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Manager.BattleManager;
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
    BattleManager battleManager;
    Skin chemicalLabelSkin = new Skin(Gdx.files.internal("skins/uiskin.json"));
    Label enemyChemicalLabel, freeChemicalLabel, playerChemicalLabel;
    ArrayList<MoveCard> moveCards;
    ArrayList<Vector2> enemyMoveCardsLocations  = new ArrayList<>(2);
    ArrayList<Vector2> freeMoveCardsLocations= new ArrayList<>(1);
    ArrayList<Vector2> playerMoveCardsLocations = new ArrayList<>(2);

    public MoveSelectCards(BattleManager battleManager, Stage stage) {
        this.battleManager = battleManager;
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

        if (!this.battleManager.enemyMoves.isEmpty()){
            int cardNumberIndex = 0;
            for (MoveSet moveSet : this.battleManager.enemyMoves) {
                MoveCard moveCard = new MoveCard(moveSet,
                        this.battleManager,
                        false,
                        enemyMoveCardsLocations.get(cardNumberIndex).x,
                        enemyMoveCardsLocations.get(cardNumberIndex).y);
                moveCards.add(moveCard);
                stage.addActor(moveCard);
                cardNumberIndex += 1;
            }
        }

        if (!this.battleManager.freeMove.isEmpty()){
            int enemyMoveCardNumber = battleManager.freeMove.size();
            float moveCardSegmentWidth = MoveCardLocations.CHEMICAL_CARDS_WIDTH/enemyMoveCardNumber;
            int cardNumberIndex = 0;
            for (MoveSet moveSet : this.battleManager.freeMove) {
                MoveCard moveCard = new MoveCard(moveSet,
                        this.battleManager,
                        false,
                        freeMoveCardsLocations.get(cardNumberIndex).x,
                        freeMoveCardsLocations.get(cardNumberIndex).y);
                moveCards.add(moveCard);
                stage.addActor(moveCard);
                cardNumberIndex += 1;
            }
        }

        if (!this.battleManager.playerMoves.isEmpty()){
            int cardNumberIndex = 0;
            for (MoveSet moveSet : this.battleManager.playerMoves) {
                MoveCard moveCard = new MoveCard(moveSet,
                        this.battleManager,
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
        for (MoveSet moveSet : battleManager.enemyMoves){
            for (MoveCard moveCard : moveCards){
                if (moveCard.moveSet == moveSet){
                    moveCard.jumpTo(enemyMoveCardsLocations.get(enemyCardIndex), 0.35f);
                    moveCard.setUnselectable();
                    enemyCardIndex += 1;
                }
            }
        }
        int freeCardIndex = 0;
        for (MoveSet moveSet : battleManager.freeMove){
            for (MoveCard moveCard : moveCards){
                if (moveCard.moveSet == moveSet){
                    moveCard.jumpTo(freeMoveCardsLocations.get(freeCardIndex), 0.3f);
                    moveCard.setUnselectable();
                    freeCardIndex += 1;
                }
            }
        }
        int playerCardIndex = 0;
        for (MoveSet moveSet : battleManager.playerMoves){
            for (MoveCard moveCard : moveCards){
                if (moveCard.moveSet == moveSet){
                    moveCard.jumpTo(playerMoveCardsLocations.get(playerCardIndex), 0.28f);
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
        CalculateCardLocationsForRow(this.battleManager.enemyMoves, enemyMoveCardsLocations, MoveCardLocations.ENEMY_CHEMICAL_LOCATION_Y);
        CalculateCardLocationsForRow(this.battleManager.freeMove, freeMoveCardsLocations, MoveCardLocations.FREE_CHEMICAL_LOCATION_Y);
        CalculateCardLocationsForRow(this.battleManager.playerMoves, playerMoveCardsLocations, MoveCardLocations.PLAYER_CHEMICAL_LOCATION_Y);
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

