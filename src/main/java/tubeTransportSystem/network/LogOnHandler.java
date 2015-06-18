package tubeTransportSystem.network;

import net.minecraft.entity.player.EntityPlayer;
import tubeTransportSystem.TubeTransportSystem;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class LogOnHandler {
    boolean displayed;
    
    @SubscribeEvent
    public void onLogIn(PlayerEvent.PlayerLoggedInEvent login) {
        if (!displayed && ProxyCommon.UPDATE_LATEST_VER != null && login.player != null && !ProxyCommon.UPDATE_LATEST_VER.equals(TubeTransportSystem.MOD_VERSION)) {
            EntityPlayer player = login.player;
            String lateVers = ProxyCommon.UPDATE_LATEST_VER;
            ProxyCommon.Notify(player, lateVers);
            displayed = true;
        }
    }
}
