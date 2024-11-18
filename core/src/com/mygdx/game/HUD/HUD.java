package com.mygdx.game.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Manager.GameManager;
import com.mygdx.game.Utils.Constants;
import com.mygdx.game.Utils.Helpers;

public class HUD implements Disposable {
    public Stage stage;
    private Viewport viewport;

    //Scene2D Widgets
    Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
    private TextButton turnCounter;
    private TextButton undoButton, endTurn;

    public HUD(SpriteBatch spriteBatch) {
        viewport = new FitViewport(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);

        turnCounter = new TextButton("Turn: " + Helpers.getGameManager().turnNumber, skin);
        turnCounter.setBounds(Constants.SCREEN_WIDTH - 210 , Constants.SCREEN_HEIGHT-40, 195, 35);
        turnCounter.setName("TurnCounterMenu");

        Table table = new Table();

        this.undoButton = new TextButton("UNDO", skin);
        undoButton.addListener(undoButtonListener);
        table.add(undoButton).expand().fill();
        DisableUndoButton(); //game manager will enable when move command has been made

        this.endTurn = new TextButton("END TURN", skin);
        endTurn.addListener(endTurnButtonListener);
        table.add(endTurn).expand().fill();
        DisableEndTurnButton(); //game manager will enable when move command has been made

        table.setBounds(Constants.SCREEN_WIDTH - 400 , 5, 390, 35);
        table.defaults().padRight(10); // All cells have a padding of 10px to the right
        table.setName("UndoEndTurnMenu");
        stage.addActor(table);
    }

    private final InputListener undoButtonListener = new InputListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            System.out.println("Played has hit undo");
            GameManager gameManager = Helpers.getGameManager();
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
            GameManager gameManager = Helpers.getGameManager();
            gameManager.EndPlayerTurn();
            return true;
        }
    };


    @Override
    public void dispose() {
        stage.dispose();
    }

    public void UpdateTurn (){
        this.turnCounter.setText("Turn: " + Helpers.getGameManager().turnNumber);
    }
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
