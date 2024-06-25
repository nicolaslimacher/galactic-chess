package com.mygdx.game.BoardUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.Board.Board;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.Constants;

public class MoveSelectButtonMenu extends Table {
    Board board;
    Skin moveSelectSkin = new Skin(Gdx.files.internal("buttons/uiskin.json"));
    public MoveSelectButtonMenu(Board board, MoveSet[] moveSets) {
        for (MoveSet moveSet: moveSets) {
            this.add(new MoveSelectButton(moveSelectSkin, moveSet,board)).width(Constants.SCREEN_WIDTH*0.225f).height(Constants.SCREEN_HEIGHT*0.2f).expand();
            this.row();
        }
        this.setBounds(Constants.SCREEN_WIDTH *0.025f, 0, Constants.SCREEN_WIDTH*0.25f, Constants.SCREEN_HEIGHT);
        this.defaults().padRight(10); // All cells have a padding of 10px to the right
        this.setName("MoveSelectButtonMenu");
        this.board = board;
        board.menuTable = this;

    }
}
