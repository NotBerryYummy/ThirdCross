package thirdcross;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = ThirdCross.MODID, value = Dist.CLIENT)
public class PerspectiveHandler {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (!Config.SKIP_BACK_PERSPECTIVE.get()) {
            return;
        }

        CameraType cameraType = minecraft.options.getCameraType();

        if (cameraType == CameraType.THIRD_PERSON_FRONT) {
            minecraft.options.setCameraType(CameraType.FIRST_PERSON);
        }
    }
}