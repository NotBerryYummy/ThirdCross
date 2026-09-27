package thirdcross;

import net.minecraft.network.chat.Component;

public enum AimSmoothing {
    OFF,
    LOW,
    MEDIUM,
    HIGH;

    public Component getTranslatedName() {
        return Component.translatable(
                "thirdcross.configuration.aimSmoothing." + name().toLowerCase()
        );
    }
}