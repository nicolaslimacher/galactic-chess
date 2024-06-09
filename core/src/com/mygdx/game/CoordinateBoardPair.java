package com.mygdx.game;

public class CoordinateBoardPair {
    // Ideally, name the class after whatever you're actually using
    // the int pairs *for.*
    public final int x;
    public final int y;
    public CoordinateBoardPair(int x, int y) {
        this.x=x;
        this.y=y;
    }

    public int GetX(){
        return this.x;
    }
    public int GetY(){
        return this.y;
    }
}
