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
            System.out.println("I've been clicked");
            CancelButton thisButton = (CancelButton) event.getListenerActor();
            thisButton.board.selectedMoveSet = null;
            thisButton.getParent().setVisible(false);
            System.out.println(thisButton.getParent());
            thisButton.board.menuTable.setVisible(true);
            System.out.println(thisButton.board.menuTable);
            return true;
        }
    };

}
