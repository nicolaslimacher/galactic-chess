package com.mygdx.game.HUD;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Manager.GameManager;

public class CustomHUDStage extends Stage {
    //game manager reference needed for HUD object listeners to find with getStage()
    GameManager gameManager;

    public CustomHUDStage(Viewport viewport, Batch batch, GameManager gameManager) {
        super(viewport, batch);
        this.gameManager = gameManager;
    }
}
