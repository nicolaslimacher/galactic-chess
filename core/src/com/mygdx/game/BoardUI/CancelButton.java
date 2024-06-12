package com.mygdx.game.BoardUI;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.Board.Board;
import com.mygdx.game.MoveSets.MoveSet;

public class CancelButton extends TextButton{
        public MoveSet moveSet;
        public Board board;
        public CancelButton(Skin skin, Board board) {
            super("Cancel", skin);
            this.board = board;
            this.addListener(MoveConfirmationCancelButtonListener);
        }
    private final ClickListener MoveConfirmationCancelButtonListener = new ClickListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            CancelButton thisButton = (CancelButton) event.getListenerActor();
            thisButton.board.selectedMoveSet = null;
            thisButton.getStage().getRoot().findActor("MoveSelectButtonMenu").setVisible(true);
            thisButton.getParent().remove();
            thisButton.board.menuTable = null;

            return true;
        }
    };

}
