package com.mygdx.game.core.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class AbilityComponent implements Component, Pool.Poolable {
    public enum AbilityType {
        NOT_DEFINED, CRIT, ARMOR
    }

    public AbilityType type;
    public int abilityID;

    @Override
    public void reset() {
        type = null;
        abilityID = 0;
    }
}
