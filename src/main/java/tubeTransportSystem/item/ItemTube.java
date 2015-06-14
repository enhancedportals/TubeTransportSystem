package tubeTransportSystem.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemTube extends ItemBlockWithMetadata {
    public static ItemTube instance;

    public ItemTube(Block b) {
        super(b, b);
        instance = this;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 10));
        list.add(new ItemStack(item, 1, 11));
        list.add(new ItemStack(item, 1, 12));
        list.add(new ItemStack(item, 1, 13));
        list.add(new ItemStack(item, 1, 14));
        list.add(new ItemStack(item, 1, 15));
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
        int meta = stack.getItemDamage();

        if (meta >= 10)
            list.add(String.format(EnumChatFormatting.GRAY + StatCollector.translateToLocal("item.tube.forced"), EnumChatFormatting.AQUA + StatCollector.translateToLocal("item.tube.direction." + (meta - 10))));
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        int meta = metadata >= 10 ? metadata - 10 : side;

        if (!world.setBlock(x, y, z, field_150939_a, meta, 3))
            return false;

        if (world.getBlock(x, y, z) == field_150939_a) {
            field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
            field_150939_a.onPostBlockPlaced(world, x, y, z, meta);
        }

        return true;
    }
}
