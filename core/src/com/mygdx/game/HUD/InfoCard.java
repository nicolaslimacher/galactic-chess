package com.mygdx.game.HUD;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.Manager.GameManager;
import com.mygdx.game.MoveSets.MoveSet;

public class InfoCard extends TextButton{
        public MoveSet moveSet;
        public GameManager gameManager;
        public InfoCard(Skin skin, MoveSet moveSet, GameManager gameManager) {
            super("", skin);
            this.moveSet = moveSet;
            this.gameManager = gameManager;
            this.setText(moveSet.name);
            this.addListener(MoveSelectButtonListener);
        }
        private final InputListener MoveSelectButtonListener = new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                InfoCard thisButton = (InfoCard) event.getListenerActor();
                GameManager gameManager = event.getStage().getRoot().findActor("GameManager");
                gameManager.selectedMoveSet = thisButton.moveSet; //make this move active allowed moveset
                thisButton.getParent().setVisible(false); //make select menu non-visible
                //create new confirmation menu
                MoveConfirmation moveConfirmationMenu = new MoveConfirmation(thisButton.gameManager, thisButton.moveSet);
                thisButton.getStage().addActor(moveConfirmationMenu);
                gameManager.moveConfirmation = moveConfirmationMenu;
                //gameManager.moveConfirmation.AddConfirmationButton(thisButton.moveSet);
                return true;
            }
        };
    }