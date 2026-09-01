package thirdcross;

import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.TranslatableEnum;

public enum CrosshairMode implements TranslatableEnum {
    OFF,
    STATIC,
    AIM,
    STATIC_AIM;

    @Override
    public Component getTranslatedName() {
        return Component.translatable(
                "thirdcross.configuration.crosshairMode." + name().toLowerCase()
        );
    }
}