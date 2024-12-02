package com.mygdx.game.Manager;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.MoveSets.MoveSet;
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

    public List<MoveSet> getNextEncounterMoveSets(int moveSetsToChose){
        List<MoveSet> availableMoveSets = new ArrayList<>(5);
        List<Integer> moveSetNumberChosen = new ArrayList<>(5);
        MoveSet[] moveSets = json.fromJson(MoveSet[].class, Gdx.files.internal("JSONs/MoveSet.json"));
        int numberOfMoveSets = moveSets.length;

        int i = 0;
        int moveSetNumber;
        while(i < moveSetsToChose) {
            //getNextRand starts at index 1
            do {
                moveSetNumber = Helpers.getPRNGManager().getNextRand(PRNGManager.PRNGType.moveCardSeed, numberOfMoveSets);
            }
            while (!moveSetNumberChosen.isEmpty() && moveSetNumberChosen.contains(moveSetNumber-1));

            moveSetNumberChosen.add(moveSetNumber-1);
            availableMoveSets.add(moveSets[moveSetNumber-1]);
            i++;
        }

        for (int num : moveSetNumberChosen){
            Gdx.app.debug(TAG, "moveSetNumber chosen: " + num);
        }

        return availableMoveSets;
    }

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
