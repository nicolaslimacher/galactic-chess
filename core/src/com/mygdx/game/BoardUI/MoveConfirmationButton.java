package com.mygdx.game.BoardUI;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.MoveSets.MoveSet;

public class MoveConfirmationButton extends TextButton {

    public MoveConfirmationButton(MoveSet moveSet, Skin skin) {
        super("", skin);
        this.setText(GetButtonText(moveSet));
    }

    private String GetButtonText(MoveSet moveSet){
        String buttonText;
        buttonText = moveSet.name;
        return buttonText;
    }
}
