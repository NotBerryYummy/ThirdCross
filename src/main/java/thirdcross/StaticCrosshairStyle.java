package thirdcross;

import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.TranslatableEnum;
import org.jetbrains.annotations.NotNull;

public enum StaticCrosshairStyle implements TranslatableEnum {
    CROSS,
    CIRCLE;

    @Override
    @NotNull
    public Component getTranslatedName() {
        return Component.translatable(
                "thirdcross.configuration.staticCrosshairStyle." + name().toLowerCase()
        );
    }
}