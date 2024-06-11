package com.mygdx.game.Utils;

import org.w3c.dom.ranges.Range;

import java.awt.font.NumericShaper;
import java.time.temporal.ValueRange;

public class Constants {
    public static final float SCREEN_WIDTH = 800.0f;
    public static final float SCREEN_HEIGHT = 480.0f;
    public static final float TILE_SIZE = 64.0f;
    public static final float TILE_BUFFER = 8.0F;
    public static final float SCREEN_BOARD_RATION = 0.7f;
    public static final float BOARD_MINIMUM_WIDTH = 400.0f;
//    public static float FilterBoardWidth (float width) {
//        if (width <= BOARD_MINIMUM_WIDTH){
//            return BOARD_MINIMUM_WIDTH;
//        } else return Math.min(width, BOARD_MAXIMUM_WIDTH);
//    }
    public static final float BOARD_MAXIMUM_WIDTH = 1000.0f;
    public static final float BOARD_MINIMUM_HEIGHT = 400.0f;
    public static final float BOARD_MAXIMUM_HEIGHT = 1000.0f;
//    public static float FilterBoardHeight (float height) {
//        if (height <= BOARD_MINIMUM_HEIGHT){
//            return BOARD_MINIMUM_HEIGHT;
//        } else return Math.min(height, BOARD_MAXIMUM_HEIGHT);
//    }

}
