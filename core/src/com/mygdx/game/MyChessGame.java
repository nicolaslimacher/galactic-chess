package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Screens.MainMenuScreen;
import com.mygdx.game.core.ResourceManager;

public class MyChessGame extends ApplicationAdapter {
    public Stage stage;
    private ResourceManager resourceManager;
    private SpriteBatch spriteBatch;
    private Game game;

    public void create() {
        stage = new Stage(new FitViewport(800, 480));
        resourceManager = new ResourceManager();
        spriteBatch = new SpriteBatch();
        //add methods for creating and disposing screens as needed
        this.setScreen(new MainMenuScreen(this));
    }

    public ResourceManager GetResourceManager(){
        return resourceManager;
    }

    public SpriteBatch GetSpriteBatch(){
        return spriteBatch;
    }


    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        stage.dispose();
    }

}