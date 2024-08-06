package com.mygdx.game.BoardUI;

import com.mygdx.game.Utils.Constants;

public class MoveCardLocations {
    public static final float CHEMICAL_START_LOCATION_X = Constants.SCREEN_WIDTH * 0.025f;
    public static final float CHEMICAL_END_LOCATION_X = Constants.SCREEN_WIDTH * 0.3f;
    public static final float CHEMICAL_CARDS_WIDTH = CHEMICAL_END_LOCATION_X - CHEMICAL_START_LOCATION_X;

    public static final float ALL_CHEMICAL_BOTTOM = Constants.SCREEN_HEIGHT * 0.025f;
    public static final float ALL_CHEMICAL_TOP = Constants.SCREEN_HEIGHT * 0.975f;

    public static final float ENEMY_CHEMICAL_LOCATION_Y = (ALL_CHEMICAL_TOP - ALL_CHEMICAL_BOTTOM) * (2f/3f);
    public static final float FREE_CHEMICAL_LOCATION_Y = (ALL_CHEMICAL_TOP - ALL_CHEMICAL_BOTTOM) * (1f/3f);
    public static final float PLAYER_CHEMICAL_LOCATION_Y = ALL_CHEMICAL_BOTTOM;
    public static final float CHEMICAL_LABEL_HEIGHT = 200f;
    public static final float CHEMICAL_CARDS_HEIGHT = ((ALL_CHEMICAL_TOP - ALL_CHEMICAL_BOTTOM) / 3f) - CHEMICAL_LABEL_HEIGHT; //allowing some room for labels
}
