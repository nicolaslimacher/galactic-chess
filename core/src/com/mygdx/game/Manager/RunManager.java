package com.mygdx.game.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.Utils.Helpers;

import java.util.ArrayList;
import java.util.List;

public class RunManager {
    private static final String TAG = RunManager.class.getSimpleName();

    private int numberOfEncounters;
    private List<Integer> easyEncountersFought;
    private int characterID;
    private BattleManager currentBattleManager;

    public RunManager() {
        numberOfEncounters = 0;
        easyEncountersFought = new ArrayList<>();
    }

    getNext

    public int getNumberOfEncounters() {return numberOfEncounters;}
    public void setNumberOfEncounters(int numberOfEncounters) {this.numberOfEncounters = numberOfEncounters;}

    public int getCharacterID() {return characterID;}
    public void setCharacterID(int characterID) {this.characterID = characterID;}

    public BattleManager getCurrentBattleManager() {return currentBattleManager;}
    public void setCurrentBattleManager(BattleManager currentBattleManager) {this.currentBattleManager = currentBattleManager;}

    public JsonValue getNextBattleJsonValue(){
        if (numberOfEncounters <= 3){
            JsonValue fights = new JsonReader().parse(Gdx.files.internal("JSONs/EasyFights.json"));
            int fightNumber = 0;

            //keep generating new encounter from seed until new encounter is made4
            do {
                fightNumber = Helpers.getPRNGManager().getNextRand(PRNGManager.PRNGType.enemyEncounterSeed, fights.size); //gives int between 1 and fights.size
            }
            while (easyEncountersFought.contains(fightNumber));

            easyEncountersFought.add(fightNumber);
            Gdx.app.log(TAG, "fightNumber: " + fightNumber);
            return fights.get(String.valueOf(fightNumber));
        } else {
            return null;
        }
    }
}
