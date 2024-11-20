package com.mygdx.game.HUD;

import static java.util.Map.entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.Utils.IntPair;

import java.util.Map;

public class PossibleMoveImageCreator extends Group {
    Image emptyGrid = new Image(new Texture(Gdx.files.internal("emptygrid.png")));

    Map<Integer, Float> xCoords = Map.ofEntries(
            entry(0,0f ),
            entry(1, 41f),
            entry(2, 82f),
            entry(3, 123f),
            entry(4, 164f)
    );

    Map<Integer, Float> yCoords = Map.ofEntries(
            entry(0,0f ),
            entry(1, 41f),
            entry(2, 82f),
            entry(3, 123f),
            entry(4, 164f)
    );

    public PossibleMoveImageCreator(MoveSet moveSetToMap) {
        this.addActor(emptyGrid);
        for (IntPair possibleMove: moveSetToMap.possibleMoves){
            Image player = new Image(new Texture(Gdx.files.internal("black_player.png")));
            player.setScale(0.5625f);
            player.setPosition(xCoords.get(2+possibleMove.xVal), yCoords.get(2+possibleMove.yVal));
            this.addActor(player);
        }
        Image player = new Image(new Texture(Gdx.files.internal("green_player.png")));
        player.setScale(0.5625f);
        player.setPosition(xCoords.get(2), yCoords.get(2));
        this.addActor(player);
    }
}
