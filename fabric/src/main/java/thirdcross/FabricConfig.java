package thirdcross;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FabricConfig {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final Path CONFIG_PATH =
            FabricLoader.getInstance()
                    .getConfigDir()
                    .resolve("thirdcross.json");

    private static final ConfigData DEFAULTS = new ConfigData();

    private static ConfigData config;

    private FabricConfig() {
    }

    public static void initialize() {
        config = load();

        Config.FIRST_PERSON_CROSSHAIR =
                () -> config.firstPersonCrosshair;

        Config.CROSSHAIR_MODE =
                () -> config.crosshairMode;

        Config.STATIC_CROSSHAIR_STYLE =
                () -> config.staticCrosshairStyle;

        Config.AIM_SMOOTHING =
                () -> config.aimSmoothing;

        Config.DISTANCE_BASED_AIM_SIZE =
                () -> config.distanceBasedAimSize;

        Config.CROSSHAIR_TINT =
                () -> config.crosshairTint;

        Config.SKIP_BACK_PERSPECTIVE =
                () -> config.skipBackPerspective;

        Config.CROSSHAIR_IN_BACK_PERSPECTIVE =
                () -> config.crosshairInBackPerspective;
    }

    private static ConfigData load() {
        if (!Files.exists(CONFIG_PATH)) {
            save(DEFAULTS);
            return copy(DEFAULTS);
        }

        try {
            String content = Files.readString(CONFIG_PATH);

            // Remove // comments before parsing as JSON.
            content = content.replaceAll("(?m)//.*$", "");

            ConfigData loaded = GSON.fromJson(
                    content,
                    ConfigData.class
            );

            if (loaded == null) {
                save(DEFAULTS);
                return copy(DEFAULTS);
            }

            return loaded;
        } catch (IOException | JsonParseException | IllegalStateException e) {
            save(DEFAULTS);
            return copy(DEFAULTS);
        }
    }

    public static void save() {
        save(config);
    }

    private static void save(ConfigData config) {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());

            String content = """
                    {
                      // Crosshair shown in first-person.
                      // Options: STATIC, AIM, STATIC_AIM, OFF
                      "firstPersonCrosshair": "%s",

                      // Crosshair mode used in third-person.
                      // Options: STATIC, AIM, STATIC_AIM, OFF
                      "crosshairMode": "%s",

                      // Shape of the static crosshair in STATIC_AIM mode.
                      // Options: CROSS, CIRCLE
                      "staticCrosshairStyle": "%s",

                      // Smoothing applied to the aim crosshair.
                      // Options: OFF, LOW, MEDIUM, HIGH
                      "aimSmoothing": "%s",

                      // Scale the aim crosshair based on target distance.
                      // Options: true, false
                      "distanceBasedAimSize": %s,

                      // Apply vanilla crosshair tinting.
                      // Options: true, false
                      "crosshairTint": %s,

                      // Skip the back-facing third-person camera when cycling perspectives.
                      // Options: true, false
                      "skipBackPerspective": %s,

                      // Show the crosshair while using the back-facing third-person camera.
                      // Options: true, false
                      "crosshairInBackPerspective": %s
                    }
                    """.formatted(
                    config.firstPersonCrosshair,
                    config.crosshairMode,
                    config.staticCrosshairStyle,
                    config.aimSmoothing,
                    config.distanceBasedAimSize,
                    config.crosshairTint,
                    config.skipBackPerspective,
                    config.crosshairInBackPerspective
            );

            Files.writeString(CONFIG_PATH, content);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to save ThirdCross configuration",
                    e
            );
        }
    }

    private static ConfigData copy(ConfigData source) {
        ConfigData copy = new ConfigData();

        copy.firstPersonCrosshair = source.firstPersonCrosshair;
        copy.crosshairMode = source.crosshairMode;
        copy.staticCrosshairStyle = source.staticCrosshairStyle;
        copy.aimSmoothing = source.aimSmoothing;
        copy.distanceBasedAimSize = source.distanceBasedAimSize;
        copy.crosshairTint = source.crosshairTint;
        copy.skipBackPerspective = source.skipBackPerspective;
        copy.crosshairInBackPerspective = source.crosshairInBackPerspective;

        return copy;
    }

    public static CrosshairMode getFirstPersonCrosshair() {
        return config.firstPersonCrosshair;
    }

    public static void setFirstPersonCrosshair(CrosshairMode value) {
        config.firstPersonCrosshair = value;
    }

    public static CrosshairMode getCrosshairMode() {
        return config.crosshairMode;
    }

    public static void setCrosshairMode(CrosshairMode value) {
        config.crosshairMode = value;
    }

    public static StaticCrosshairStyle getStaticCrosshairStyle() {
        return config.staticCrosshairStyle;
    }

    public static void setStaticCrosshairStyle(StaticCrosshairStyle value) {
        config.staticCrosshairStyle = value;
    }

    public static AimSmoothing getAimSmoothing() {
        return config.aimSmoothing;
    }

    public static void setAimSmoothing(AimSmoothing value) {
        config.aimSmoothing = value;
    }

    public static boolean getDistanceBasedAimSize() {
        return config.distanceBasedAimSize;
    }

    public static void setDistanceBasedAimSize(boolean value) {
        config.distanceBasedAimSize = value;
    }

    public static boolean getCrosshairTint() {
        return config.crosshairTint;
    }

    public static void setCrosshairTint(boolean value) {
        config.crosshairTint = value;
    }

    public static boolean getSkipBackPerspective() {
        return config.skipBackPerspective;
    }

    public static void setSkipBackPerspective(boolean value) {
        config.skipBackPerspective = value;
    }

    public static boolean getCrosshairInBackPerspective() {
        return config.crosshairInBackPerspective;
    }

    public static void setCrosshairInBackPerspective(boolean value) {
        config.crosshairInBackPerspective = value;
    }

    private static final class ConfigData {

        private CrosshairMode firstPersonCrosshair =
                CrosshairMode.STATIC;

        private CrosshairMode crosshairMode =
                CrosshairMode.STATIC;

        private StaticCrosshairStyle staticCrosshairStyle =
                StaticCrosshairStyle.CROSS;

        private AimSmoothing aimSmoothing =
                AimSmoothing.MEDIUM;

        private boolean distanceBasedAimSize = false;

        private boolean crosshairTint = true;

        private boolean skipBackPerspective = false;

        private boolean crosshairInBackPerspective = false;
    }
}