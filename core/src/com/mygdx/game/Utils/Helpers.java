package com.mygdx.game.Utils;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.MoveSets.MoveSet;

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

    public static MoveSet[] GetRandomMoveSets(int lowerBound, int upperBound){
        MoveSet[] availableMoveSets = new MoveSet[5];
        MoveSet[] moveSets = json.fromJson(MoveSet[].class, Gdx.files.internal("MoveSet.json"));
        int moveSetIndex = 0;
        for ( int moveSetNumber : Helpers.GetShuffledIntsInRange(lowerBound,upperBound).subList(0,5) ) {
            availableMoveSets[moveSetIndex] = moveSets[moveSetNumber];
            moveSetIndex = moveSetIndex + 1;
        }
        return availableMoveSets;
    }
}
