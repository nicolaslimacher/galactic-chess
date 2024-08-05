package com.mygdx.game.Utils;

import java.util.Objects;

public class IntPair{
    public int xVal;
    public int yVal;
    public IntPair(int x, int y) {
        this.xVal=x;
        this.yVal=y;
    }

    public IntPair(){
        this.xVal=0;
        this.yVal=0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntPair that = (IntPair) o;
        return xVal == that.xVal && yVal == that.yVal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xVal, yVal);
    }
}
