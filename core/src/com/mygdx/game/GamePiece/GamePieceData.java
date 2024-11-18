package com.mygdx.game.GamePiece;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.Components.AbilityComponent;

public class GamePieceData {

    private final JsonValue gamePieceData;

    public GamePieceData(int gamePieceID) {
        gamePieceData = new JsonReader().parse(Gdx.files.internal("JSONs/GamePiece.json")).get(String.valueOf(gamePieceID));
    }

    public int getAttackPoints(){
        return gamePieceData.getInt("attack");
    }

    public int getHitPoints(){
        return gamePieceData.getInt("hit points");
    }

    public String getTextureName(){
        return gamePieceData.getString("texture name");
    }

    public AbilityComponent.AbilityType getAbility(){
        return AbilityComponent.AbilityType.fromString(gamePieceData.getString("ability")); //defaults to AbilityType.NULL
    }
}
