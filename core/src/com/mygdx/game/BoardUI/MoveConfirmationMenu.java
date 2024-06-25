package com.mygdx.game.BoardUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Board.Board;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.Constants;

public class MoveConfirmationMenu extends Table {
    Board board;
    Skin moveSelectSkin = new Skin(Gdx.files.internal("buttons/uiskin.json"));
    public MoveConfirmationMenu(Board board) {
        this.setBounds(Constants.SCREEN_WIDTH*0.025f, 0, Constants.SCREEN_WIDTH*0.25f, Constants.SCREEN_HEIGHT);
        this.board = board;
        this.setVisible(false);
        this.setName("MoveConfirmationMenu");
    }

    public void AddConfirmationButton (MoveSet moveSet) {
        this.setVisible(true);
        this.row();
        this.add(new MoveConfirmationButton(moveSet, moveSelectSkin, this.board)).size((float) (this.getWidth() * 0.8f), (float) (this.getHeight() * (0.8)));
    }
}
