package com.mygdx.game.gamestate;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Utils.Utils;
import com.mygdx.game.core.ResourceManager;
import com.mygdx.game.core.gamestate.EGameState;
import com.mygdx.game.core.gamestate.GameState;
import com.mygdx.game.core.input.InputManager;
import com.mygdx.game.ecs.ECSEngine;

public class GSGame extends GameState<> implements GameTimeSystem.GameTimeListener {
    private final ECSEngine ecsEngine;
    private final ImmutableArray<Entity> playerEntities;


    public GSGame(final EGameState type) {
        super(type);

        // entity component system
        this.ecsEngine = new ECSEngine(new OrthographicCamera());
        ecsEngine.getSystem(PlayerContactSystem.class).addPlayerContactListener(this);
        ecsEngine.getSystem(GameTimeSystem.class).addGameTimeListener(this);
        playerEntities = ecsEngine.getEntitiesFor(Family.all(PlayerComponent.class).get());

        // init map -> this needs to happen after ECSEngine creation because some systems need to register as listeners first
        Utils.getMapManager().loadMap(world);
        ecsEngine.addPlayer(Utils.getMapManager().getCurrentMap().getStartLocation());
    }

//    @Override
//    protected GameUI createHUD(final HUD hud, final TTFSkin skin) {
//        return new GameUI(hud, skin);
//    }

    @Override
    public void activate() {
        super.activate();
        //Utils.getInputManager().addKeyInputListener(ecsEngine.getSystem(.class));
        //saveState.loadState(playerEntities.first(), ecsEngine, gameStateHUD);
        //Utils.getAudioManager().playAudio(AudioManager.AudioType.ALMOST_FINISHED);
        //ecsEngine.getSystem(PlayerMovementSystem.class).reset();
    }

    @Override
    public void deactivate() {
        super.deactivate();
        //Utils.getInputManager().removeKeyInputListener(ecsEngine.getSystem(PlayerMovementSystem.class));
        //saveState.updateState(playerEntities.first(), ecsEngine);
    }

    @Override
    public void step(final float fixedTimeStep) {
        // important to update entity engine before updating the box2d because we need to store
        // the body position before the next step for the interpolation rendering
        ecsEngine.update(fixedTimeStep);
        super.step(fixedTimeStep);
    }

    @Override
    public void render(final float alpha) {
        ecsEngine.render(alpha);
        super.render(alpha);
    }

    @Override
    public void resize(final int width, final int height) {
        ecsEngine.resize(width, height);
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        ecsEngine.dispose();
        world.dispose();
        rayHandler.dispose();
    }

    @Override
    public void keyDown(final InputManager manager, final EKey key) {
        if (key == EKey.BACK) {
            Utils.setGameState(EGameState.MENU);
        }
    }

    @Override
    public void keyUp(final InputManager manager, final EKey key) {
        // input handling is done within ECS systems
    }

    @Override
    public void crystalContact(final int crystalsFound) {
        gameStateHUD.setCrystals(crystalsFound);
        if (crystalsFound == 1) {
            gameStateHUD.showInfoMessage(hud.getLocalizedString("crystalInfo"), 7.0f);
        }
    }


    @Override
    public void chromaOrbContact(final int chromaOrbsFound) {
        //gameStateHUD.setChromaOrb(chromaOrbsFound);
        if (chromaOrbsFound == 1) {
            //gameStateHUD.showInfoMessage(hud.getLocalizedString("chromaOrbInfo"), 7.0f);
        }
    }

    @Override
    public void portalContact(final boolean hasAllCrystals) {
        if (hasAllCrystals) {
            //Utils.setGameState(EGameState.VICTORY);
        } else {
            //gameStateHUD.showInfoMessage(hud.getLocalizedString("portalInfo"), 5.0f);
        }
    }

    @Override
    public void gameTimeUpdated(final int hours, final int minutes, final int seconds) {
        //gameStateHUD.setGameTime(hours, minutes, seconds);
    }
}
