package com.mygdx.game.BoardUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.GameManager.GameManager;
import com.mygdx.game.GameManager.Team;
import com.mygdx.game.Utils.Constants;

public class UndoEndTurnMenu extends Table {
    Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
    public TextButton undoButton;
    public TextButton endTurn;

    public UndoEndTurnMenu() {
        this.undoButton = new TextButton("UNDO", skin);
        undoButton.addListener(undoButtonListener);
        this.add(undoButton).expand().fill();
        DisableUndoButton();

        this.endTurn = new TextButton("END TURN", skin);
        endTurn.addListener(endTurnButtonListener);
        this.add(endTurn).expand().fill();
        DisableEndTurnButton(); //game manager will enable when move command has been made

        this.setBounds(Constants.SCREEN_WIDTH - 400 , 5, 390, 35);
        this.defaults().padRight(10); // All cells have a padding of 10px to the right
        this.setName("UndoEndTurnMenu");
    }

    private final InputListener undoButtonListener = new InputListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            System.out.println("Played has hit undo");
            GameManager gameManager = getStage().getRoot().findActor("GameManager");
            if (gameManager.latestGamePieceCommand != null){
                gameManager.latestGamePieceCommand.Undo();
            }
            DisableEndTurnButton();
            return true;
        }
    };

    private final InputListener endTurnButtonListener = new InputListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            System.out.println("Player has ended turn");
            GameManager gameManager = getStage().getRoot().findActor("GameManager");
            gameManager.EndPlayerTurn();
            return true;
        }
    };

    public void EnableUndoButton(){
        this.undoButton.setTouchable(Touchable.enabled);
    }
    public void DisableUndoButton(){
        this.undoButton.setTouchable(Touchable.disabled);
    }

    public void EnableEndTurnButton(){
        this.endTurn.setTouchable(Touchable.enabled);
    }
    public void DisableEndTurnButton(){
        this.endTurn.setTouchable(Touchable.disabled);
    }
}
