package tubeTransportSystem.network;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tubeTransportSystem.TubeTransportSystem;
import tubeTransportSystem.block.BlockStation;
import tubeTransportSystem.block.BlockStationHorizontal;
import tubeTransportSystem.block.BlockTube;
import tubeTransportSystem.item.ItemStation;
import tubeTransportSystem.item.ItemTube;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

public class ProxyCommon {
    Configuration config;
    public static double CONFIG_MAX_SPEED = 0.5;
    public static double CONFIG_MAX_SPEED_INVERSE = -0.5;
    public static boolean CONFIG_UPDATE = true;
    public static String UPDATE_LATEST_VER = null;
    public int lastSideHit = 0;
    public static Logger logger = LogManager.getLogger("TubeTransportSystem");

    public void setupConfig(File file) {
        config = new Configuration(file);
        CONFIG_MAX_SPEED = MathHelper.clamp_double(config.get("General", "MaxTubeSpeed", 0.5, "The maximum speed an entity can travel through the Transport Tubes").getDouble(), 0, 10);
        CONFIG_MAX_SPEED_INVERSE = -CONFIG_MAX_SPEED;
        CONFIG_UPDATE = config.get("General", "UpdateCheck", true, "Allow checking for updates from " + TubeTransportSystem.UPDATE_URL).getBoolean();
        config.save();
        
        if (CONFIG_UPDATE) {
            try {
                URL versionIn = new URL(TubeTransportSystem.UPDATE_URL);
                BufferedReader in = new BufferedReader(new InputStreamReader(versionIn.openStream()));
                UPDATE_LATEST_VER = in.readLine();
                if (FMLCommonHandler.instance().getSide() == Side.SERVER && !UPDATE_LATEST_VER.equals(TubeTransportSystem.MOD_VERSION))
                    logger.info("You're using an outdated version (v" + TubeTransportSystem.MOD_VERSION + "). The newest version is: " + UPDATE_LATEST_VER);
            } catch (Exception e) {
                logger.warn("Unable to get the latest version information");
                UPDATE_LATEST_VER = TubeTransportSystem.MOD_VERSION;
            }
        }
    }

    public void registerBlocks() {
        GameRegistry.registerBlock(new BlockTube("tube"), ItemTube.class, "tube");
        GameRegistry.registerBlock(new BlockStation("station"), ItemStation.class, "station");
        GameRegistry.registerBlock(new BlockStationHorizontal("station"), "stationH");
    }

    public void registerCrafting() {
        GameRegistry.addShapedRecipe(new ItemStack(BlockTube.instance, 16), "SGS", "GEG", "SGS", 'S', Blocks.stone, 'G', Blocks.glass, 'E', Items.ender_pearl);
        GameRegistry.addShapedRecipe(new ItemStack(BlockStation.instance, 1), "SLS", "S S", "SLS", 'S', Blocks.stone, 'L', new ItemStack(Blocks.stone_slab, 1, 0));

        for (int i = 0; i < 6; i++) {
            GameRegistry.addShapelessRecipe(new ItemStack(BlockTube.instance, 1, 10 + i), new ItemStack(BlockTube.instance, 1, i > 0 ? 10 + i - 1 : i));
            GameRegistry.addShapelessRecipe(new ItemStack(BlockTube.instance, 4, 10 + i), new ItemStack(BlockTube.instance, 1, i > 0 ? 10 + i - 1 : i), new ItemStack(BlockTube.instance, 1, i > 0 ? 10 + i - 1 : i), new ItemStack(BlockTube.instance, 1, i > 0 ? 10 + i - 1 : i), new ItemStack(BlockTube.instance, 1, i > 0 ? 10 + i - 1 : i));
            GameRegistry.addShapelessRecipe(new ItemStack(BlockTube.instance, 9, 10 + i), new ItemStack(BlockTube.instance, 1, i > 0 ? 10 + i - 1 : i), new ItemStack(BlockTube.instance, 1, i > 0 ? 10 + i - 1 : i), new ItemStack(BlockTube.instance, 1, i > 0 ? 10 + i - 1 : i), new ItemStack(BlockTube.instance, 1, i > 0 ? 10 + i - 1 : i), new ItemStack(BlockTube.instance, 1, i > 0 ? 10 + i - 1 : i), new ItemStack(BlockTube.instance, 1, i > 0 ? 10 + i - 1 : i), new ItemStack(BlockTube.instance, 1, i > 0 ? 10 + i - 1 : i), new ItemStack(BlockTube.instance, 1, i > 0 ? 10 + i - 1 : i), new ItemStack(BlockTube.instance, 1, i > 0 ? 10 + i - 1 : i));
        }

        GameRegistry.addShapelessRecipe(new ItemStack(BlockTube.instance, 1, 0), new ItemStack(BlockTube.instance, 1, 15));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockTube.instance, 4, 0), new ItemStack(BlockTube.instance, 1, 15), new ItemStack(BlockTube.instance, 1, 15), new ItemStack(BlockTube.instance, 1, 15), new ItemStack(BlockTube.instance, 1, 15));
        GameRegistry.addShapelessRecipe(new ItemStack(BlockTube.instance, 9, 0), new ItemStack(BlockTube.instance, 1, 15), new ItemStack(BlockTube.instance, 1, 15), new ItemStack(BlockTube.instance, 1, 15), new ItemStack(BlockTube.instance, 1, 15), new ItemStack(BlockTube.instance, 1, 15), new ItemStack(BlockTube.instance, 1, 15), new ItemStack(BlockTube.instance, 1, 15), new ItemStack(BlockTube.instance, 1, 15), new ItemStack(BlockTube.instance, 1, 15));
    }

    public void miscSetup() {
    }

    public static void Notify(EntityPlayer player, String lateVers) {
        player.addChatMessage(new ChatComponentText("Tube Transport System has been updated to v" + lateVers + " :: You are running v" + TubeTransportSystem.MOD_VERSION));
    }
}
