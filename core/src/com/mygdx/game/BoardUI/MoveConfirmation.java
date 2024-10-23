package com.mygdx.game.BoardUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.MoveSets.MoveSet;

/**
 * Displays a selected move card with more info and allows it to be played
 *
 * @author Nico Limacher
 * @since 1.0
 */

public class MoveConfirmation extends Actor {
    GameManager gameManager;
    TextureRegion textureRegion;
    private final MoveSet moveSet;
    Label moveSymbol, moveName, cancelText;
    Skin moveConfirmationSkin;
    PossibleMoveImageCreator possibleMoveImage;

    public MoveConfirmation(GameManager gameManager, MoveSet moveSet) {
        moveConfirmationSkin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        Texture bottleImage = new Texture(Gdx.files.internal("bottle.png"));
        this.textureRegion = new TextureRegion(bottleImage, 480, 1000);
        this.setWidth(MoveCardLocations.CHEMICAL_CARDS_WIDTH);
        this.setHeight(MoveCardLocations.ALL_CHEMICAL_TOP - MoveCardLocations.ALL_CHEMICAL_BOTTOM);
        this.setPosition(MoveCardLocations.CHEMICAL_START_LOCATION_X, MoveCardLocations.ALL_CHEMICAL_BOTTOM);
        this.gameManager = gameManager;
        this.moveSet = moveSet;
        this.setBounds(getX(), getY(), getWidth(), getHeight());
        this.setName("MoveConfirmationMenu");

        gameManager.getStage().addActor(this);

        moveSymbol = new Label(moveSet.symbol, moveConfirmationSkin, "moveCardConfirmation");
        moveSymbol.setFontScale(0.55f);
        moveSymbol.setAlignment(Align.center);
        moveSymbol.setTouchable(Touchable.disabled);
        this.getStage().addActor(moveSymbol);

        moveName = new Label(moveSet.name, moveConfirmationSkin, "moveCardConfirmation");
        moveName.setFontScale(0.45f);
        moveName.setWrap(true);
        moveName.setAlignment(Align.center);
        moveName.setTouchable(Touchable.disabled);

        this.getStage().addActor(moveName);

        cancelText = new Label("Click here to cancel", moveConfirmationSkin, "small");
        cancelText.setAlignment(Align.center);
        cancelText.setTouchable(Touchable.disabled);
        this.getStage().addActor(cancelText);

        possibleMoveImage = new PossibleMoveImageCreator(moveSet);
        possibleMoveImage.setScale(0.73f);
        possibleMoveImage.setTouchable(Touchable.disabled);
        this.getStage().addActor(possibleMoveImage);

        LocateTextAndImages();

        this.addListener(MoveConfirmationCancelListener);
    }

    private void LocateTextAndImages(){

        this.moveName.setDebug(false);
        this.moveName.setPosition(this.getX()+(this.getWidth() * 0.3f), this.getY()+(this.getHeight() * 0.55f));
        this.moveName.setSize(this.getWidth() * 0.7f, this.getHeight() * 0.2f);
        this.moveName.setAlignment(Align.center);

        this.moveSymbol.setDebug(false);
        this.moveSymbol.setPosition(this.getX(), this.getY()+(this.getHeight() * 0.55f));
        this.moveSymbol.setSize(this.getWidth() * 0.3f, this.getHeight() * 0.2f);
        this.moveSymbol.setAlignment(Align.center);

        this.cancelText.setDebug(false);
        this.cancelText.setPosition(this.getX(), this.getY()+(this.getHeight() * 0.45f));
        this.cancelText.setSize(this.getWidth(), this.getHeight() * 0.1f);
        this.cancelText.setAlignment(Align.center);

        this.possibleMoveImage.setDebug(false);
        this.possibleMoveImage.setScale(0.79f);
        possibleMoveImage.setPosition(this.getX()+(this.getWidth()/2 - 120f*0.79f), this.getY()+(this.getHeight() * 0.10f));
    }

    private final ClickListener MoveConfirmationCancelListener = new ClickListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            MoveConfirmation moveConfirmation = (MoveConfirmation) event.getListenerActor();
            moveConfirmation.ReturnToMoveSelectCards();
            return true;
        }
    };

    @Override
    public void draw(Batch batch, float parentAlpha){
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, 1f);
        batch.draw(textureRegion, getX(), getY(), getWidth(), getHeight());
    }

    public void ReturnToMoveSelectCards(){
        if (gameManager.selectedMoveSet == this.moveSet) //remove selected moveSet IF this was for selected move
            gameManager.selectedMoveSet = null;
        gameManager.moveSelectCards.SetCardsVisibility(true);

        //remove text over
        this.moveName.remove();
        this.moveSymbol.remove();
        this.possibleMoveImage.remove();
        this.cancelText.remove();
        this.remove();
    }
}
