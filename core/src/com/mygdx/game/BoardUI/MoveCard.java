package com.mygdx.game.BoardUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.MoveSets.MoveSet;

public class MoveCard extends Actor {
    public MoveSet moveSet;
    public GameManager gameManager;
    private boolean selectable;
    TextureRegion textureRegionBackground;
    Label moveSymbolLabel, moveNameLabel;
    Skin moveSelectSkin;
    PossibleMoveImageCreator possibleMoveImage;
    public MoveCard(MoveSet moveSet, GameManager gameManager, boolean selectable, float x, float y) {
        this.moveSet = moveSet;
        this.gameManager = gameManager;
        this.selectable = selectable;

        //create text labels
        moveSelectSkin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        moveSymbolLabel = new Label(moveSet.symbol, moveSelectSkin);
        moveSymbolLabel.setAlignment(Align.center);
        moveNameLabel = new Label(moveSet.name, moveSelectSkin);
        moveNameLabel.setAlignment(Align.center);
        this.addListener(MoveSelectButtonListener);
        this.setBounds(x, y,70f, 150f);
        this.setWidth(70f);
    }

    private final InputListener MoveSelectButtonListener = new InputListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            MoveCard thisMoveCard = (MoveCard) event.getListenerActor();
            GameManager gameManager = event.getStage().getRoot().findActor("GameManager");
            if(thisMoveCard.selectable)
                gameManager.selectedMoveSet = thisMoveCard.moveSet; //make this move active allowed moveset if selectable

            thisMoveCard.getParent().setVisible(false); //make select menu non-visible
            //create new confirmation menu
            MoveConfirmation moveConfirmationMenu = new MoveConfirmation(thisMoveCard.gameManager, thisMoveCard.moveSet);
            thisMoveCard.getStage().addActor(moveConfirmationMenu);

            gameManager.moveConfirmation = moveConfirmationMenu;
            return true;
        }
    };

    public void setSelectable(){
        this.selectable = true;
    }
    public void setUnselectable() {
        this.selectable = false;
    }


}
