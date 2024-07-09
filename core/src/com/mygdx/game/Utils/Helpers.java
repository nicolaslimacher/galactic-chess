package com.mygdx.game.Utils;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.MoveSets.MoveSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Helpers {
    public static List<Integer> GetShuffledIntsInRange(int lowerBound, int upperBound){
        List<Integer> range = IntStream.range(lowerBound, upperBound).boxed().collect(Collectors.toList());
        Collections.shuffle(range);
        return range;
    }

    public static List<MoveSet> GetRandomMoveSets(int lowerBound, int upperBound){
        List<MoveSet> availableMoveSets = new ArrayList<>(5);
        MoveSet[] moveSets = json.fromJson(MoveSet[].class, Gdx.files.internal("MoveSet.json"));
        for ( int moveSetNumber : Helpers.GetShuffledIntsInRange(lowerBound,upperBound).subList(0,5) ) {
            availableMoveSets.add(moveSets[moveSetNumber]);
        }
        return availableMoveSets;
    }
}
