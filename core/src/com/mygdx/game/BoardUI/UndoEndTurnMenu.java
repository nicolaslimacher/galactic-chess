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

public class UndoEndTurnMenu extends Table {
    Skin skin = new Skin(Gdx.files.internal("buttons/uiskin.json"));
    public TextButton undoButton;
    public TextButton endTurn;

    public UndoEndTurnMenu() {
        this.undoButton = new TextButton("UNDO", skin);
        undoButton.addListener(undoButtonListener);
        undoButton.setDisabled(true);
        this.add(undoButton).expand().fill();

        this.endTurn = new TextButton("END TURN", skin);
        endTurn.addListener(endTurnButtonListener);
        this.add(endTurn).expand().fill();

        this.setBounds(Constants.SCREEN_WIDTH - 400 , 5, 390, 35);
        this.defaults().padRight(10); // All cells have a padding of 10px to the right
        this.setName("UndoEndTurnMenu");
    }

    private final InputListener undoButtonListener = new InputListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            GameManager gameManager = getStage().getRoot().findActor("GameManager");
            if (gameManager.latestGamePieceCommand != null){
                gameManager.latestGamePieceCommand.Undo();
            }
            return true;
        }
    };

    private final InputListener endTurnButtonListener = new InputListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            GameManager gameManager = getStage().getRoot().findActor("GameManager");
            gameManager.turnNumber = gameManager.turnNumber + 1;
            gameManager.currentTurn = Team.ENEMY;
            gameManager.movedThisTurn = false;
            //call AI to make turn
            gameManager.enemyAI.MakeMove();
            gameManager.turnCounterMenu.UpdateTurn();
            return true;
        }
    };

    public void EnableUndoButton(){
        this.undoButton.setDisabled(false);
    }
    public void DisableUndoButton(){
        this.undoButton.setDisabled(true);
    }
}
