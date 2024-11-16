package com.mygdx.game.Components;

public class AbilityComponent implements Component{
    public final AbilityType abilityType;
    public enum AbilityType{
        CRITICAL,
        SHIELDED,
        WEAK
    }

    public AbilityComponent(AbilityType abilityType) {
        this.abilityType = abilityType;
    }
}
