package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Screens.MainMenuScreen;

public class MyChessGame extends Game{
    public Stage stage;

    public void create() {
        stage = new Stage(new ScreenViewport());
        //add methods for creating and disposing screens as needed
        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        stage.dispose();
    }

}