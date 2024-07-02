package com.mygdx.game.BoardUI;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.Constants;

public class MoveSelectCards extends Table {
    GameManager gameManager;
    Skin moveSelectSkin = new Skin(Gdx.files.internal("buttons/uiskin.json"));
    public MoveSelectCards(GameManager gameManager) {
        this.setSkin(moveSelectSkin);
        this.setDebug(true);
        this.defaults().align(Align.center);
        this.add("their chemicals").colspan(5).align(Align.center);
        this.row();
        for (MoveSet moveSet : gameManager.enemyMoves) {
            this.add(new MoveCard(moveSelectSkin, moveSet, this.gameManager)).expand().fill();
        }
        this.row();
        this.add("free chemicals").colspan(5).align(Align.center);
        this.row();
        for (MoveSet moveSet : gameManager.freeMoves) {
            this.add(new MoveCard(moveSelectSkin, moveSet, this.gameManager)).expand().fill();
        }
        this.row();
        this.add("my chemicals").colspan(5).align(Align.center);
        this.row();
        for (MoveSet moveSet : gameManager.myMoves) {
            this.add(new MoveCard(moveSelectSkin, moveSet, this.gameManager)).expand().fill();
        }
        this.setBounds(Constants.SCREEN_WIDTH *0.025f, 0, Constants.SCREEN_WIDTH*0.25f, Constants.SCREEN_HEIGHT);
        this.defaults().padRight(10); // All cells have a padding of 10px to the right
        this.setName("MoveSelectCards");
        this.gameManager = gameManager;
    }

    }

