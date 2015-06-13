package tubeTransportSystem;

import net.minecraft.creativetab.CreativeTabs;
import tubeTransportSystem.network.ProxyCommon;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = TubeTransportSystem.MOD_ID, version = TubeTransportSystem.MOD_VERSION, name=TubeTransportSystem.MOD_NAME)
public class TubeTransportSystem {
	public static final String MOD_NAME = "Tube Transport System";
    public static final String MOD_ID = "tts";
    public static final String MOD_VERSION = "0.1";
    public static final CreativeTabs creativeTab = new CreativeTab();
    
    @Instance(MOD_ID)
    public static TubeTransportSystem instance;
    
    @SidedProxy(clientSide="tubeTransportSystem.network.ProxyClient", serverSide="tubeTransportSystem.network.ProxyCommon")
    public static ProxyCommon proxy;
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	
    }
    
    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
    	proxy.setupConfig(event.getSuggestedConfigurationFile());
    	proxy.registerBlocks();
    }
    
    @EventHandler
    public void postinit(FMLPostInitializationEvent event) {
    	proxy.miscSetup();
    	proxy.registerCrafting();
    }
}
