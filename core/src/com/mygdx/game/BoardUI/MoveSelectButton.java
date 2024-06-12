package com.mygdx.game.BoardUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.Board.Board;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Pawn.Pawn;
import com.mygdx.game.Pawn.PossiblePawnMove;

public class MoveSelectButton extends TextButton {
    public MoveSet moveSet;
    public Board board;
    public MoveSelectButton(Skin skin, MoveSet moveSet, Board board) {
        super("", skin);
        this.moveSet = moveSet;
        this.board = board;
        this.setText(moveSet.name);
        this.addListener(MoveSelectButtonListener);
    }
    private final ClickListener MoveSelectButtonListener = new ClickListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            MoveSelectButton thisButton = (MoveSelectButton) event.getListenerActor();
            thisButton.board.selectedMoveSet = thisButton.moveSet; //make this move active allowed moveset
            thisButton.getParent().setVisible(false); //make select menu non-visible
            //create new confirmation menu
            MoveConfirmationMenu moveConfirmationMenu = new MoveConfirmationMenu(thisButton.board);
            thisButton.getStage().addActor(moveConfirmationMenu);
            thisButton.board.confirmationMenu = moveConfirmationMenu;
            thisButton.board.confirmationMenu.AddConfirmationButton(thisButton.moveSet);
            return true;
        }
    };
}

