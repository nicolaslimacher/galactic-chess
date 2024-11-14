package com.mygdx.game.gamestate;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Utils.Utils;
import com.mygdx.game.core.ResourceManager;
import com.mygdx.game.core.gamestate.EGameState;
import com.mygdx.game.core.gamestate.GameState;
import com.mygdx.game.core.input.EKey;
import com.mygdx.game.core.input.InputManager;

public class GSLoading extends GameState {
    private final ResourceManager resourceManager;
    private boolean isMusicLoaded;

    public GSLoading(final EGameState type) {
        super(type);
        isMusicLoaded = false;

        resourceManager = Utils.getResourceManager();
        resourceManager.load("characters_and_effects/character_and_effect.atlas", TextureAtlas.class);
        resourceManager.load("map/tiles/map.atlas", TextureAtlas.class);
        //loadAudio();
    }

//    @Override
//    protected LoadingUI createHUD(final HUD hud, final TTFSkin skin) {
//        return new LoadingUI(hud, skin);
//    }

//    private void loadAudio() {
//        for (final AudioManager.AudioType type : AudioManager.AudioType.values()) {
//            if (resourceManager.isLoaded(type.getFilePath())) {
//                continue;
//            }
//            if (type.isMusic()) {
//                resourceManager.load(type.getFilePath(), Music.class);
//            } else {
//                resourceManager.load(type.getFilePath(), Sound.class);
//            }
//        }
//    }

    @Override
    public void step(final float fixedTimeStep) {
        super.step(fixedTimeStep);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void keyDown(final InputManager manager, final EKey key) {
        if (resourceManager.getProgress() == 1) {
            //Utils.getAudioManager().playAudio(AudioManager.AudioType.SELECT);
            //Utils.setGameState(EGameState.MENU, true);
        }
    }

    @Override
    public void keyUp(final InputManager manager, final EKey key) {
        // nothing to do for key up
    }
}
