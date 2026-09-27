package thirdcross;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public final class FabricThirdCrossClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FabricConfig.initialize();

        ClientTickEvents.END_CLIENT_TICK.register(
                PerspectiveHandler::update
        );

        WorldRenderEvents.LAST.register(context ->
                AimProjection.update(
                        context.tickCounter()
                                .getGameTimeDeltaPartialTick(false),
                        context.camera(),
                        context.positionMatrix(),
                        context.projectionMatrix()
                )
        );
    }
}