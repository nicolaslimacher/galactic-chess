package com.mygdx.game.core.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class StatsComponent implements Component, Pool.Poolable {
    public int attackVal;
    public int healthVal;

    @Override
    public void reset() {
        attackVal = 0;
    }
}
