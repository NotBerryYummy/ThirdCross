package thirdcross.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.CameraType;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thirdcross.Config;
import thirdcross.CrosshairMode;
import thirdcross.CrosshairRenderer;
import thirdcross.CrosshairSize;
import thirdcross.StaticCrosshairStyle;

@SuppressWarnings("unused")
@Mixin(Gui.class)
public class GuiMixin {

    private static final ResourceLocation VANILLA_CROSSHAIR =
            ResourceLocation.withDefaultNamespace("hud/crosshair");

    @Redirect(
            method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFuncSeparate(Lcom/mojang/blaze3d/platform/GlStateManager$SourceFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DestFactor;Lcom/mojang/blaze3d/platform/GlStateManager$SourceFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DestFactor;)V"
            )
    )
    private void thirdcross$controlVanillaCrosshairTint(
            GlStateManager.SourceFactor srcRgb,
            GlStateManager.DestFactor dstRgb,
            GlStateManager.SourceFactor srcAlpha,
            GlStateManager.DestFactor dstAlpha
    ) {
        Minecraft minecraft = Minecraft.getInstance();

        CrosshairMode mode = minecraft.options.getCameraType().isFirstPerson()
                ? Config.FIRST_PERSON_CROSSHAIR.get()
                : Config.CROSSHAIR_MODE.get();

        boolean tint = mode != CrosshairMode.STATIC_AIM
                && mode != CrosshairMode.OFF
                && Config.CROSSHAIR_TINT.get();

        if (tint) {
            RenderSystem.blendFuncSeparate(
                    srcRgb,
                    dstRgb,
                    srcAlpha,
                    dstAlpha
            );
        } else {
            RenderSystem.defaultBlendFunc();
        }
    }

    @Redirect(
            method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V"
            )
    )
    private void thirdcross$handleVanillaCrosshair(
            GuiGraphics guiGraphics,
            ResourceLocation sprite,
            int x,
            int y,
            int width,
            int height
    ) {
        Minecraft minecraft = Minecraft.getInstance();

        if (!VANILLA_CROSSHAIR.equals(sprite)) {
            guiGraphics.blitSprite(sprite, x, y, width, height);
            return;
        }

        if (!minecraft.options.getCameraType().isFirstPerson()) {
            CrosshairMode mode = Config.CROSSHAIR_MODE.get();

            if (mode == CrosshairMode.OFF) {
                return;
            }

            if (minecraft.options.getCameraType() == CameraType.THIRD_PERSON_FRONT
                    && !Config.CROSSHAIR_IN_BACK_PERSPECTIVE.get()) {
                return;
            }

            float baselineSize = CrosshairSize.getBaselineSize(width, height);

            switch (mode) {
                case STATIC -> guiGraphics.blitSprite(
                        sprite,
                        x,
                        y,
                        width,
                        height
                );

                case AIM -> CrosshairRenderer.renderAimCrosshair(
                        guiGraphics,
                        Config.CROSSHAIR_TINT.get(),
                        baselineSize
                );

                case STATIC_AIM -> {
                    CrosshairRenderer.renderAimCrosshair(
                            guiGraphics,
                            false,
                            baselineSize
                    );

                    if (Config.STATIC_CROSSHAIR_STYLE.get()
                            == StaticCrosshairStyle.CROSS) {
                        guiGraphics.blitSprite(
                                sprite,
                                x,
                                y,
                                width,
                                height
                        );
                    } else {
                        CrosshairRenderer.renderStaticCrosshair(
                                guiGraphics,
                                StaticCrosshairStyle.CIRCLE,
                                false
                        );
                    }
                }
            }

            return;
        }

        CrosshairMode mode = Config.FIRST_PERSON_CROSSHAIR.get();

        if (mode == CrosshairMode.STATIC) {
            guiGraphics.blitSprite(
                    sprite,
                    x,
                    y,
                    width,
                    height
            );
            return;
        }

        if (mode == CrosshairMode.AIM) {
            float baselineSize = CrosshairSize.getBaselineSize(width, height);

            CrosshairRenderer.renderAimCrosshair(
                    guiGraphics,
                    Config.CROSSHAIR_TINT.get(),
                    baselineSize
            );
            return;
        }

        if (mode == CrosshairMode.STATIC_AIM) {
            float baselineSize = CrosshairSize.getBaselineSize(width, height);

            CrosshairRenderer.renderAimCrosshair(
                    guiGraphics,
                    false,
                    baselineSize
            );

            if (Config.STATIC_CROSSHAIR_STYLE.get()
                    == StaticCrosshairStyle.CROSS) {
                guiGraphics.blitSprite(
                        sprite,
                        x,
                        y,
                        width,
                        height
                );
            } else {
                CrosshairRenderer.renderStaticCrosshair(
                        guiGraphics,
                        StaticCrosshairStyle.CIRCLE,
                        false
                );
            }
        }
    }

    @Redirect(
            method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/CameraType;isFirstPerson()Z"
            )
    )
    private boolean thirdcross$allowThirdPersonCrosshair(
            CameraType cameraType
    ) {
        return true;
    }

    @Inject(
            method = "renderCrosshair",
            at = @At("HEAD"),
            cancellable = true
    )
    private void thirdcross$disableCrosshairWhenOff(
            GuiGraphics guiGraphics,
            DeltaTracker deltaTracker,
            CallbackInfo ci
    ) {
        Minecraft minecraft = Minecraft.getInstance();

        CrosshairMode mode = minecraft.options.getCameraType().isFirstPerson()
                ? Config.FIRST_PERSON_CROSSHAIR.get()
                : Config.CROSSHAIR_MODE.get();

        if (mode == CrosshairMode.OFF) {
            ci.cancel();
        }
    }
}
