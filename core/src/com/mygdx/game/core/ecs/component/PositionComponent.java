package com.mygdx.game.core.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.Utils.IntPair;

public class PositionComponent implements Component, Pool.Poolable {
    IntPair boardPosition;
    float screenPosX;
    float screenPosY;

    @Override
    public void reset() {
        boardPosition = null;
        screenPosX = 0;
        screenPosY = 0;
    }
}
