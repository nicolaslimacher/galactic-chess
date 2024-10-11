package com.mygdx.game.BoardUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.MoveSets.MoveSet;
import java.util.ArrayList;

/**
 * Displays cards for moves in Game Screen
 *
 * @author Nico Limacher
 * @since 1.0
 */

public class MoveSelectCards extends Actor {
    GameManager gameManager;
    Skin chemicalLabelSkin = new Skin(Gdx.files.internal("skins/uiskin.json"));
    Label enemyChemicalLabel, freeChemicalLabel, myChemicalLabel;
    ArrayList<MoveCard> moveCards;

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

        myChemicalLabel = new Label("My Chemicals" ,chemicalLabelSkin);
        myChemicalLabel.setPosition(MoveCardLocations.CHEMICAL_START_LOCATION_X,
                (MoveCardLocations.PLAYER_CHEMICAL_LOCATION_Y + MoveCardLocations.CHEMICAL_CARDS_HEIGHT));
        myChemicalLabel.setSize(MoveCardLocations.CHEMICAL_CARDS_WIDTH,
                MoveCardLocations.CHEMICAL_LABEL_HEIGHT);
        myChemicalLabel.setAlignment(Align.center);
        stage.addActor(myChemicalLabel);


        CreateCards(stage);
        this.setName("MoveSelectCards");
    }

    public void CreateCards (Stage stage){

        if (!this.gameManager.enemyMoves.isEmpty()){
            int enemyMoveCardNumber = gameManager.enemyMoves.size();
            float moveCardSegmentWidth = MoveCardLocations.CHEMICAL_CARDS_WIDTH/enemyMoveCardNumber;
            int cardNumberIndex = 0;
            for (MoveSet moveSet : this.gameManager.enemyMoves) {
                MoveCard moveCard = new MoveCard(moveSet,
                        this.gameManager,
                        false,
                        MoveCardLocations.CHEMICAL_START_LOCATION_X + (moveCardSegmentWidth * cardNumberIndex) + moveCardSegmentWidth/2 - MoveCardLocations.CARD_WIDTH/2,
                        MoveCardLocations.ENEMY_CHEMICAL_LOCATION_Y);
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
                        MoveCardLocations.CHEMICAL_START_LOCATION_X + (moveCardSegmentWidth * cardNumberIndex) + moveCardSegmentWidth/2 - MoveCardLocations.CARD_WIDTH/2,
                        MoveCardLocations.FREE_CHEMICAL_LOCATION_Y);
                moveCards.add(moveCard);
                stage.addActor(moveCard);
                cardNumberIndex += 1;
            }
        }

        if (!this.gameManager.myMoves.isEmpty()){
            int enemyMoveCardNumber = gameManager.myMoves.size();
            float moveCardSegmentWidth = MoveCardLocations.CHEMICAL_CARDS_WIDTH/enemyMoveCardNumber;
            int cardNumberIndex = 0;
            for (MoveSet moveSet : this.gameManager.myMoves) {
                MoveCard moveCard = new MoveCard(moveSet,
                        this.gameManager,
                        false,
                        MoveCardLocations.CHEMICAL_START_LOCATION_X + (moveCardSegmentWidth * cardNumberIndex) + moveCardSegmentWidth/2 - MoveCardLocations.CARD_WIDTH/2,
                        MoveCardLocations.PLAYER_CHEMICAL_LOCATION_Y);
                moveCards.add(moveCard);
                stage.addActor(moveCard);
                cardNumberIndex += 1;
            }
        }
    }

    public void UpdateCardLocations(){

    }

    public void SetCardsVisibility(boolean bool){
        for ( MoveCard moveCard : moveCards ) {
            moveCard.setVisible(bool);
        }
    }


}

