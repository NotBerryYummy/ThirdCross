package thirdcross;

import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.TranslatableEnum;

public enum AimSmoothing implements TranslatableEnum {
    OFF,
    LOW,
    MEDIUM,
    HIGH;

    @Override
    public Component getTranslatedName() {
        return Component.translatable(
                "thirdcross.configuration.aimSmoothing." + name().toLowerCase()
        );
    }
}
