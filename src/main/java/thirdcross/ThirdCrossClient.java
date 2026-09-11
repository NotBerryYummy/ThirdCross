package thirdcross;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = ThirdCross.MODID, dist = Dist.CLIENT)
public class ThirdCrossClient {

    public ThirdCrossClient(ModContainer container) {
        container.registerExtensionPoint(
                IConfigScreenFactory.class,
                (minecraft, parent) -> new ConfigurationScreen(
                        container,
                        parent,
                        ThirdCrossConfigScreen::new
                )
        );
    }
}
