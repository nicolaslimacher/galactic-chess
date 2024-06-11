package com.mygdx.game.Utils;

import java.util.Objects;

public class CoordinateBoardPair {
    // Ideally, name the class after whatever you're actually using
    // the int pairs *for.*
    public final int x;
    public final int y;
    public CoordinateBoardPair(int x, int y) {
        this.x=x;
        this.y=y;
    }

    public CoordinateBoardPair(IntPair intPair){
        this.x= intPair.getXVal();
        this.y= intPair.getYVal();
    }

    public int GetX(){
        return this.x;
    }
    public int GetY(){
        return this.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoordinateBoardPair that = (CoordinateBoardPair) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
