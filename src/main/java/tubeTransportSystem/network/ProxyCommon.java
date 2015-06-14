package tubeTransportSystem.network;

import java.io.File;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.config.Configuration;
import tubeTransportSystem.block.BlockStation;
import tubeTransportSystem.block.BlockTube;
import tubeTransportSystem.item.ItemStation;
import tubeTransportSystem.item.ItemTube;
import cpw.mods.fml.common.registry.GameRegistry;

public class ProxyCommon {
    Configuration config;
    public static double CONFIG_MAX_SPEED = 0.5;
    public static double CONFIG_MAX_SPEED_INVERSE = -0.5;

    public void setupConfig(File file) {
        config = new Configuration(file);
        CONFIG_MAX_SPEED = MathHelper.clamp_double(config.get("General", "MaxTubeSpeed", 0.5, "The maximum speed an entity can travel through the Transport Tubes").getDouble(), 0, 10);
        CONFIG_MAX_SPEED_INVERSE = -CONFIG_MAX_SPEED;
    }

    public void registerBlocks() {
        GameRegistry.registerBlock(new BlockTube("tube"), ItemTube.class, "tube");
        GameRegistry.registerBlock(new BlockStation("station"), ItemStation.class, "station");
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
}
