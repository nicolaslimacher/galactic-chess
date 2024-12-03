package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Manager.PRNGManager;
import com.mygdx.game.Manager.ResourceManager;
import com.mygdx.game.Manager.RunManager;
import com.mygdx.game.Screens.MainMenuScreen;

public class WranglerGiddyUp extends Game{
    public Stage stage;
    private ResourceManager resourceManager;
    private PRNGManager prngManager;
    private RunManager runManager;


    public void create() {
        stage = new Stage(new FitViewport(800, 480));
        resourceManager = new ResourceManager();
        prngManager = new PRNGManager();
        runManager = new RunManager();

        //resourceManager.loadSkinSynchronously()
        //add methods for creating and disposing screens as needed
        this.setScreen(new MainMenuScreen(this));
    }

    public ResourceManager getResourceManager(){
        return resourceManager;
    }
    public PRNGManager getPrngManager() {return prngManager;}
    public RunManager getRunManager() {return runManager;}

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        stage.dispose();
    }

}