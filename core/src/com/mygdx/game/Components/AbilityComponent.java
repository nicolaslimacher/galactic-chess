package com.mygdx.game.Components;

public class AbilityComponent implements Component{
    public final AbilityType abilityType;
    public enum AbilityType{
        NULL("null", "No ability"),
        CRITICAL("critical", "First attack deals double damage"),
        SHIELDED("shielded", "First time taking damage is negated"),
        WEAK("weak", "First attack deals half damage");

        private final String name;

        AbilityType(String name, String description) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static AbilityType fromString(String text) {
            for (AbilityType b : AbilityType.values()) {
                if (b.name.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            return AbilityType.NULL;
        }
    }

    public AbilityComponent(AbilityType abilityType) {
        this.abilityType = abilityType;
    }
}
