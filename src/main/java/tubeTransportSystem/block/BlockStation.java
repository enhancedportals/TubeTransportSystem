package tubeTransportSystem.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tubeTransportSystem.TubeTransportSystem;
import tubeTransportSystem.client.RenderStation;
import tubeTransportSystem.item.ItemStation;
import tubeTransportSystem.network.ProxyClient;
import tubeTransportSystem.network.ProxyCommon;

public class BlockStation extends Block {
	public static BlockStation instance;
	IIcon side_top, side_bot;
	IIcon entr_top, entr_bot;
	IIcon side_misc;

	public BlockStation(String n) {
		super(Material.rock);
		setBlockName(n);
		instance = this;
		setLightOpacity(1);
		setHardness(5f);
		setCreativeTab(TubeTransportSystem.creativeTab);
	}
	
	@Override
	public int getRenderType() {
		return RenderStation.ID;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		side_top = iconRegister.registerIcon(TubeTransportSystem.MOD_ID + ":station_side_top");
		side_bot = iconRegister.registerIcon(TubeTransportSystem.MOD_ID + ":station_side_bot");
		entr_top = iconRegister.registerIcon(TubeTransportSystem.MOD_ID + ":station_entr_top");
		entr_bot = iconRegister.registerIcon(TubeTransportSystem.MOD_ID + ":station_entr_bot");
		side_misc = iconRegister.registerIcon(TubeTransportSystem.MOD_ID + ":station_misc");
	}
	
