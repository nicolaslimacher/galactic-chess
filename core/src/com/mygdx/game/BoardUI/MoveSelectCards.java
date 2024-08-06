package com.mygdx.game.BoardUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.MoveSets.MoveSet;

import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

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
    Collection<MoveCard> MoveCards

    public MoveSelectCards(GameManager gameManager) {
        this.gameManager = gameManager;

        enemyChemicalLabel = new Label("Enemy Chemicals" ,chemicalLabelSkin);
        enemyChemicalLabel.setPosition(MoveCardLocations.CHEMICAL_START_LOCATION_X,
                (MoveCardLocations.ENEMY_CHEMICAL_LOCATION_Y + MoveCardLocations.CHEMICAL_CARDS_HEIGHT));
        enemyChemicalLabel.setSize(MoveCardLocations.CHEMICAL_CARDS_WIDTH,
                MoveCardLocations.CHEMICAL_LABEL_HEIGHT);
        gameManager.getStage().addActor(enemyChemicalLabel);

        freeChemicalLabel = new Label("Free Chemicals" ,chemicalLabelSkin);
        freeChemicalLabel.setPosition(MoveCardLocations.CHEMICAL_START_LOCATION_X,
                (MoveCardLocations.FREE_CHEMICAL_LOCATION_Y + MoveCardLocations.CHEMICAL_CARDS_HEIGHT));
        freeChemicalLabel.setSize(MoveCardLocations.CHEMICAL_CARDS_WIDTH,
                MoveCardLocations.CHEMICAL_LABEL_HEIGHT);
        gameManager.getStage().addActor(freeChemicalLabel);

        myChemicalLabel = new Label("My Chemicals" ,chemicalLabelSkin);
        freeChemicalLabel.setPosition(MoveCardLocations.CHEMICAL_START_LOCATION_X,
                (MoveCardLocations.PLAYER_CHEMICAL_LOCATION_Y + MoveCardLocations.CHEMICAL_CARDS_HEIGHT));
        freeChemicalLabel.setSize(MoveCardLocations.CHEMICAL_CARDS_WIDTH,
                MoveCardLocations.CHEMICAL_LABEL_HEIGHT);
        gameManager.getStage().addActor(myChemicalLabel);


        CreateCards();
        // All cells have a padding of 10px to the right
        this.setName("MoveSelectCards");
    }

    public void CreateCards (){

        if (!this.gameManager.enemyMoves.isEmpty()){
        for (MoveSet moveSet : this.gameManager.enemyMoves) {
            MoveCard moveCard = new MoveCard(chemicalLabelSkin, moveSet, this.gameManager, false);
        }}

        if (!this.gameManager.freeMove.isEmpty()){
        for (MoveSet moveSet : this.gameManager.freeMove) {
            MoveCard moveCard = new MoveCard(chemicalLabelSkin, moveSet, this.gameManager, false);

        }}

        if (!this.gameManager.freeMove.isEmpty()){
        for (MoveSet moveSet : this.gameManager.myMoves) {
            MoveCard moveCard = new MoveCard(chemicalLabelSkin, moveSet, this.gameManager, true);
        }}
    }

    public void UpdateCardLocations(){

    }


}

