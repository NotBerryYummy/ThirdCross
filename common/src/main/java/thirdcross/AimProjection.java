package thirdcross;

import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import thirdcross.aim.PlayerAim;

public final class AimProjection {

    private static final float SNAP_DISTANCE = 100.0f;
    private static final float DEFAULT_CROSSHAIR_SIZE = 15.0f;
    private static final float MAXIMUM_CROSSHAIR_SIZE = 19.0f;
    private static final float MINIMUM_CROSSHAIR_SIZE = 9.0f;

    private static float screenX;
    private static float screenY;
    private static boolean valid;

    private static HitResult.Type targetType;
    private static double targetDistance;

    private static float smoothedX;
    private static float smoothedY;
    private static boolean hasSmoothedPosition;

    private static CameraType lastCameraType;
    private static Level lastLevel;

    private AimProjection() {
    }

    public static void update(
            float partialTicks,
            Camera camera,
            Matrix4f modelViewMatrix,
            Matrix4f projectionMatrix
    ) {
        valid = false;

        Minecraft minecraft = Minecraft.getInstance();
        CameraType currentCameraType = minecraft.options.getCameraType();

        if (currentCameraType != lastCameraType || minecraft.level != lastLevel) {
            resetAimState();
            lastCameraType = currentCameraType;
            lastLevel = minecraft.level;
        }

        HitResult aimResult = PlayerAim.getAimResult(partialTicks);

        if (aimResult == null || minecraft.player == null) {
            return;
        }

        Vec3 aimPoint = aimResult.getLocation();
        targetType = aimResult.getType();

        targetDistance = minecraft.player.getEyePosition(partialTicks)
                .distanceTo(aimPoint);

        Vec3 cameraPosition = camera.getPosition();

        float relativeX = (float) (aimPoint.x - cameraPosition.x);
        float relativeY = (float) (aimPoint.y - cameraPosition.y);
        float relativeZ = (float) (aimPoint.z - cameraPosition.z);

        Vector4f position = new Vector4f(
                relativeX,
                relativeY,
                relativeZ,
                1.0f
        );

        position.mul(modelViewMatrix);
        position.mul(projectionMatrix);

        if (position.w <= 0.0f) {
            resetAimState();
            return;
        }

        float normalizedX = position.x / position.w;
        float normalizedY = position.y / position.w;

        if (normalizedX < -1.0f || normalizedX > 1.0f
                || normalizedY < -1.0f || normalizedY > 1.0f) {
            resetAimState();
            return;
        }

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        float targetX = (normalizedX + 1.0f) * 0.5f * screenWidth;
        float targetY = (1.0f - normalizedY) * 0.5f * screenHeight;

        updateSmoothedPosition(targetX, targetY);

        screenX = smoothedX;
        screenY = smoothedY;
        valid = true;
    }

    private static void updateSmoothedPosition(float targetX, float targetY) {
        if (!hasSmoothedPosition) {
            smoothedX = targetX;
            smoothedY = targetY;
            hasSmoothedPosition = true;
            return;
        }

        float distance = (float) Math.hypot(
                targetX - smoothedX,
                targetY - smoothedY
        );

        if (distance > SNAP_DISTANCE) {
            smoothedX = targetX;
            smoothedY = targetY;
            return;
        }

        float smoothingFactor = getSmoothingFactor();

        smoothedX += (targetX - smoothedX) * smoothingFactor;
        smoothedY += (targetY - smoothedY) * smoothingFactor;
    }

    private static float getSmoothingFactor() {
        return switch (Config.AIM_SMOOTHING.get()) {
            case OFF -> 1.0f;
            case LOW -> 0.5f;
            case MEDIUM -> 0.25f;
            case HIGH -> 0.12f;
        };
    }

    private static void resetAimState() {
        hasSmoothedPosition = false;
        smoothedX = 0.0f;
        smoothedY = 0.0f;
        targetType = null;
        targetDistance = 0.0D;
    }

    public static float getCrosshairSize(float baselineSize) {
        if (targetType == null) {
            return baselineSize;
        }

        if (targetType == HitResult.Type.MISS) {
            return Config.DISTANCE_BASED_AIM_SIZE.get()
                    ? 0.0f
                    : baselineSize;
        }

        if (!Config.DISTANCE_BASED_AIM_SIZE.get()) {
            return baselineSize;
        }

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return baselineSize;
        }

        double interactionRange = switch (targetType) {
            case BLOCK -> minecraft.player.blockInteractionRange();
            case ENTITY -> minecraft.player.entityInteractionRange();
            default -> 0.0D;
        };

        if (interactionRange <= 0.0D) {
            return baselineSize;
        }

        float distanceBasedSize;

        if (targetDistance >= interactionRange) {
            distanceBasedSize = MINIMUM_CROSSHAIR_SIZE;
        } else {
            float progress = (float) (targetDistance / interactionRange);

            distanceBasedSize =
                    MAXIMUM_CROSSHAIR_SIZE
                            - (MAXIMUM_CROSSHAIR_SIZE - MINIMUM_CROSSHAIR_SIZE)
                            * progress;
        }

        float distanceMultiplier =
                distanceBasedSize / DEFAULT_CROSSHAIR_SIZE;

        return baselineSize * distanceMultiplier;
    }

    public static boolean isValid() {
        return valid;
    }

    public static float getScreenX() {
        return screenX;
    }

    public static float getScreenY() {
        return screenY;
    }
}
