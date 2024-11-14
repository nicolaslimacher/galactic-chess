package com.mygdx.game.core.ecs.component;

import com.badlogic.ashley.core.ComponentMapper;

public class Components {
    public static final ComponentMapper<AbilityComponent> ABILITY = ComponentMapper
            .getFor(AbilityComponent.class);
    public static final ComponentMapper<GamePieceComponent> GAMEPIECE = ComponentMapper
            .getFor(GamePieceComponent.class);
    public static final ComponentMapper<KingComponent> KING = ComponentMapper
            .getFor(KingComponent.class);
    public static final ComponentMapper<PositionComponent> POSITION = ComponentMapper
            .getFor(PositionComponent.class);
    public static final ComponentMapper<RenderComponent> RENDER = ComponentMapper
            .getFor(RenderComponent.class);
    public static final ComponentMapper<StatsComponent> STATS = ComponentMapper
            .getFor(StatsComponent.class);
    public static final ComponentMapper<AnimationComponent> ANIMATION = ComponentMapper
            .getFor(AnimationComponent.class);

//  EXAMPLE USAGE IN SYSTEM
//    @Override
//    public void processEntity(Entity entity, float deltaTime) {
//        Worker w = Components.WORKER.get(entity);
//        w.update(deltaTime * GameController.get().getTimeScale());
//    }

}
