package tubeTransportSystem;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import tubeTransportSystem.block.BlockStation;
import tubeTransportSystem.block.BlockStationHorizontal;
import tubeTransportSystem.block.BlockTube;
import tubeTransportSystem.item.ItemTube;
import tubeTransportSystem.network.ProxyCommon;
import tubeTransportSystem.repack.codechicken.lib.raytracer.RayTracer;
import tubeTransportSystem.util.CreativeTab;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = TubeTransportSystem.MOD_ID, version = TubeTransportSystem.MOD_VERSION, name = TubeTransportSystem.MOD_NAME)
public class TubeTransportSystem {
    public static final String MOD_NAME = "Tube Transport System";
    public static final String MOD_ID = "tts";
    public static final String MOD_VERSION = "0.5";
    public static final CreativeTabs creativeTab = new CreativeTab();

    @Instance(MOD_ID)
    public static TubeTransportSystem instance;

    @SidedProxy(clientSide = "tubeTransportSystem.network.ProxyClient", serverSide = "tubeTransportSystem.network.ProxyCommon")
    public static ProxyCommon proxy;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
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
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onBlockHighlight(DrawBlockHighlightEvent event) {
        if (event.target.typeOfHit == MovingObjectType.BLOCK) {
            Block b = event.player.worldObj.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ);
            
            if (b == BlockStation.instance || b == BlockStationHorizontal.instance || b == BlockTube.instance)
                RayTracer.retraceBlock(event.player.worldObj, event.player, event.target.blockX, event.target.blockY, event.target.blockZ);
            
            ItemStack stack = event.player.inventory.mainInventory[event.player.inventory.currentItem];
            
            if (stack != null && stack.getItem() == ItemTube.instance) {
                proxy.lastSideHit = event.target.sideHit;
            }
        }
    }
}
