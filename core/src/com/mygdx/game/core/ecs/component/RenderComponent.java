package com.mygdx.game.core.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.MyChessGame;

public class RenderComponent implements Component, Pool.Poolable {
    TextureRegion textureRegion;

    @Override
    public void reset() {
        textureRegion = null;
    }

    public void textureRegionLookup(String filename, MyChessGame game){
        textureRegion = game.GetAssetManager().get("texturePacks/battleTextures.atlas", TextureAtlas.class).findRegion(filename);
    }
}
