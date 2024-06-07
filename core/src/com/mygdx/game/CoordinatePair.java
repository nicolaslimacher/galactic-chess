package com.mygdx.game;

public class CoordinatePair {
    // Ideally, name the class after whatever you're actually using
    // the int pairs *for.*
    final int x;
    final int y;
    public CoordinatePair(int x, int y) {
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
