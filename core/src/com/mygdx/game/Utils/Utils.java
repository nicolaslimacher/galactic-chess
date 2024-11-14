package com.mygdx.game.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyChessGame;
import com.mygdx.game.core.ResourceManager;
import com.mygdx.game.core.gamestate.EGameState;
import com.mygdx.game.core.input.InputManager;

public final class Utils {

    private Utils(){
    }

    public static SpriteBatch getSpriteBatch() {
        return ((MyChessGame) Gdx.app.getApplicationListener()).GetSpriteBatch();
    }

    public static InputManager getInputManager() {
        return (InputManager) ((InputMultiplexer) Gdx.input.getInputProcessor()).getProcessors().get(0);
    }

    public static ResourceManager getResourceManager() {
        return ((MyChessGame) Gdx.app.getApplicationListener()).GetResourceManager();
    }

    public static void setGameState(final EGameState gameStateType) {
        setGameState(gameStateType, false);
    }

    public static void setGameState(final EGameState gameStateType, final boolean disposeActive) {
        ((MyChessGame) Gdx.app.getApplicationListener()).getGame().setGameState(gameStateType, disposeActive);
    }
}
