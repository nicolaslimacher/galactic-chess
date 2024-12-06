package com.mygdx.game.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.GamePiece.GamePiece;
import com.mygdx.game.HUD.TTFSkin;
import com.mygdx.game.Utils.Constants;
import com.mygdx.game.Utils.Helpers;

public class DisplayMessage extends Actor {
    private static final String TAG = DisplayMessage.class.getSimpleName();
    Label headerDisplay, subDisplay;
    GlyphLayout glyphLayout;
    TTFSkin ttfSkin = Helpers.getGameSkin();

    public DisplayMessage(BattleManager battleManager, String headerMessage, String subMessage, float displayTime) {
        glyphLayout = new GlyphLayout();

        headerDisplay = new Label(headerMessage, Helpers.getGameSkin(), "headerDisplayMessage");
        headerDisplay.setDebug(true);
        glyphLayout.setText(headerDisplay.getStyle().font, headerMessage);
        headerDisplay.setPosition((Constants.SCREEN_WIDTH / 2) - (glyphLayout.width / 2), Constants.SCREEN_HEIGHT / 2);
        Gdx.app.debug(TAG, "Display label created with text: " + headerDisplay.getText() + " at pos: " + headerDisplay.getX() + "," + headerDisplay.getY());
        float bottomEdge = Constants.SCREEN_HEIGHT / 2 - glyphLayout.height;

        subDisplay = new Label(subMessage, Helpers.getGameSkin(), "subDisplayMessage");
        subDisplay.setDebug(true);
        glyphLayout.setText(subDisplay.getStyle().font, subMessage);
        subDisplay.setPosition((Constants.SCREEN_WIDTH / 2) - (glyphLayout.width / 2), bottomEdge - (glyphLayout.height / 2));
        Gdx.app.debug(TAG, "Sub Display label created with text: " + subDisplay.getText() + " at pos: " + subDisplay.getX() + "," + subDisplay.getY());

        battleManager.getStage().addActor(this);
        RunnableAction destroy = new RunnableAction();
        destroy.setRunnable(DisplayMessage.this::remove);
        this.addAction(new SequenceAction(
                new DelayAction(displayTime),
                destroy
        ));
    }

    public DisplayMessage(BattleManager battleManager, String headerMessage, float displayTime) {
        this(battleManager, headerMessage, "", displayTime);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.headerDisplay.draw(batch, parentAlpha);
        this.subDisplay.draw(batch, parentAlpha);
    }
}