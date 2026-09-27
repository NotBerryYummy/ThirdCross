package thirdcross;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = ThirdCross.MODID, value = Dist.CLIENT)
public final class NeoForgePerspectiveHandler {

    private NeoForgePerspectiveHandler() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        PerspectiveHandler.update(net.minecraft.client.Minecraft.getInstance());
    }
}
