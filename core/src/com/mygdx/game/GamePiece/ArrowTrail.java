package com.mygdx.game.GamePiece;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ArrowTrail extends Actor {

    public final TextureRegion textureRegion;

    public ArrowTrail(Vector2 pos) {
        this.textureRegion = new TextureRegion(new Texture(Gdx.files.internal("black_player.png")), 64, 64);
        this.setBounds(pos.x, pos.y, 32, 32);
        //this.setDebug(true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, 1f);
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(),
            getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
