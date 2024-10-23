package com.mygdx.game.BoardUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.GameManager.Team;
import com.mygdx.game.Utils.Constants;

public class TurnCounterMenu extends Table {
    GameManager gameManager;
    TextButton turnCounter;
    Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
    public TurnCounterMenu(GameManager gameManager) {
        this.gameManager = gameManager;
        turnCounter = new TextButton("Turn: " + gameManager.turnNumber, skin);

        this.add(turnCounter).expand().fill();
        this.setBounds(Constants.SCREEN_WIDTH - 210 , Constants.SCREEN_HEIGHT-40, 195, 35);
        this.setName("TurnCounterMenu");
    }

    public void UpdateTurn (){
        this.turnCounter.setText("Turn: " + gameManager.turnNumber);
    }
}
