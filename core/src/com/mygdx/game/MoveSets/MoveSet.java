package com.mygdx.game.MoveSets;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Utils.IntPair;

public class MoveSet{
    public int index;
    public String name;
    public Array<IntPair> possibleMoves;

    public MoveSet() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Array<IntPair> getMoveSets() {
        return possibleMoves;
    }

    public void setMoveSets(Array<IntPair> possibleMoves) {
        this.possibleMoves = possibleMoves;
    }
}
