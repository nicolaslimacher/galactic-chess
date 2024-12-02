package com.mygdx.game.Manager;

import static java.lang.Long.parseLong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import java.util.Random;

public class PRNGManager {
    private static final String TAG = PRNGManager.class.getSimpleName();

    private final Random random;

    private boolean useUserProvidedSeed;
    private long originalSeed;
    private long userProvidedSeed;

    public enum PRNGType{
        //need more? itemRewardSeed? enemyAIseed?
        moveCardSeed(0L),
        enemyEncounterSeed(0L),
        enemyStatsSeed(0L),
        pawnRewardSeed(0L),
        enemyAISeed(0L);

        private long seedValue;

        PRNGType(long seedValue){
            this.seedValue = seedValue;
        }

        public void setSeedValue(long SeedValue){
            this.seedValue = SeedValue;
        }
        public long getSeedValue(){
            return seedValue;
        }
    }

    public long getOriginalSeed() {
        return originalSeed;
    }

    public boolean usingUserProvidedSeed() {
        return useUserProvidedSeed;
    }

    public PRNGManager() {
        this.random = new Random(System.currentTimeMillis());
        //using next long because i'm not sure system.currentTimeMillis is same format?
        //want seeds to be consistent and positive
        this.originalSeed = Math.abs(random.nextLong());
        setSpecificSeedsFromPrimary(originalSeed);
        userProvidedSeed = 0L;
        useUserProvidedSeed = false;
    }

    private void setSpecificSeedsFromPrimary(long seed){
        random.setSeed(seed);
        PRNGType.moveCardSeed.setSeedValue(random.nextLong());
        PRNGType.enemyEncounterSeed.setSeedValue(random.nextLong());
        PRNGType.enemyStatsSeed.setSeedValue(random.nextLong());
        PRNGType.pawnRewardSeed.setSeedValue(random.nextLong());
        PRNGType.enemyAISeed.setSeedValue(random.nextLong());
    }

    public void setSeedToPlayerSeed(String playerSeed){
        userProvidedSeed = parseLong(playerSeed);
        useUserProvidedSeed = true;
        setSpecificSeedsFromPrimary(userProvidedSeed);
        Gdx.app.debug(TAG, "set to player seed: " + userProvidedSeed + ", using user provided seed? " + useUserProvidedSeed);
    }

    public void revertPlayerSeed(){
        userProvidedSeed = 0L;
        useUserProvidedSeed = false;
        setSpecificSeedsFromPrimary(originalSeed);
        Gdx.app.debug(TAG, "reverting to original seed: " + originalSeed + ", using user provided seed? " + useUserProvidedSeed);
    }

    public void updateFromSave(long originalSeed, long moveCardSeed, long enemyEncounterSeed, long pawnRewardSeed, long enemyAISeed){
        this.originalSeed = originalSeed;
        PRNGType.moveCardSeed.setSeedValue(moveCardSeed);
        PRNGType.enemyEncounterSeed.setSeedValue(enemyEncounterSeed);
        PRNGType.pawnRewardSeed.setSeedValue(pawnRewardSeed);
        PRNGType.enemyAISeed.setSeedValue(enemyAISeed);
    }

    public int getNextRand(PRNGType prngType, int upperBound){
        random.setSeed(prngType.getSeedValue());
        long nextSeed = random.nextLong();
        prngType.setSeedValue(nextSeed);
        Gdx.app.debug(TAG, "nextSeed % upperBound: " + nextSeed % upperBound);
        int rand = ((int) (Math.abs(nextSeed) % upperBound) + 1);
        Gdx.app.debug(TAG, "rand for type: " + prngType + " (upperBound: " + upperBound + ") generated: " + rand);
        return rand;
    }

    public int getNextRand(PRNGType prngType, int lowerBound, int upperBound){
        random.setSeed(prngType.getSeedValue());
        long nextSeed = random.nextLong();
        prngType.setSeedValue(nextSeed);
        int rand = lowerBound + (int) (Math.abs(nextSeed) % upperBound) + 1;
        Gdx.app.debug(TAG, "rand for type: " + prngType + " (lowerBound: " + lowerBound +", upperBound: " + upperBound + ") generated: " + rand);
        return rand;
    }
}
