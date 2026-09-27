package thirdcross;

import net.minecraft.network.chat.Component;

public enum CrosshairMode {
    OFF,
    STATIC,
    AIM,
    STATIC_AIM;

    public Component getTranslatedName() {
        return Component.translatable(
                "thirdcross.configuration.crosshairMode." + name().toLowerCase()
        );
    }
}