package com.mygdx.game.BoardUI;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.MoveSets.MoveSet;

public class MoveConfirmationButton extends Button {

    public MoveConfirmationButton(MoveSet moveSet, Skin skin) {
        super(skin);
        Label buttonText = new Label(GetButtonText(moveSet), this.getSkin());
        buttonText.setWrap(true);
        buttonText.setWidth(200);
        buttonText.setAlignment(Align.center);
        this.add(buttonText).fillX().prefWidth(200);
    }

    private String GetButtonText(MoveSet moveSet){
        String buttonText;
        buttonText = moveSet.name;
        return buttonText;
    }
}
