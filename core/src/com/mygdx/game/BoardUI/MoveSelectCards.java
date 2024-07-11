package com.mygdx.game.BoardUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.Constants;

public class MoveSelectCards extends Table {
    GameManager gameManager;
    Skin moveSelectSkin = new Skin(Gdx.files.internal("buttons/uiskin.json"));
    public MoveSelectCards(GameManager gameManager) {
        this.setSkin(moveSelectSkin);
        //this.setDebug(true);
        this.defaults().align(Align.center);
        this.gameManager = gameManager;

        UpdateCards();

        this.setBounds(Constants.SCREEN_WIDTH *0.025f, 0, Constants.SCREEN_WIDTH*0.25f, Constants.SCREEN_HEIGHT);
        this.defaults().padRight(10); // All cells have a padding of 10px to the right
        this.setName("MoveSelectCards");
    }

    public void UpdateCards (){
        this.clearChildren();

        this.add("their chemicals").colspan(5).align(Align.center);
        this.row();
        for (MoveSet moveSet : this.gameManager.enemyMoves) {
            MoveCard moveCard = new MoveCard(moveSelectSkin, moveSet, this.gameManager, false);
            this.add(moveCard).prefWidth(75).prefHeight(150);
        }
        this.row();

        this.add("free chemicals").colspan(5).align(Align.center);
        this.row();
        for (MoveSet moveSet : this.gameManager.freeMove) {
            MoveCard moveCard = new MoveCard(moveSelectSkin, moveSet, this.gameManager, false);
            this.add(moveCard).prefWidth(75).prefHeight(150).colspan(2);
        }
        this.row();

        this.add("my chemicals").colspan(5).align(Align.center);
        this.row();
        for (MoveSet moveSet : this.gameManager.myMoves) {
            MoveCard moveCard = new MoveCard(moveSelectSkin, moveSet, this.gameManager, true);
            this.add(moveCard).prefWidth(75).prefHeight(150);
        }
    }
}

