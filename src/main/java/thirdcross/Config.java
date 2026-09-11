package thirdcross;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class Config {

    private Config() {
    }

    private static final ModConfigSpec.Builder BUILDER =
            new ModConfigSpec.Builder();

    public static final ModConfigSpec.EnumValue<CrosshairMode> FIRST_PERSON_CROSSHAIR =
            BUILDER
                    .comment("Crosshair mode used in first-person view.")
                    .defineEnum("firstPersonCrosshair", CrosshairMode.STATIC);

    public static final ModConfigSpec.EnumValue<CrosshairMode> CROSSHAIR_MODE =
            BUILDER
                    .comment("Third-person crosshair mode.")
                    .defineEnum("crosshairMode", CrosshairMode.STATIC);

    public static final ModConfigSpec.EnumValue<StaticCrosshairStyle> STATIC_CROSSHAIR_STYLE =
            BUILDER
                    .comment("Style of the static crosshair when using Static + Aim.")
                    .defineEnum("staticCrosshairStyle", StaticCrosshairStyle.CROSS);

    public static final ModConfigSpec.EnumValue<AimSmoothing> AIM_SMOOTHING =
            BUILDER
                    .comment("Smoothing applied to the Player Aim crosshair.")
                    .defineEnum("aimSmoothing", AimSmoothing.MEDIUM);

    public static final ModConfigSpec.BooleanValue DISTANCE_BASED_AIM_SIZE =
            BUILDER
                    .comment("Scale the Player Aim crosshair based on target distance.")
                    .define("distanceBasedAimSize", false);

    public static final ModConfigSpec.BooleanValue CROSSHAIR_TINT =
            BUILDER
                    .comment("Apply vanilla crosshair tinting.")
                    .define("crosshairTint", true);

    public static final ModConfigSpec.BooleanValue SKIP_BACK_PERSPECTIVE =
            BUILDER
                    .comment("Skip the back-facing third-person camera when cycling perspectives.")
                    .define("skipBackPerspective", false);

    public static final ModConfigSpec.BooleanValue CROSSHAIR_IN_BACK_PERSPECTIVE =
            BUILDER
                    .comment("Show the crosshair while using the back-facing third-person camera.")
                    .define("crosshairInBackPerspective", false);

    static final ModConfigSpec SPEC = BUILDER.build();
}
