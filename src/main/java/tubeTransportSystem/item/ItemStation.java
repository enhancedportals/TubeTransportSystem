package tubeTransportSystem.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import tubeTransportSystem.TubeTransportSystem;

public class ItemStation extends ItemBlockWithMetadata {
	public static IIcon entrance, side;
	
	public ItemStation(Block b) {
		super(b, b);
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
		if (!world.isAirBlock(x, y + 1, z))
			return false;
		
		if (world.setBlock(x, y, z, field_150939_a, metadata, 3) && world.setBlock(x, y + 1, z, field_150939_a, metadata, 3)) {
			if (world.getBlock(x, y, z) == field_150939_a) {
				field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
				field_150939_a.onPostBlockPlaced(world, x, y, z, metadata);
			}
			
			if (world.getBlock(x, y + 1, z) == field_150939_a) {
				field_150939_a.onBlockPlacedBy(world, x, y + 1, z, player, stack);
				field_150939_a.onPostBlockPlaced(world, x, y + 1, z, metadata);
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int p_77648_7_, float p_77648_8_, float p_77648_9_,float p_77648_10_) {
		boolean supr = super.onItemUse(stack, player, world, x, y, z, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_);
		
		if (supr) {
			if (world.isAirBlock(x, y + 1, z)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean func_150936_a(World world, int x, int y, int z, int p_150936_5_, EntityPlayer player, ItemStack stack) {
		boolean supr = super.func_150936_a(world, x, y, z, p_150936_5_, player, stack);
		
		if (supr) {
			if (world.isAirBlock(x, y + 1, z)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		super.registerIcons(iconRegister);
		entrance = iconRegister.registerIcon(TubeTransportSystem.MOD_ID + ":station1");
		side = iconRegister.registerIcon(TubeTransportSystem.MOD_ID + ":station2");
	}
}
