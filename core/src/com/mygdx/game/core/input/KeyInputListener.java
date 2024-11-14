package com.mygdx.game.core.input;

public interface KeyInputListener {
    void keyDown(final InputManager manager, final EKey key);

    void keyUp(final InputManager manager, final EKey key);
}