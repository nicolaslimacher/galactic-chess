package com.mygdx.game.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.game.MyChessGame;
import com.mygdx.game.Utils.IntPair;
import com.mygdx.game.core.ecs.EntityEngine;
import com.mygdx.game.core.ecs.component.AbilityComponent;
import com.mygdx.game.core.ecs.component.StatsComponent;
import com.mygdx.game.core.ecs.component.GamePieceComponent;
import com.mygdx.game.core.ecs.component.KingComponent;
import com.mygdx.game.core.ecs.component.RenderComponent;

public class ECSEngine extends EntityEngine {
    private final MyChessGame game;

    public ECSEngine(MyChessGame game, OrthographicCamera camera) {
        super();
        this.game = game;
    }

    public void createGamePiece(int gamePieceId, boolean isKing, IntPair boardPosition){
        final Entity gamePiece = this.createEntity();

        final GamePieceComponent gamePieceComponent = this.createComponent(GamePieceComponent.class);
        gamePieceComponent.GamePieceID = 1;
        gamePiece.add(gamePieceComponent);

        final StatsComponent statsComponent = this.createComponent(StatsComponent.class);
        statsComponent.attackVal = 1;
        statsComponent.healthVal = 1;
        gamePiece.add(statsComponent);

        if (isKing){
            gamePiece.add(this.createComponent(KingComponent.class));
        }

        final RenderComponent renderComponent = this.createComponent(RenderComponent.class);
        renderComponent.textureRegionLookup("black_player", game);
        gamePiece.add(renderComponent);

        final AbilityComponent abilityComponent = this.createComponent(AbilityComponent.class);
        abilityComponent.abilityID = 1;
        abilityComponent.type = AbilityComponent.AbilityType.NOT_DEFINED;
        gamePiece.add(abilityComponent);

    }


}
