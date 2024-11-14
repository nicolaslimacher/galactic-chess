package com.mygdx.game.core.gamestate;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Utils.Utils;
import com.mygdx.game.core.input.KeyInputListener;

public abstract class GameState<T extends Table> implements Disposable, KeyInputListener { //need input listener?
    private final EGameState type;

    protected GameState(final EGameState type) {
        this.type = type;
    }

    //protected abstract T createHUD(final HUD hud, final TTFSkin skin);

    public EGameState getType() {
        return type;
    }

    public void activate() {
        Utils.getInputManager().addKeyInputListener(this);
        //gameStateHUD.setVisible(true);
    }

    public void deactivate() {
        Utils.getInputManager().removeKeyInputListener(this);
        //gameStateHUD.setVisible(false);
    }

    public void step(final float fixedTimeStep) {
       // hud.step(fixedTimeStep);
    }

    public void render(final float alpha) {
        //hud.render(alpha);
    }

    public void resize(final int width, final int height) {
        //hud.resize(width, height);
    }
}
