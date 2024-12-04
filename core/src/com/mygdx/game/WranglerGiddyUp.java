package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.HUD.TTFSkin;
import com.mygdx.game.Manager.PRNGManager;
import com.mygdx.game.Manager.ResourceManager;
import com.mygdx.game.Manager.RunManager;
import com.mygdx.game.Screens.MainMenuScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WranglerGiddyUp extends Game{
    public Stage stage;
    private ResourceManager resourceManager;
    private PRNGManager prngManager;
    private RunManager runManager;
    private TTFSkin skin;


    public void create() {
        stage = new Stage(new FitViewport(800, 480));
        resourceManager = new ResourceManager();
        prngManager = new PRNGManager();
        runManager = new RunManager();


        Map<String,List<Integer>> fonts = new HashMap<String,List<Integer>>(){};
        fonts.put("skins/fontinaredsuit.ttf", Arrays.asList(26, 32));
        fonts.put("skins/il_grinta.ttf", Arrays.asList(16, 20, 26));
        fonts.put("skins/jaini.ttf", Arrays.asList(16, 20, 26));

        skin = resourceManager.loadSkinSynchronously("skins/ttfskin.json", fonts);

        //add methods for creating and disposing screens as needed
        this.setScreen(new MainMenuScreen(this));
    }

    public ResourceManager getResourceManager(){
        return resourceManager;
    }
    public PRNGManager getPrngManager() {return prngManager;}
    public RunManager getRunManager() {return runManager;}
    public TTFSkin getSkin(){return skin;}

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        stage.dispose();
    }

}