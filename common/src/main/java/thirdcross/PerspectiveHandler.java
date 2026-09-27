package thirdcross;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;

public final class PerspectiveHandler {

    private PerspectiveHandler() {
    }

    public static void update(Minecraft minecraft) {
        if (!Config.SKIP_BACK_PERSPECTIVE.get()) {
            return;
        }

        CameraType cameraType = minecraft.options.getCameraType();

        if (cameraType == CameraType.THIRD_PERSON_FRONT) {
            minecraft.options.setCameraType(CameraType.FIRST_PERSON);
        }
    }
}
