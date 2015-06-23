package enhanced.tts.network;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import enhanced.base.mod.BaseProxy;
import enhanced.tts.TubeTransportSystem;
import enhanced.tts.block.BlockStation;
import enhanced.tts.block.BlockStationHorizontal;
import enhanced.tts.block.BlockTube;
import enhanced.tts.item.ItemStation;
import enhanced.tts.item.ItemTube;

public class ProxyCommon extends BaseProxy {
    public static double CONFIG_MAX_SPEED = 0.5;
    public static double CONFIG_MAX_SPEED_INVERSE = -0.5;
    public static String UPDATE_LATEST_VER = null;
    public int lastSideHit = 0;

    @Override
    public void registerConfiguration() {
        CONFIG_MAX_SPEED = MathHelper.clamp_double(config.get("General", "MaxTubeSpeed", 0.5, "The maximum speed an entity can travel through the Transport Tubes").getDouble(), 0, 10);
        CONFIG_MAX_SPEED_INVERSE = -CONFIG_MAX_SPEED;
        TubeTransportSystem.instance.CHECK_FOR_UPDATES = config.get("General", "UpdateCheck", true, "Allow checking for updates from " + TubeTransportSystem.MOD_URL).getBoolean();
    }

    @Override
    public void registerBlocks() {
        GameRegistry.registerBlock(new BlockTube("tube"), ItemTube.class, "tube");
        GameRegistry.registerBlock(new BlockStation("station"), ItemStation.class, "station");
        GameRegistry.registerBlock(new BlockStationHorizontal("station"), "stationH");
    }

    @Override
    public void registerRecipes() {
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

    @Override
    public void registerItems() {

    }

    @Override
    public void registerTileEntities() {

    }

    @Override
    public void registerPackets() {

    }
}
