package com.mygdx.game.BoardUI;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.MoveSets.MoveSet;

public class MoveCard extends Button {
        public MoveSet moveSet;
        public GameManager gameManager;
        private boolean selectable;
        public MoveCard(Skin skin, MoveSet moveSet, GameManager gameManager, boolean selectable) {
            super(skin);
            this.moveSet = moveSet;
            this.gameManager = gameManager;
            this.selectable = selectable;
            Label textLabel = new Label(moveSet.symbol + "\n" + moveSet.name, skin);
            textLabel.setWrap(true);
            textLabel.setAlignment(Align.center);
            this.add(textLabel).expand().fill();
            if (this.selectable) {
                this.addListener(MoveSelectButtonListener);
            }else{
                this.addListener(MoveInfoButtonListener);
            }
            this.setHeight(150f);
            this.setWidth(70f);
        }

        private final InputListener MoveInfoButtonListener = new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                MoveCard thisButton = (MoveCard) event.getListenerActor();
                GameManager gameManager = event.getStage().getRoot().findActor("GameManager");
                thisButton.getParent().setVisible(false); //make select menu non-visible
                //create new confirmation menu
                MoveConfirmation moveConfirmationMenu = new MoveConfirmation(thisButton.gameManager);
                thisButton.getStage().addActor(moveConfirmationMenu);
                gameManager.moveConfirmation = moveConfirmationMenu;
                gameManager.moveConfirmation.AddConfirmationButton(thisButton.moveSet);
                return true;
            }
        };

    private final InputListener MoveSelectButtonListener = new InputListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            MoveCard thisButton = (MoveCard) event.getListenerActor();
            GameManager gameManager = event.getStage().getRoot().findActor("GameManager");
            gameManager.selectedMoveSet = thisButton.moveSet; //make this move active allowed moveset
            thisButton.getParent().setVisible(false); //make select menu non-visible
            //create new confirmation menu
            MoveConfirmation moveConfirmationMenu = new MoveConfirmation(thisButton.gameManager);
            thisButton.getStage().addActor(moveConfirmationMenu);
            gameManager.moveConfirmation = moveConfirmationMenu;
            gameManager.moveConfirmation.AddConfirmationButton(thisButton.moveSet);
            return true;
        }
    };



        public void setSelectable(){
            if (!this.selectable) {
                this.removeListener(MoveInfoButtonListener);
                this.addListener(MoveSelectButtonListener);
            }
            this.selectable = true;
        }
        public void setUnselectable(){
            if (this.selectable) {
                this.removeListener(MoveSelectButtonListener);
                this.addListener(MoveInfoButtonListener);
            }
            this.selectable = false;
        }
    }