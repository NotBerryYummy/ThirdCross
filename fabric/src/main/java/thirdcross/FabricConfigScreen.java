package thirdcross;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class FabricConfigScreen extends Screen {

    private final Screen parent;

    private Button firstPersonCrosshairButton;
    private Button crosshairModeButton;
    private Button staticCrosshairStyleButton;
    private Button aimSmoothingButton;
    private Button distanceBasedAimSizeButton;
    private Button crosshairTintButton;
    private Button skipBackPerspectiveButton;
    private Button crosshairInBackPerspectiveButton;

    private CrosshairMode firstPersonCrosshair;
    private CrosshairMode crosshairMode;
    private StaticCrosshairStyle staticCrosshairStyle;
    private AimSmoothing aimSmoothing;
    private boolean distanceBasedAimSize;
    private boolean crosshairTint;
    private boolean skipBackPerspective;
    private boolean crosshairInBackPerspective;

    public FabricConfigScreen(Screen parent) {
        super(Component.literal("ThirdCross"));
        this.parent = parent;

        firstPersonCrosshair =
                FabricConfig.getFirstPersonCrosshair();

        crosshairMode =
                FabricConfig.getCrosshairMode();

        staticCrosshairStyle =
                FabricConfig.getStaticCrosshairStyle();

        aimSmoothing =
                FabricConfig.getAimSmoothing();

        distanceBasedAimSize =
                FabricConfig.getDistanceBasedAimSize();

        crosshairTint =
                FabricConfig.getCrosshairTint();

        skipBackPerspective =
                FabricConfig.getSkipBackPerspective();

        crosshairInBackPerspective =
                FabricConfig.getCrosshairInBackPerspective();
    }

    @Override
    protected void init() {
        int buttonHeight = 20;
        int columnGap = 10;

        int buttonWidth = Math.min(
                256,
                (this.width - 30 - columnGap) / 2
        );

        int leftColumn =
                (this.width - buttonWidth * 2 - columnGap) / 2;

        int rightColumn =
                leftColumn + buttonWidth + columnGap;

        int top = 48;
        int rowSpacing = 24;

        // First-person crosshair
        firstPersonCrosshairButton = addRenderableWidget(
                Button.builder(
                        getFirstPersonCrosshairText(),
                        button -> cycleFirstPersonCrosshair()
                ).bounds(
                        leftColumn,
                        top,
                        buttonWidth,
                        buttonHeight
                ).build()
        );

        // Third-person crosshair
        crosshairModeButton = addRenderableWidget(
                Button.builder(
                        getCrosshairModeText(),
                        button -> cycleCrosshairMode()
                ).bounds(
                        rightColumn,
                        top,
                        buttonWidth,
                        buttonHeight
                ).build()
        );

        // Static crosshair style
        staticCrosshairStyleButton = addRenderableWidget(
                Button.builder(
                        getStaticCrosshairStyleText(),
                        button -> cycleStaticCrosshairStyle()
                ).bounds(
                        leftColumn,
                        top + rowSpacing,
                        buttonWidth,
                        buttonHeight
                ).build()
        );

        // Aim smoothing
        aimSmoothingButton = addRenderableWidget(
                Button.builder(
                        getAimSmoothingText(),
                        button -> cycleAimSmoothing()
                ).bounds(
                        rightColumn,
                        top + rowSpacing,
                        buttonWidth,
                        buttonHeight
                ).build()
        );

        // Distance-based aim size
        distanceBasedAimSizeButton = addRenderableWidget(
                Button.builder(
                        getDistanceBasedAimSizeText(),
                        button -> {
                            distanceBasedAimSize =
                                    !distanceBasedAimSize;

                            updateButtonLabels();
                        }
                ).bounds(
                        leftColumn,
                        top + rowSpacing * 2,
                        buttonWidth,
                        buttonHeight
                ).build()
        );

        // Crosshair tint
        crosshairTintButton = addRenderableWidget(
                Button.builder(
                        getCrosshairTintText(),
                        button -> {
                            crosshairTint =
                                    !crosshairTint;

                            updateButtonLabels();
                        }
                ).bounds(
                        rightColumn,
                        top + rowSpacing * 2,
                        buttonWidth,
                        buttonHeight
                ).build()
        );

        // Skip back perspective
        skipBackPerspectiveButton = addRenderableWidget(
                Button.builder(
                        getSkipBackPerspectiveText(),
                        button -> {
                            skipBackPerspective =
                                    !skipBackPerspective;

                            updateButtonLabels();
                            updateButtonStates();
                        }
                ).bounds(
                        leftColumn,
                        top + rowSpacing * 3,
                        buttonWidth,
                        buttonHeight
                ).build()
        );

        // Crosshair in back perspective
        crosshairInBackPerspectiveButton = addRenderableWidget(
                Button.builder(
                        getCrosshairInBackPerspectiveText(),
                        button -> {
                            crosshairInBackPerspective =
                                    !crosshairInBackPerspective;

                            updateButtonLabels();
                        }
                ).bounds(
                        rightColumn,
                        top + rowSpacing * 3,
                        buttonWidth,
                        buttonHeight
                ).build()
        );

        updateButtonStates();

        // Done button
        int doneWidth = 200;
        int doneHeight = 20;
        int doneY = this.height - 30;
        int doneX = (this.width - doneWidth) / 2;

        addRenderableWidget(
                Button.builder(
                        Component.literal("Done"),
                        button -> saveAndClose()
                ).bounds(
                        doneX,
                        doneY,
                        doneWidth,
                        doneHeight
                ).build()
        );
    }

    private void cycleFirstPersonCrosshair() {
        CrosshairMode[] values = CrosshairMode.values();

        firstPersonCrosshair =
                values[
                        (firstPersonCrosshair.ordinal() + 1)
                                % values.length
                        ];

        updateButtonLabels();
        updateButtonStates();
    }

    private void cycleCrosshairMode() {
        CrosshairMode[] values = CrosshairMode.values();

        crosshairMode =
                values[
                        (crosshairMode.ordinal() + 1)
                                % values.length
                        ];

        updateButtonLabels();
        updateButtonStates();
    }

    private void cycleStaticCrosshairStyle() {
        StaticCrosshairStyle[] values =
                StaticCrosshairStyle.values();

        staticCrosshairStyle =
                values[
                        (staticCrosshairStyle.ordinal() + 1)
                                % values.length
                        ];

        updateButtonLabels();
    }

    private void cycleAimSmoothing() {
        AimSmoothing[] values =
                AimSmoothing.values();

        aimSmoothing =
                values[
                        (aimSmoothing.ordinal() + 1)
                                % values.length
                        ];

        updateButtonLabels();
    }

    private void updateButtonLabels() {
        firstPersonCrosshairButton.setMessage(
                getFirstPersonCrosshairText()
        );

        crosshairModeButton.setMessage(
                getCrosshairModeText()
        );

        staticCrosshairStyleButton.setMessage(
                getStaticCrosshairStyleText()
        );

        aimSmoothingButton.setMessage(
                getAimSmoothingText()
        );

        distanceBasedAimSizeButton.setMessage(
                getDistanceBasedAimSizeText()
        );

        crosshairTintButton.setMessage(
                getCrosshairTintText()
        );

        skipBackPerspectiveButton.setMessage(
                getSkipBackPerspectiveText()
        );

        crosshairInBackPerspectiveButton.setMessage(
                getCrosshairInBackPerspectiveText()
        );
    }

    private void updateButtonStates() {
        boolean firstPersonUsesAim =
                firstPersonCrosshair == CrosshairMode.AIM
                        || firstPersonCrosshair == CrosshairMode.STATIC_AIM;

        boolean thirdPersonUsesAim =
                crosshairMode == CrosshairMode.AIM
                        || crosshairMode == CrosshairMode.STATIC_AIM;

        boolean usesStaticAim =
                firstPersonCrosshair == CrosshairMode.STATIC_AIM
                        || crosshairMode == CrosshairMode.STATIC_AIM;

        boolean usesTintableCrosshair =
                firstPersonCrosshair == CrosshairMode.STATIC
                        || firstPersonCrosshair == CrosshairMode.AIM
                        || crosshairMode == CrosshairMode.STATIC
                        || crosshairMode == CrosshairMode.AIM;

        staticCrosshairStyleButton.active =
                usesStaticAim;

        aimSmoothingButton.active =
                firstPersonUsesAim || thirdPersonUsesAim;

        distanceBasedAimSizeButton.active =
                firstPersonUsesAim || thirdPersonUsesAim;

        crosshairTintButton.active =
                usesTintableCrosshair;

        crosshairInBackPerspectiveButton.active =
                crosshairMode != CrosshairMode.OFF
                        && !skipBackPerspective;
    }

    private Component getFirstPersonCrosshairText() {
        return Component.literal(
                "First-Person Crosshair: "
                        + getCrosshairModeName(firstPersonCrosshair)
        );
    }

    private Component getCrosshairModeText() {
        return Component.literal(
                "Third-Person Crosshair: "
                        + getCrosshairModeName(crosshairMode)
        );
    }

    private Component getStaticCrosshairStyleText() {
        return Component.literal(
                "Static Crosshair Style: "
                        + getStaticCrosshairStyleName(
                        staticCrosshairStyle
                )
        );
    }

    private Component getAimSmoothingText() {
        return Component.literal(
                "Aim Smoothing: "
                        + getAimSmoothingName(aimSmoothing)
        );
    }

    private Component getDistanceBasedAimSizeText() {
        return Component.literal(
                "Distance-Based Aim Size: "
                        + getOnOffName(distanceBasedAimSize)
        );
    }

    private Component getCrosshairTintText() {
        return Component.literal(
                "Crosshair Tint: "
                        + getOnOffName(crosshairTint)
        );
    }

    private Component getSkipBackPerspectiveText() {
        return Component.literal(
                "Skip Back Perspective: "
                        + getOnOffName(skipBackPerspective)
        );
    }

    private Component getCrosshairInBackPerspectiveText() {
        return Component.literal(
                "Crosshair in Back Perspective: "
                        + getOnOffName(crosshairInBackPerspective)
        );
    }

    private String getCrosshairModeName(CrosshairMode mode) {
        return switch (mode) {
            case STATIC -> "Static";
            case AIM -> "Aim";
            case STATIC_AIM -> "Static + Aim";
            case OFF -> "Off";
        };
    }

    private String getStaticCrosshairStyleName(
            StaticCrosshairStyle style
    ) {
        return switch (style) {
            case CROSS -> "Cross";
            case CIRCLE -> "Circle";
        };
    }

    private String getAimSmoothingName(AimSmoothing smoothing) {
        return switch (smoothing) {
            case OFF -> "Off";
            case LOW -> "Low";
            case MEDIUM -> "Medium";
            case HIGH -> "High";
        };
    }

    private String getOnOffName(boolean value) {
        return value ? "On" : "Off";
    }

    private void saveAndClose() {
        FabricConfig.setFirstPersonCrosshair(
                firstPersonCrosshair
        );

        FabricConfig.setCrosshairMode(
                crosshairMode
        );

        FabricConfig.setStaticCrosshairStyle(
                staticCrosshairStyle
        );

        FabricConfig.setAimSmoothing(
                aimSmoothing
        );

        FabricConfig.setDistanceBasedAimSize(
                distanceBasedAimSize
        );

        FabricConfig.setCrosshairTint(
                crosshairTint
        );

        FabricConfig.setSkipBackPerspective(
                skipBackPerspective
        );

        FabricConfig.setCrosshairInBackPerspective(
                crosshairInBackPerspective
        );

        FabricConfig.save();

        if (this.minecraft != null) {
            this.minecraft.setScreen(parent);
        }
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(parent);
        }
    }

    @Override
    public void render(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        renderBackground(
                guiGraphics,
                mouseX,
                mouseY,
                partialTick
        );

        // Top panel
        guiGraphics.fill(
                0,
                0,
                this.width,
                32,
                0xCC000000
        );

        // Bottom panel
        guiGraphics.fill(
                0,
                this.height - 32,
                this.width,
                this.height,
                0xCC000000
        );

        // Render buttons
        super.render(
                guiGraphics,
                mouseX,
                mouseY,
                partialTick
        );

        // Top separator
        guiGraphics.fill(
                0,
                31,
                this.width,
                32,
                0xFF555555
        );

        // Bottom separator
        guiGraphics.fill(
                0,
                this.height - 32,
                this.width,
                this.height - 31,
                0xFF555555
        );

        // Title
        guiGraphics.drawCenteredString(
                this.font,
                this.title,
                this.width / 2,
                10,
                0xFFFFFF
        );
    }
}