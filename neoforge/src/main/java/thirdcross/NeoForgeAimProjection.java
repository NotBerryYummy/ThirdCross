package thirdcross;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid = ThirdCross.MODID, value = Dist.CLIENT)
public final class NeoForgeAimProjection {

    private NeoForgeAimProjection() {
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) {
            return;
        }

        AimProjection.update(
                event.getPartialTick().getGameTimeDeltaPartialTick(false),
                event.getCamera(),
                event.getModelViewMatrix(),
                event.getProjectionMatrix()
        );
    }
}
