package com.mygdx.game.BoardUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.MoveSets.MoveSet;

public class MoveCard extends Actor {
    public MoveSet moveSet;
    public GameManager gameManager;
    private boolean selectable;
    Sprite sprite;
    Label moveSymbolLabel, moveNameLabel;
    Skin moveSelectSkin;
    PossibleMoveImageCreator possibleMoveImage;
    public MoveCard(MoveSet moveSet, GameManager gameManager, boolean selectable, float x, float y) {
        this.moveSet = moveSet;
        this.gameManager = gameManager;
        this.selectable = selectable;
        sprite = new Sprite(new Texture(Gdx.files.internal("moveCardBackground.png")), (int) MoveCardLocations.CARD_WIDTH, 150);
        sprite.setScale(0.75f);
        sprite.setPosition(x, y);

        //create text labels
        moveSelectSkin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        moveSymbolLabel = new Label(moveSet.symbol, moveSelectSkin, "moveCardSelect");
        moveSymbolLabel.setFontScale(0.65f);
        moveSymbolLabel.setDebug(true);
        moveNameLabel = new Label(moveSet.name, moveSelectSkin, "moveCardSelect");
        moveNameLabel.setFontScale(0.40f);
        moveNameLabel.setDebug(true);
        this.addListener(MoveSelectButtonListener);
        this.setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        //this.setWidth(70f);
    }

    private final InputListener MoveSelectButtonListener = new InputListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            MoveCard thisMoveCard = (MoveCard) event.getListenerActor();
            GameManager gameManager = event.getStage().getRoot().findActor("GameManager");
            if(thisMoveCard.selectable)
                gameManager.selectedMoveSet = thisMoveCard.moveSet; //make this move active allowed moveset if selectable

            gameManager.moveSelectCards.SetCardsVisibility(false); //make select menu non-visible
            //create new confirmation menu
            MoveConfirmation moveConfirmation = new MoveConfirmation(thisMoveCard.gameManager, thisMoveCard.moveSet);
            thisMoveCard.getStage().addActor(moveConfirmation);

            gameManager.moveConfirmation = moveConfirmation;
            return true;
        }
    };

    public void setSelectable(){
        this.selectable = true;
    }
    public void setUnselectable() {
        this.selectable = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, 1f);
        //batch.draw(sprite, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        sprite.draw(batch, parentAlpha);
        //draw labels
        if (moveNameLabel != null && moveSymbolLabel != null) {
            SetLabelPositions();
            this.moveNameLabel.draw(batch, parentAlpha);
            this.moveSymbolLabel.draw(batch, parentAlpha);
        }
    }

    private void SetLabelPositions (){
//        moveNameLabel.setBounds(sprite.getX(),
//                sprite.getY() + 125f/150f*sprite.getHeight(),
//                30f/75f*sprite.getWidth(),
//                25f/150f*sprite.getHeight());
        moveNameLabel.setPosition(sprite.getX() + 30f/75f*sprite.getWidth(),
                sprite.getY() + 125f/150f*sprite.getHeight(),
                Align.left);
//        moveSymbolLabel.setBounds(sprite.getX() + 30f/75f*sprite.getWidth(),
//                sprite.getY() + 125f/150f*sprite.getHeight(),
//                30f/75f*sprite.getWidth(),
//                25f/150f*sprite.getHeight());
        moveSymbolLabel.setPosition(sprite.getX(),
                sprite.getY() + 125f/150f*sprite.getHeight(),
                Align.left);
    }


}
