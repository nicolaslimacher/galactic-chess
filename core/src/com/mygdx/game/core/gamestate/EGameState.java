package com.mygdx.game.core.gamestate;

import com.mygdx.game.gamestate.GSLoading;

public enum EGameState {
    GAME(GSGame.class),
    LOADING(GSLoading.class)
    //,MENU(GSMenu.class),
    //VICTORY(GSVictory.class)
    ;

    private final Class<? extends GameState> gsClass;

    EGameState(final Class<? extends GameState> gsClass) {
        this.gsClass = gsClass;
    }

    public Class<? extends GameState> getGameStateType() {
        return gsClass;
    }
}
