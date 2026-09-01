package thirdcross;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = ThirdCross.MODID, value = Dist.CLIENT)
public class CrosshairRenderer {

    private static final ResourceLocation CROSSHAIR_SPRITE =
            ResourceLocation.withDefaultNamespace("hud/crosshair");

    private static final ResourceLocation STATIC_CIRCLE_SPRITE =
            ResourceLocation.fromNamespaceAndPath(
                    ThirdCross.MODID,
                    "crosshair/static_circle"
            );

    private static final int CROSSHAIR_SIZE = 15;

    @SubscribeEvent
    public static void onRenderGuiLayer(RenderGuiLayerEvent.Pre event) {
        if (!event.getName().equals(VanillaGuiLayers.CROSSHAIR)) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.options.hideGui) {
            return;
        }

        CameraType cameraType = minecraft.options.getCameraType();
        CrosshairMode mode = getCrosshairMode(cameraType);

        // ThirdCross takes full control of the vanilla crosshair layer.
        event.setCanceled(true);

        if (mode == CrosshairMode.OFF) {
            return;
        }

        if (cameraType == CameraType.THIRD_PERSON_FRONT
                && !Config.CROSSHAIR_IN_BACK_PERSPECTIVE.get()) {
            return;
        }

        GuiGraphics guiGraphics = event.getGuiGraphics();

        switch (mode) {
            case STATIC -> renderStaticCrosshair(
                    guiGraphics,
                    StaticCrosshairStyle.CROSS,
                    Config.CROSSHAIR_TINT.get()
            );

            case AIM -> renderAimCrosshair(
                    guiGraphics,
                    Config.CROSSHAIR_TINT.get()
            );

            case STATIC_AIM -> {
                renderStaticCrosshair(
                        guiGraphics,
                        Config.STATIC_CROSSHAIR_STYLE.get(),
                        false
                );
                renderAimCrosshair(guiGraphics, false);
            }
        }
    }

    private static CrosshairMode getCrosshairMode(CameraType cameraType) {
        if (cameraType.isFirstPerson()) {
            return Config.FIRST_PERSON_CROSSHAIR.get();
        }

        return Config.CROSSHAIR_MODE.get();
    }

    private static void renderStaticCrosshair(
            GuiGraphics guiGraphics,
            StaticCrosshairStyle style,
            boolean tint
    ) {
        if (tint) {
            beginCrosshairTint();
        }

        int x = (guiGraphics.guiWidth() - CROSSHAIR_SIZE) / 2;
        int y = (guiGraphics.guiHeight() - CROSSHAIR_SIZE) / 2;

        ResourceLocation sprite = style == StaticCrosshairStyle.CIRCLE
                ? STATIC_CIRCLE_SPRITE
                : CROSSHAIR_SPRITE;

        guiGraphics.blitSprite(
                sprite,
                x,
                y,
                CROSSHAIR_SIZE,
                CROSSHAIR_SIZE
        );

        if (tint) {
            endCrosshairTint();
        }
    }

    private static void renderAimCrosshair(
            GuiGraphics guiGraphics,
            boolean tint
    ) {
        if (!AimProjection.isValid()) {
            return;
        }

        float aimSize = AimProjection.getCrosshairSize();

        if (aimSize <= 0.0f) {
            return;
        }

        if (tint) {
            beginCrosshairTint();
        }

        int size = Math.round(aimSize);

        if ((size & 1) == 0) {
            size--;
        }

        float centeredSize = size;

        int x = Math.round(
                AimProjection.getScreenX() - centeredSize / 2.0f
        );
        int y = Math.round(
                AimProjection.getScreenY() - centeredSize / 2.0f
        );

        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getGuiSprites()
                .getSprite(CROSSHAIR_SPRITE);

        guiGraphics.blit(
                x,
                y,
                0,
                size,
                size,
                sprite
        );

        if (tint) {
            endCrosshairTint();
        }
    }

    private static void beginCrosshairTint() {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
                GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );
    }

    private static void endCrosshairTint() {
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}