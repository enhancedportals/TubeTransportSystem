package enhanced.tts;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
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
import enhanced.base.mod.BaseMod;
import enhanced.base.repack.codechicken.lib.raytracer.RayTracer;
import enhanced.tts.block.BlockStation;
import enhanced.tts.block.BlockStationHorizontal;
import enhanced.tts.block.BlockTube;
import enhanced.tts.item.ItemTube;
import enhanced.tts.network.ProxyCommon;

@Mod(modid = TubeTransportSystem.MOD_ID, version = TubeTransportSystem.MOD_VERSION, name = TubeTransportSystem.MOD_NAME, dependencies = TubeTransportSystem.MOD_DEPENDENCIES)
public class TubeTransportSystem extends BaseMod {
    public static final String MOD_NAME = "Tube Transport System", MOD_ID = "tts", MOD_ID_SHORT = "tts", MOD_VERSION = "0.6", MOD_DEPENDENCIES = "required-after:enhancedcore", MOD_URL = "https://raw.githubusercontent.com/enhancedportals/VERSION/master/VERSION%20-%20Tube%20Transport%20System";

    @Instance(MOD_ID)
    public static TubeTransportSystem instance;

    @SidedProxy(clientSide = "enhanced.tts.network.ProxyClient", serverSide = "enhanced.tts.network.ProxyCommon")
    public static ProxyCommon proxy;

    public TubeTransportSystem() {
        super(MOD_URL, MOD_ID, MOD_ID_SHORT, MOD_NAME, MOD_VERSION);
    }

    // Startup

    @EventHandler
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        creativeTab.setItem(new ItemStack(ItemTube.instance, 1, 0));
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event, proxy);
    }

    // World Events

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onBlockHighlight(DrawBlockHighlightEvent event) {
        if (event.target.typeOfHit == MovingObjectType.BLOCK) {
            Block b = event.player.worldObj.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ);

            if (b == BlockStation.instance || b == BlockStationHorizontal.instance || b == BlockTube.instance)
                RayTracer.retraceBlock(event.player.worldObj, event.player, event.target.blockX, event.target.blockY, event.target.blockZ);

            ItemStack stack = event.player.inventory.mainInventory[event.player.inventory.currentItem];

            if (stack != null && stack.getItem() == ItemTube.instance)
                proxy.lastSideHit = event.target.sideHit;
        }
    }
}
