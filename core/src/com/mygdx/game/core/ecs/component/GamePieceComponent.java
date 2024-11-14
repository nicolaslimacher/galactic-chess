package com.mygdx.game.core.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class GamePieceComponent implements Component, Pool.Poolable {
    public int GamePieceID;

    @Override
    public void reset() {
        GamePieceID = 0;
    }
}
