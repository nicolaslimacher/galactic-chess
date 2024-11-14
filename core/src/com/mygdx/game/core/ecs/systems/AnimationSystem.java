package com.mygdx.game.core.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.core.ecs.component.AnimationComponent;
import com.mygdx.game.core.ecs.component.Components;

public class AnimationSystem extends IteratingSystem {
    public AnimationSystem() {
        super(Family.all(AnimationComponent.class).get());
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        Components.ANIMATION.get(entity).aniTimer += deltaTime;
    }
}
