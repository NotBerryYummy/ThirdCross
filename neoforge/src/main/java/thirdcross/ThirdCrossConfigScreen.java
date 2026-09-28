package thirdcross;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ThirdCrossConfigScreen extends ConfigurationScreen.ConfigurationSectionScreen {

    private AbstractWidget aimSmoothingWidget;
    private AbstractWidget distanceBasedSizeWidget;
    private AbstractWidget crosshairTintWidget;
    private AbstractWidget crosshairBackWidget;
    private AbstractWidget staticCrosshairStyleWidget;

    public ThirdCrossConfigScreen(
            Screen parent,
            ModConfig.Type type,
            ModConfig modConfig,
            Component title
    ) {
        super(parent, type, modConfig, title);
    }

    @Override
    protected <T extends Enum<T>> Element createEnumValue(
            @NotNull String key,
            @NotNull ModConfigSpec.ValueSpec spec,
            @NotNull Supplier<T> source,
            @NotNull Consumer<T> target
    ) {
        Element element = super.createEnumValue(
                key,
                spec,
                source,
                value -> {
                    target.accept(value);
                    updateWidgetStates();
                }
        );

        if (element == null) {
            return null;
        }

        AbstractWidget widget = createWidget(element);

        if (key.equals("aimSmoothing")) {
            aimSmoothingWidget = widget;
        }

        if (key.equals("staticCrosshairStyle")) {
            staticCrosshairStyleWidget = widget;
        }

        updateWidgetStates();

        return createElement(element, widget);
    }

    @Override
    protected Element createBooleanValue(
            @NotNull String key,
            @NotNull ModConfigSpec.ValueSpec spec,
            @NotNull Supplier<Boolean> source,
            @NotNull Consumer<Boolean> target
    ) {
        Element element = super.createBooleanValue(
                key,
                spec,
                source,
                value -> {
                    target.accept(value);
                    updateWidgetStates();
                }
        );

        if (element == null) {
            return null;
        }

        AbstractWidget widget = createWidget(element);

        if (key.equals("distanceBasedAimSize")) {
            distanceBasedSizeWidget = widget;
        }

        if (key.equals("crosshairTint")) {
            crosshairTintWidget = widget;
        }

        if (key.equals("crosshairInBackPerspective")) {
            crosshairBackWidget = widget;
        }

        updateWidgetStates();

        return createElement(element, widget);
    }

    private AbstractWidget createWidget(Element element) {
        return element.getWidget(Minecraft.getInstance().options);
    }

    private Element createElement(Element original, AbstractWidget widget) {
        return new Element(
                original.name(),
                original.tooltip(),
                widget,
                original.undoable()
        );
    }

    private void updateWidgetStates() {
        CrosshairMode firstPersonMode = Config.FIRST_PERSON_CROSSHAIR.get();
        CrosshairMode thirdPersonMode = Config.CROSSHAIR_MODE.get();

        boolean hasAimCrosshair =
                usesAimCrosshair(firstPersonMode)
                        || usesAimCrosshair(thirdPersonMode);

        boolean hasCrosshair =
                firstPersonMode != CrosshairMode.OFF
                        || thirdPersonMode != CrosshairMode.OFF;

        boolean hasStaticAim =
                firstPersonMode == CrosshairMode.STATIC_AIM
                        || thirdPersonMode == CrosshairMode.STATIC_AIM;

        boolean backFacingAvailable =
                !Config.SKIP_BACK_PERSPECTIVE.get();

        if (aimSmoothingWidget != null) {
            aimSmoothingWidget.active = hasAimCrosshair;
        }

        if (distanceBasedSizeWidget != null) {
            distanceBasedSizeWidget.active = hasAimCrosshair;
        }

        if (crosshairTintWidget != null) {
            crosshairTintWidget.active =
                    hasCrosshair
                            && canUseTint(firstPersonMode, thirdPersonMode);
        }

        if (staticCrosshairStyleWidget != null) {
            staticCrosshairStyleWidget.active = hasStaticAim;
        }

        if (crosshairBackWidget != null) {
            crosshairBackWidget.active =
                    thirdPersonMode != CrosshairMode.OFF
                            && backFacingAvailable;
        }
    }

    private boolean usesAimCrosshair(CrosshairMode mode) {
        return mode == CrosshairMode.AIM
                || mode == CrosshairMode.STATIC_AIM;
    }

    private boolean canUseTint(
            CrosshairMode firstPersonMode,
            CrosshairMode thirdPersonMode
    ) {
        return firstPersonMode == CrosshairMode.STATIC
                || firstPersonMode == CrosshairMode.AIM
                || thirdPersonMode == CrosshairMode.STATIC
                || thirdPersonMode == CrosshairMode.AIM;
    }
}
