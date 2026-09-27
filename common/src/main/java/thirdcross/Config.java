package thirdcross;

public final class Config {

    private Config() {
    }

    public static ConfigValue<CrosshairMode> FIRST_PERSON_CROSSHAIR =
            () -> CrosshairMode.STATIC;

    public static ConfigValue<CrosshairMode> CROSSHAIR_MODE =
            () -> CrosshairMode.STATIC;

    public static ConfigValue<StaticCrosshairStyle> STATIC_CROSSHAIR_STYLE =
            () -> StaticCrosshairStyle.CROSS;

    public static ConfigValue<AimSmoothing> AIM_SMOOTHING =
            () -> AimSmoothing.MEDIUM;

    public static ConfigValue<Boolean> DISTANCE_BASED_AIM_SIZE =
            () -> false;

    public static ConfigValue<Boolean> CROSSHAIR_TINT =
            () -> true;

    public static ConfigValue<Boolean> SKIP_BACK_PERSPECTIVE =
            () -> false;

    public static ConfigValue<Boolean> CROSSHAIR_IN_BACK_PERSPECTIVE =
            () -> false;
}
