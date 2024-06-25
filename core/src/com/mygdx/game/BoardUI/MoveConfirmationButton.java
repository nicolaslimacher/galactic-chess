package com.mygdx.game.BoardUI;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Board.Board;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.MoveSets.MoveSet;

public class MoveConfirmationButton extends Button {
    Board board;

    public MoveConfirmationButton(MoveSet moveSet, Skin skin, Board board) {
        super(skin);
        this.board = board;
        Table confirmationName = new Table();
        confirmationName.defaults().padTop(5f);
        Label moveSymbol = new Label(moveSet.symbol, this.getSkin());
        moveSymbol.setFontScale(1.2f);
        moveSymbol.setAlignment(Align.center);
        Label buttonText = new Label(moveSet.name, this.getSkin());
        buttonText.setWrap(true);
        buttonText.setAlignment(Align.center);
        confirmationName.add(moveSymbol).width(50).center();
        confirmationName.add(buttonText).expandX().prefWidth(400);
        confirmationName.align(Align.topLeft);
        this.add(confirmationName).fill().expand();
        confirmationName.row();
        Label cancelText = new Label("Click here to cancel", this.getSkin(), "small");
        buttonText.setAlignment(Align.center);
        confirmationName.add(cancelText).colspan(2).padTop(5f);
        confirmationName.row();
        PossibleMoveImageCreator possibleMoveImage = new PossibleMoveImageCreator(moveSet);
        possibleMoveImage.setScale(0.73f);
        confirmationName.add(possibleMoveImage).colspan(2).expand().left().padLeft(2f);

        this.addListener(MoveConfirmationCancelButtonListener);
    }
    private final ClickListener MoveConfirmationCancelButtonListener = new ClickListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            MoveConfirmationButton thisButton = (MoveConfirmationButton) event.getListenerActor();
            GameManager gameManager = event.getStage().getRoot().findActor("GameManager");
            gameManager.selectedMoveSet = null;
            thisButton.getStage().getRoot().findActor("MoveSelectButtonMenu").setVisible(true);
            thisButton.getParent().remove();
            thisButton.board.menuTable = null;

            return true;
        }
    };
}
