package thirdcross;

import net.minecraft.network.chat.Component;

public enum StaticCrosshairStyle {
    CROSS,
    CIRCLE;

    public Component getTranslatedName() {
        return Component.translatable(
                "thirdcross.configuration.staticCrosshairStyle." + name().toLowerCase()
        );
    }
}