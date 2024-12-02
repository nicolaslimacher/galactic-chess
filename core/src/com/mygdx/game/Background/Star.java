package com.mygdx.game.Background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Manager.BattleManager;

public class Star extends Actor {
    BattleManager battleManager;
    long timeAtCreation, timeToBrightest, timeToFade; //in nanos
    StarType starType;

    public Star(BattleManager battleManager, long timeToBrightest, long timeToFade, StarType starType) {
        this.battleManager = battleManager;
        battleManager.getStage().addActor(this);
        this.toBack();
        this.timeAtCreation = TimeUtils.nanoTime();
        this.timeToBrightest = timeToBrightest;
        this.timeToFade = timeToFade;
        this.starType = starType;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //check how opaque star should be
        float alphaDraw = 0f;
        long timeSinceStarCreation = TimeUtils.timeSinceNanos(timeAtCreation);
        if (timeSinceStarCreation < timeToBrightest){
            alphaDraw = (float)(timeSinceStarCreation) / (float)timeToBrightest;
        } else if (timeSinceStarCreation < timeToBrightest + 1000000000L) {
            alphaDraw = 1f;
        } else if (timeSinceStarCreation < timeToFade + 1000000000L) {
            alphaDraw = (1f - ((float)(timeSinceStarCreation - timeToBrightest - 1000000000L) / (float)(timeToFade - timeToBrightest - 1000000000L)));
        } else {
            //destroy if older than timeToFade
            this.remove();
        }

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, alphaDraw);
        if (starType == StarType.SMALL) {
            batch.draw(this.battleManager.smallStar, getX(), getY());
        } else {
            batch.draw(this.battleManager.mediumStar, getX(), getY());
        }
    }
}


