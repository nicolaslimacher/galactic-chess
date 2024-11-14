package com.mygdx.game.core.ecs.systems;

import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.MyChessGame;
import com.mygdx.game.core.ecs.RenderSystem;

public class GameRenderSystem implements RenderSystem, Disposable {
    public static final String TAG = GameRenderSystem.class.getSimpleName();

    //private final

    public GameRenderSystem(final MyChessGame context) {
    }

    @Override
    public void dispose() {

    }

    @Override
    public void render(float alpha) {

    }

    @Override
    public void resize(int width, int height) {

    }
}
