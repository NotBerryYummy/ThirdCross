package thirdcross;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Vector4f;
import thirdcross.aim.PlayerAim;

@EventBusSubscriber(modid = ThirdCross.MODID, value = Dist.CLIENT)
public class AimProjection {

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

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) {
            return;
        }

        valid = false;

        final Minecraft minecraft = Minecraft.getInstance();
        final CameraType currentCameraType = minecraft.options.getCameraType();

        if (currentCameraType != lastCameraType || minecraft.level != lastLevel) {
            resetAimState();
            lastCameraType = currentCameraType;
            lastLevel = minecraft.level;
        }

        final float partialTicks =
                event.getPartialTick().getGameTimeDeltaPartialTick(false);

        final HitResult aimResult = PlayerAim.getAimResult(partialTicks);

        if (aimResult == null || minecraft.player == null) {
            return;
        }

        final Vec3 aimPoint = aimResult.getLocation();
        targetType = aimResult.getType();

        targetDistance = minecraft.player.getEyePosition(partialTicks)
                .distanceTo(aimPoint);

        final Vec3 cameraPosition = event.getCamera().getPosition();

        final float relativeX = (float) (aimPoint.x - cameraPosition.x);
        final float relativeY = (float) (aimPoint.y - cameraPosition.y);
        final float relativeZ = (float) (aimPoint.z - cameraPosition.z);

        final Vector4f position = new Vector4f(
                relativeX,
                relativeY,
                relativeZ,
                1.0f
        );

        position.mul(event.getModelViewMatrix());
        position.mul(event.getProjectionMatrix());

        if (position.w <= 0.0f) {
            resetAimState();
            return;
        }

        final float normalizedX = position.x / position.w;
        final float normalizedY = position.y / position.w;

        if (normalizedX < -1.0f || normalizedX > 1.0f
                || normalizedY < -1.0f || normalizedY > 1.0f) {
            resetAimState();
            return;
        }

        final int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        final int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        final float targetX = (normalizedX + 1.0f) * 0.5f * screenWidth;
        final float targetY = (1.0f - normalizedY) * 0.5f * screenHeight;

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

        final float distance = (float) Math.hypot(
                targetX - smoothedX,
                targetY - smoothedY
        );

        if (distance > SNAP_DISTANCE) {
            smoothedX = targetX;
            smoothedY = targetY;
            return;
        }

        final float smoothingFactor = getSmoothingFactor();

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

    public static float getCrosshairSize() {
        if (targetType == null) {
            return DEFAULT_CROSSHAIR_SIZE;
        }

        if (targetType == HitResult.Type.MISS) {
            return Config.DISTANCE_BASED_AIM_SIZE.get()
                    ? 0.0f
                    : DEFAULT_CROSSHAIR_SIZE;
        }

        if (!Config.DISTANCE_BASED_AIM_SIZE.get()) {
            return DEFAULT_CROSSHAIR_SIZE;
        }

        final Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return DEFAULT_CROSSHAIR_SIZE;
        }

        final double interactionRange = switch (targetType) {
            case BLOCK -> minecraft.player.blockInteractionRange();
            case ENTITY -> minecraft.player.entityInteractionRange();
            default -> 0.0D;
        };

        if (interactionRange <= 0.0D) {
            return DEFAULT_CROSSHAIR_SIZE;
        }

        if (targetDistance >= interactionRange) {
            return MINIMUM_CROSSHAIR_SIZE;
        }

        final float progress = (float) (targetDistance / interactionRange);

        return MAXIMUM_CROSSHAIR_SIZE
                - (MAXIMUM_CROSSHAIR_SIZE - MINIMUM_CROSSHAIR_SIZE) * progress;
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