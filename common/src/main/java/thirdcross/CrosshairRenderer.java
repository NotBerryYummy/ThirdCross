package thirdcross;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

public class CrosshairRenderer {

    private static final ResourceLocation CROSSHAIR_SPRITE =
            ResourceLocation.withDefaultNamespace("hud/crosshair");

    private static final ResourceLocation STATIC_CIRCLE_SPRITE =
            ResourceLocation.fromNamespaceAndPath(
                    ModConstants.MOD_ID,
                    "crosshair/static_circle"
            );

    private static final int CROSSHAIR_SIZE = 15;

    public static void renderStaticCrosshair(
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

    public static void renderAimCrosshair(
            GuiGraphics guiGraphics,
            boolean tint,
            float baselineSize
    ) {
        if (!AimProjection.isValid()) {
            return;
        }

        float aimSize = AimProjection.getCrosshairSize(baselineSize);

        if (aimSize <= 0.0f) {
            return;
        }

        int size = CrosshairSize.toOddSize(aimSize);

        if (size <= 0) {
            return;
        }

        if (tint) {
            beginCrosshairTint();
        }

        int x = Math.round(
                AimProjection.getScreenX() - size / 2.0f
        );

        int y = Math.round(
                AimProjection.getScreenY() - size / 2.0f
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
