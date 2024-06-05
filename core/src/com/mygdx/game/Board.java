package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class Board {
    final private Texture darkCorner;
    final private Texture darkEdge;
    final private Texture darkInternal;
    final private Texture lightCorner;
    final private Texture lightEdge;
    final private Texture lightInternal;

    public Board() {
        this.darkCorner = new Texture("dark_corner.png");
        this.darkEdge = new Texture("dark_edge.png");
        this.darkInternal = new Texture("dark_internal.png");
        this.lightCorner = new Texture("light_corner.png");
        this.lightEdge = new Texture("light_edge.png");
        this.lightInternal = new Texture("light_internal.png");
    }

    public void render(CustomSpriteBatch batch) {
        drawCorners(batch);
        drawEdges(batch);
        drawInternal(batch);
    }

    private void drawCorners(CustomSpriteBatch batch) {
        //top left
        batch.draw(darkCorner, 0, Constants.SCREEN_HEIGHT - Constants.TILE_SIZE, false, false);

        //top right
        batch.draw(lightCorner,
                Constants.SCREEN_WIDTH - Constants.TILE_SIZE,
                Constants.SCREEN_HEIGHT - Constants.TILE_SIZE,
                true, false);

        //bottom left
        batch.draw(lightCorner, 0, 0, false, true);
        //bottom right
        batch.draw(darkCorner, Constants.SCREEN_WIDTH - Constants.TILE_SIZE, 0, true, true);
    }

    private void drawEdges(CustomSpriteBatch batch) {
        for (int i = 1; i < 7; i++) {
            //left
            batch.draw((i % 2) == 0 ? lightEdge : darkEdge, 0, i * Constants.TILE_SIZE, 90);

            //right
            batch.draw((i % 2) != 0 ? lightEdge : darkEdge, Constants.SCREEN_WIDTH - Constants.TILE_SIZE, i * Constants.TILE_SIZE, 270);

            //bottom
            batch.draw((i % 2) == 0 ? lightEdge : darkEdge, i * Constants.TILE_SIZE, 0, 180);

            //top
            batch.draw((i % 2) != 0 ? lightEdge : darkEdge, i * Constants.TILE_SIZE, Constants.SCREEN_HEIGHT - Constants.TILE_SIZE, 0);
        }
    }

    private void drawInternal(CustomSpriteBatch batch) {
        for (int i = 1; i < 7; i++) {
            for (int j = 1; j < 7; j++) {
                batch.draw(i % 2 != j % 2 ? darkInternal : lightInternal, i * Constants.TILE_SIZE, j * Constants.TILE_SIZE);
            }
        }
    }
}
