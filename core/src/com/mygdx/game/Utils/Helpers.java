package com.mygdx.game.Utils;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.MoveSets.MoveSet;
import com.mygdx.game.MyChessGame;

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

    public static void KeepPopUpOverBoard(TextButton button, float desiredX, float desiredY, float desiredWidth, float desiredHeight){
        float finalX, finalY;

        if (desiredX + desiredWidth > Constants.SCREEN_WIDTH * 0.9f){
            finalX = Constants.SCREEN_WIDTH * 0.9f - desiredWidth;
        } else finalX = Math.max(desiredX, Constants.SCREEN_BOARD_WIDTH_LEFT_OFFSET * 2f);

        if (desiredY + desiredHeight > Constants.SCREEN_HEIGHT * 0.9f){
            finalY = Constants.SCREEN_HEIGHT * 0.9f - desiredHeight;
        } else finalY = Math.max(desiredY, Constants.SCREEN_HEIGHT * 0.1f);

        button.setBounds(finalX, finalY, desiredWidth, desiredHeight);
    }
}