	@Override
	public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int s) {
		int meta = blockAccess.getBlockMetadata(x, y, z);
		
		if (meta == 0 && s == 2)
			return entr_bot;
		else if (meta == 4 && s == 2)
			return entr_top;
		else if (meta == 1 && s == 5)
			return entr_bot;
		else if (meta == 5 && s == 5)
			return entr_top;
		else if (meta == 2 && s == 3)
			return entr_bot;
		else if (meta == 6 && s == 3)
			return entr_top;
		else if (meta == 3 && s == 4)
			return entr_bot;
		else if (meta == 7 && s == 4)
			return entr_top;
		
		return s == 0 || s == 1 ? side_misc : meta > 3 ? side_top : side_bot;
	}
	
	@Override
	public IIcon getIcon(int s, int m) {
		return s == 2 ? ItemStation.entrance : ItemStation.side;
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}
		
	@Override
	public boolean isNormalCube() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean canRenderInPass(int pass) {
		ProxyClient.renderPass = pass;
		return pass < 2;
	}
	
	@Override
	public int getRenderBlockPass() {
		return 1;
	}
	
	@Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
        int facing = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

        if (world.getBlock(x, y - 1, z) == this && world.getBlockMetadata(x, y - 1, z) <= 3)
        	facing += 4;
        
        world.setBlockMetadataWithNotify(x, y, z, facing, 3);
    }
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int s) {
		if (s > 1 && s <= 5)
			return true;
		
		if (blockAccess.getBlock(x, y, z) == this)
			if (blockAccess.getBlockMetadata(x, y, z) <= 3 && s == 1)
				return true;
			else
				return false;
		
		if (blockAccess.getBlock(x, y, z) == BlockTube.instance)
			return false;
		else
			return true;
	}
	
	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity) {
		int meta = world.getBlockMetadata(x, y, z);
		List<AxisAlignedBB> axis = new ArrayList<AxisAlignedBB>();
		
		if (entity == null || entity instanceof EntityItem)
			return;

		if (meta == 0 || meta == 4) {
			axis.add(AxisAlignedBB.getBoundingBox(x, y, z, x + 0.1, y + 1, z + 1)); // X
			axis.add(AxisAlignedBB.getBoundingBox(x + 0.9, y, z, x + 1, y + 1, z + 1)); // X
			axis.add(AxisAlignedBB.getBoundingBox(x, y, z + 0.9, x + 1, y + 1, z + 1)); // Z
		} else if (meta == 1 || meta == 5) {
			axis.add(AxisAlignedBB.getBoundingBox(x, y, z, x + 0.1, y + 1, z + 1)); // X
			axis.add(AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 0.1)); // Z
			axis.add(AxisAlignedBB.getBoundingBox(x, y, z + 0.9, x + 1, y + 1, z + 1)); // Z
		}  else if (meta == 2 || meta == 6) {
			axis.add(AxisAlignedBB.getBoundingBox(x, y, z, x + 0.1, y + 1, z + 1)); // X
			axis.add(AxisAlignedBB.getBoundingBox(x + 0.9, y, z, x + 1, y + 1, z + 1)); // X
			axis.add(AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 0.1)); // Z
		} else if (meta == 3 || meta == 7) {
			axis.add(AxisAlignedBB.getBoundingBox(x + 0.9, y, z, x + 1, y + 1, z + 1)); // X
			axis.add(AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 0.1)); // Z
			axis.add(AxisAlignedBB.getBoundingBox(x, y, z + 0.9, x + 1, y + 1, z + 1)); // Z
		}
		
		if (meta > 3) { // top
				if (entity.isSneaking() && world.getBlockMetadata(x, y + 1, z) == ForgeDirection.UP.ordinal()) {
					axis.add(AxisAlignedBB.getBoundingBox(x, y + 0.9, z, x + 1, y + 1, z + 1));
				} else if (world.getBlock(x, y + 1, z) != BlockTube.instance) {
					axis.add(AxisAlignedBB.getBoundingBox(x, y + 0.9, z, x + 1, y + 1, z + 1));
				}
		} else { // bottom
			if (entity.posY >= y) {
				if (entity.isSneaking() && world.getBlockMetadata(x, y - 1, z) == ForgeDirection.DOWN.ordinal()) {
					axis.add(AxisAlignedBB.getBoundingBox(x, y - 0.01, z, x + 1, y, z + 1));
				} else if (world.getBlock(x, y - 1, z) != BlockTube.instance) {
					axis.add(AxisAlignedBB.getBoundingBox(x, y - 0.01, z, x + 1, y, z + 1));
				} else if (world.getBlockMetadata(x, y - 1, z) != ForgeDirection.DOWN.ordinal()) {
					axis.add(AxisAlignedBB.getBoundingBox(x, y - 0.01, z, x + 1, y, z + 1));
				}
			}
		}
		
		for (AxisAlignedBB a : axis) {
			if (a != null && axisAlignedBB.intersectsWith(a))
				list.add(a);
		}
	}
		
	@Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
		int meta = world.getBlockMetadata(x, y, z);
		
		if (meta > 3 && world.getBlock(x, y - 1, z) == this)
			world.setBlock(x, y - 1, z, Blocks.air);
		else if (meta <= 3 && world.getBlock(x, y + 1, z) == this)
			world.setBlock(x, y + 1, z, Blocks.air);
		
		super.onBlockExploded(world, x, y, z, explosion);
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
		if (meta > 3 && world.getBlock(x, y - 1, z) == this)
			world.setBlock(x, y - 1, z, Blocks.air);
		else if (meta <= 3 && world.getBlock(x, y + 1, z) == this)
			world.setBlock(x, y + 1, z, Blocks.air);
	}
	
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return false;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		int meta = world.getBlockMetadata(x, y, z);
		
		if (entity == null)
			return;
		
		entity.motionX = MathHelper.clamp_double(entity.motionX, ProxyCommon.CONFIG_MAX_SPEED_INVERSE, ProxyCommon.CONFIG_MAX_SPEED);
		entity.motionY = MathHelper.clamp_double(entity.motionY, ProxyCommon.CONFIG_MAX_SPEED_INVERSE, ProxyCommon.CONFIG_MAX_SPEED);
		entity.motionZ = MathHelper.clamp_double(entity.motionZ, ProxyCommon.CONFIG_MAX_SPEED_INVERSE, ProxyCommon.CONFIG_MAX_SPEED);
		
		if (!entity.isSneaking() && meta > 3 && world.getBlock(x, y + 1, z) == BlockTube.instance && world.getBlockMetadata(x, y + 1, z) == ForgeDirection.UP.ordinal()) {
			entity.addVelocity(0, 0.2, 0);
		}
	}
}
