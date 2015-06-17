package tubeTransportSystem.item;

import tubeTransportSystem.util.Utilities;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemStationHorizontal extends ItemBlockWithMetadata {
    public ItemStationHorizontal(Block b) {
        super(b, b);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        ForgeDirection d = Utilities.entityGetDirection(player).getOpposite();
        int x2 = x + d.offsetX, y2 = y + d.offsetY, z2  = z + d.offsetZ;
        metadata = d.ordinal();
        
        if (!world.isAirBlock(x2, y2, z2))
            return false;

        if (world.setBlock(x, y, z, field_150939_a, metadata, 3) && world.setBlock(x2, y2, z2, field_150939_a, metadata, 3)) {
            if (world.getBlock(x, y, z) == field_150939_a) {
                field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
                field_150939_a.onPostBlockPlaced(world, x, y, z, metadata);
            }

            if (world.getBlock(x, y + 1, z) == field_150939_a) {
                field_150939_a.onBlockPlacedBy(world, x2, y2, z2, player, stack);
                field_150939_a.onPostBlockPlaced(world, x2, y2, z2, metadata);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
        boolean supr = super.onItemUse(stack, player, world, x, y, z, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_);
        
        if (supr) {
            ForgeDirection d = Utilities.entityGetDirection(player).getOpposite();
            
            if (world.isAirBlock(x + d.offsetX, y + 1, z + d.offsetZ))
                return true;
        }

        return false;
    }

    @Override
    public boolean func_150936_a(World world, int x, int y, int z, int p_150936_5_, EntityPlayer player, ItemStack stack) {
        boolean supr = super.func_150936_a(world, x, y, z, p_150936_5_, player, stack);
        
        if (supr) {
            ForgeDirection d = Utilities.entityGetDirection(player).getOpposite();
            
            if (world.isAirBlock(x + d.offsetX, y + 1, z + d.offsetZ))
                return true;
        }

        return false;
    }
}
