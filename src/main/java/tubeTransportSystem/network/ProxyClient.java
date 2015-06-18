package tubeTransportSystem.network;

import tubeTransportSystem.client.RenderStation;
import tubeTransportSystem.client.RenderTube;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ProxyClient extends ProxyCommon {
    @Override
    public void miscSetup() {
        super.miscSetup();
        RenderTube.ID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(RenderTube.ID, new RenderTube());
        RenderStation.ID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(RenderStation.ID, new RenderStation());
    }
}
