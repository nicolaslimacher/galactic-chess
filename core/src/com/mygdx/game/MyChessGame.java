package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Screens.MainMenuScreen;

public class MyChessGame extends Game{
    public Stage stage;
    private AssetManager assetManager;

    public void create() {
        stage = new Stage(new FitViewport(800, 480));
        assetManager = new AssetManager();
        //add methods for creating and disposing screens as needed
        this.setScreen(new MainMenuScreen(this));
    }

    public AssetManager GetAssetManager(){
        return assetManager;
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        stage.dispose();
    }

}