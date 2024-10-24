package com.mygdx.game.BoardUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Actions.ArcToAction;
import com.mygdx.game.Board.BoardTile;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.GamePiece.LandingClouds;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.IntPair;

public class MoveCard extends Actor {
    public MoveSet moveSet;
    public GameManager gameManager;
    private boolean selectable;
    TextureRegion textureRegion;
    Label moveSymbolLabel, moveNameLabel;
    Skin moveSelectSkin;
    PossibleMoveImageCreator possibleMoveImage;
    public MoveCard(MoveSet moveSet, GameManager gameManager, boolean selectable, float x, float y) {
        this.moveSet = moveSet;
        this.gameManager = gameManager;
        this.selectable = selectable;
        Texture texture = new Texture(Gdx.files.internal("moveCardBackground.png"));
        textureRegion = new TextureRegion(texture, (int) MoveCardLocations.CARD_WIDTH, (int) MoveCardLocations.CHEMICAL_CARDS_HEIGHT);
        this.setBounds(textureRegion.getRegionX(), textureRegion.getRegionY(),
                textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        this.setPosition(x, y);
        Gdx.app.log("MoveCard", "MoveCard created, name: " + this.moveSet.name + ", selectable? : " + this.selectable + ", position: " + this.getX() + "," + this.getY());

        //create text labels
        moveSelectSkin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        moveSymbolLabel = new Label(moveSet.symbol, moveSelectSkin, "moveCardSelect");
        moveSymbolLabel.setFontScale(0.65f);
        moveSymbolLabel.setDebug(true);
        moveNameLabel = new Label(moveSet.name, moveSelectSkin, "moveCardSelect");
        moveNameLabel.setFontScale(0.40f);
        moveNameLabel.setDebug(true);
        this.addListener(MoveSelectButtonListener);
    }

    private final InputListener MoveSelectButtonListener = new InputListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            MoveCard thisMoveCard = (MoveCard) event.getListenerActor();
            GameManager gameManager = event.getStage().getRoot().findActor("GameManager");
            if(thisMoveCard.selectable) {
                gameManager.selectedMoveSet = thisMoveCard.moveSet; //make this move active allowed moveset if selectable
                Gdx.app.log("MoveCard", "move card selected, Game Manager selected moveset: " + gameManager.selectedMoveSet.name);
            }
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
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        //sprite.draw(batch, parentAlpha);
        //draw labels
        if (moveNameLabel != null && moveSymbolLabel != null) {
            SetLabelPositions();
            this.moveNameLabel.draw(batch, parentAlpha);
            this.moveSymbolLabel.draw(batch, parentAlpha);
        }
    }

    private void SetLabelPositions (){
        moveNameLabel.setPosition(this.getX() + 30f/75f*this.getWidth(),
                this.getY() + 125f/150f*this.getHeight(),
                Align.left);
        moveSymbolLabel.setPosition(this.getX(),
                this.getY() + 125f/150f*this.getHeight(),
                Align.left);
    }

    public void JumpTo(Vector2 newPosition, float jumpDelay) {
        Gdx.app.log("MoveCard", "MoveCard " + this.moveSet.symbol + " is moving to " + newPosition.x + "," + newPosition.y + ".");

        //movement action (and undo squish)
        MoveToAction arcMove = new MoveToAction();
        arcMove.setPosition(newPosition.x, newPosition.y);
        arcMove.setDuration(1.5f);
        arcMove.setInterpolation(Interpolation.exp10);


        //add clouds after movement
        SequenceAction jumpTo = new SequenceAction(Actions.delay(jumpDelay), arcMove);
        this.addAction(jumpTo);
        this.toFront();
    }


}
