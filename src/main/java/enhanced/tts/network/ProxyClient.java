package enhanced.tts.network;

import cpw.mods.fml.client.registry.RenderingRegistry;
import enhanced.tts.client.RenderStation;
import enhanced.tts.client.RenderTube;

public class ProxyClient extends ProxyCommon {
    @Override
    public void postInit() {
        super.postInit();
        RenderTube.ID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(RenderTube.ID, new RenderTube());
        RenderStation.ID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(RenderStation.ID, new RenderStation());
    }
}
