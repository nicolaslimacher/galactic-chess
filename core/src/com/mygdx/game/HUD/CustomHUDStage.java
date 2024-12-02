package com.mygdx.game.HUD;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Manager.BattleManager;

public class CustomHUDStage extends Stage {
    //game manager reference needed for HUD object listeners to find with getStage()
    BattleManager battleManager;

    public CustomHUDStage(Viewport viewport, Batch batch, BattleManager battleManager) {
        super(viewport, batch);
        this.battleManager = battleManager;
    }
}
