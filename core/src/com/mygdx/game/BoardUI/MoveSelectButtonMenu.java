package com.mygdx.game.BoardUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.Board.Board;
import com.mygdx.game.MoveSets.MoveSet;

public class MoveSelectButtonMenu extends Table {
    Board board;
    Skin moveSelectSkin = new Skin(Gdx.files.internal("buttons/uiskin.json"));
    public MoveSelectButtonMenu(Board board, MoveSet[] moveSets) {
        for (MoveSet moveSet: moveSets) {
            this.add(new MoveSelectButton(moveSelectSkin, moveSet,board)).width(Gdx.graphics.getWidth()*0.225f).height(Gdx.graphics.getHeight()*0.2f).expand();
            this.row();
        }
        this.setBounds(Gdx.graphics.getWidth()*0.025f, 0, Gdx.graphics.getWidth()*0.25f, Gdx.graphics.getHeight());
        this.defaults().padRight(10); // All cells have a padding of 10px to the right
        this.setName("MoveSelectButtonMenu");
    }
}
