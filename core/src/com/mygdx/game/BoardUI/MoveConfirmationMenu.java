package com.mygdx.game.BoardUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.Board.Board;
import com.mygdx.game.MoveSets.MoveSet;

public class MoveConfirmationMenu extends Table {
    Board board;
    Skin moveSelectSkin = new Skin(Gdx.files.internal("buttons/uiskin.json"));
    public MoveConfirmationMenu(Board board) {
        this.setBounds(Gdx.graphics.getWidth()*0.025f, 0, Gdx.graphics.getWidth()*0.25f, Gdx.graphics.getHeight());
        this.defaults().padRight(10); // All cells have a padding of 10px to the right
        this.setVisible(false);
        this.setName("MoveConfirmationMenu");
    }

    public void AddConfirmationButton (MoveSet moveSet){
        this.setVisible(true);
        this.add(new MoveConfirmationButton(moveSet, moveSelectSkin));
        this.row();
        TextButton cancelButton = new TextButton("Cancel", moveSelectSkin);
        this.add(cancelButton);
    }
}
