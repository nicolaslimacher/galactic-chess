package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MyChessGame extends Game{
    public CustomSpriteBatch customBatch;
    public BitmapFont font;
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