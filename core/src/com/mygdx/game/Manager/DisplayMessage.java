package com.mygdx.game.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.mygdx.game.Utils.Constants;

public class DisplayMessage extends Actor {
    private final BattleManager battleManager;
    private final BitmapFont bitmapFont;
    String headerMessage, subMessage;

    public DisplayMessage(BattleManager battleManager, String headerMessage, String subMessage, float displayTime) {
        this.battleManager = battleManager;
        this.bitmapFont = new BitmapFont(Gdx.files.internal("fonts/il-grinta-large.fnt"));
        this.headerMessage = headerMessage;
        this.subMessage = subMessage;

        battleManager.getStage().addActor(this);

        RunnableAction destroy = new RunnableAction();
        destroy.setRunnable(DisplayMessage.this::remove);
        this.addAction(new SequenceAction(
                new DelayAction(displayTime),
                destroy
        ));
    }

    public DisplayMessage(BattleManager battleManager, String headerMessage, float displayTime) {
        this.battleManager = battleManager;
        this.bitmapFont = new BitmapFont(Gdx.files.internal("fonts/il-grinta-large.fnt"));
        this.headerMessage = headerMessage;

        battleManager.getStage().addActor(this);

        RunnableAction destroy = new RunnableAction();
        destroy.setRunnable(DisplayMessage.this::remove);
        this.addAction(new SequenceAction(
                new DelayAction(displayTime),
                destroy
        ));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Constants.SLIME_GREEN.getRed(), Constants.SLIME_GREEN.getGreen(), Constants.SLIME_GREEN.getBlue(), 1f);
        bitmapFont.getData().setScale(2f);
        bitmapFont.draw(batch, headerMessage, Constants.SCREEN_WIDTH / 2 - headerMessage.length()*50f/2, Constants.SCREEN_HEIGHT / 3 * 2);
        if(subMessage != null && !subMessage.trim().isEmpty()) {
            float subTextScale = 0.5f;
            bitmapFont.getData().setScale(subTextScale);
            bitmapFont.draw(batch, subMessage, Constants.SCREEN_WIDTH / 2 - subMessage.length()*20f/2, Constants.SCREEN_HEIGHT / 3 * 2 - 95f);
        }
    }
}