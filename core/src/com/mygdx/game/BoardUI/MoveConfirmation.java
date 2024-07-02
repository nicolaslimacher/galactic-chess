package com.mygdx.game.BoardUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.Constants;

public class MoveConfirmation extends Table {
    GameManager gameManager;
    Skin moveSelectSkin = new Skin(Gdx.files.internal("buttons/uiskin.json"));
    public MoveConfirmation(GameManager gameManager) {
        this.setBounds(Constants.SCREEN_WIDTH*0.025f, 0, Constants.SCREEN_WIDTH*0.25f, Constants.SCREEN_HEIGHT);
        this.gameManager = gameManager;
        this.setVisible(false);
        this.setName("MoveConfirmationMenu");
    }

    public void AddConfirmationButton (MoveSet moveSet) {
        this.setVisible(true);
        this.row();
        this.add(NewMoveConfirmationButton(moveSelectSkin, moveSet)).size(this.getWidth() * 0.8f, this.getHeight() * 0.8f);
    }

    private Button NewMoveConfirmationButton(Skin skin, MoveSet moveSet){
        Button button = new Button(skin);
        Table confirmationName = new Table();
        confirmationName.defaults().padTop(5f);

        Label moveSymbol = new Label(moveSet.symbol, skin);
        moveSymbol.setFontScale(1.2f);
        moveSymbol.setAlignment(Align.center);
        confirmationName.add(moveSymbol).width(50).center();

        Label buttonText = new Label(moveSet.name, skin);
        buttonText.setWrap(true);
        buttonText.setAlignment(Align.center);
        confirmationName.add(buttonText).expandX().prefWidth(400);
        confirmationName.align(Align.topLeft);

        button.add(confirmationName).fill().expand();
        confirmationName.row();
        Label cancelText = new Label("Click here to cancel", skin, "small");
        buttonText.setAlignment(Align.center);
        confirmationName.add(cancelText).colspan(2).padTop(5f);
        confirmationName.row();
        PossibleMoveImageCreator possibleMoveImage = new PossibleMoveImageCreator(moveSet);
        possibleMoveImage.setScale(0.73f);
        confirmationName.add(possibleMoveImage).colspan(2).expand().left().padLeft(2f);

        this.addListener(MoveConfirmationCancelButtonListener);
        return button;
    }

    private final ClickListener MoveConfirmationCancelButtonListener = new ClickListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            MoveConfirmation moveConfirmation = (MoveConfirmation) event.getListenerActor();
            GameManager gameManager = event.getStage().getRoot().findActor("GameManager");
            gameManager.selectedMoveSet = null;
            moveConfirmation.getStage().getRoot().findActor("MoveSelectCards").setVisible(true);
            moveConfirmation.remove();
            //gameManager.menuTable = null; why did i add this before?
            return true;
        }
    };
}
