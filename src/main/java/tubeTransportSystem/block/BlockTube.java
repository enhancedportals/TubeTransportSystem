package tubeTransportSystem.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tubeTransportSystem.TubeTransportSystem;
import tubeTransportSystem.client.RenderTube;
import tubeTransportSystem.network.ProxyClient;
import tubeTransportSystem.network.ProxyCommon;

public class BlockTube extends Block {
	public static BlockTube instance;
	IIcon textures[] = new IIcon[7];
	
	public BlockTube(String n) {
		super(Material.rock);
		setBlockName(n);
		instance = this;
		setLightOpacity(1);
		setHardness(5f);
		setCreativeTab(TubeTransportSystem.creativeTab);
	}
	
	@Override
	public int getRenderType() {
		return RenderTube.ID;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		for (int i = 0; i < textures.length; i++) {
			textures[i] = iconRegister.registerIcon(TubeTransportSystem.MOD_ID + ":tube/" + i);
		}
	}
	
	@Override
	public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int s) {
		int meta = blockAccess.getBlockMetadata(x, y, z);
		
		if (meta == 0 || meta == 1) {
			if (s == 0 || s == 1)
				return textures[0];
			
			boolean up = blockAccess.getBlock(x, y + 1, z) == this && blockAccess.getBlockMetadata(x, y + 1, z) == meta;
			boolean down = blockAccess.getBlock(x, y - 1, z) == this && blockAccess.getBlockMetadata(x, y - 1, z) == meta;
			
			if (up && down)
				return textures[5];
			else if (up)
				return textures[6];
			else if (down)
				return textures[4];
			else
				return textures[0];
		} else if (meta == 2 || meta == 3) {
			if (s == 2 || s == 3)
				return textures[0];
			
			boolean up = blockAccess.getBlock(x, y, z + 1) == this && blockAccess.getBlockMetadata(x, y, z + 1) == meta;
			boolean down = blockAccess.getBlock(x, y, z - 1) == this && blockAccess.getBlockMetadata(x, y, z - 1) == meta;
			
			if (up && down)
				return textures[s == 0 || s == 1 ? 5 : 2];
			else if (up)
				return textures[s == 0 || s == 1 ? 4 : s == 4 ? 1 : 3];
			else if (down)
				return textures[s == 0 || s == 1 ? 6 : s == 4 ? 3 : 1];
			else
				return textures[0];
		} else if (meta == 4 || meta == 5) {
			if (s == 4 || s == 5)
				return textures[0];
			
			boolean up = blockAccess.getBlock(x + 1, y, z) == this && blockAccess.getBlockMetadata(x + 1, y, z) == meta;
			boolean down = blockAccess.getBlock(x - 1, y, z) == this && blockAccess.getBlockMetadata(x - 1, y, z) == meta;
			
			if (up && down)
				return textures[2];
			else if (up)
				return textures[s == 2 ? 3 : 1];
			else if (down)
				return textures[s == 2 ? 1 : 3];
			else
				return textures[0];
		}
		
		return textures[0];
	}
	
	@Override
	public IIcon getIcon(int s, int m) {
		return textures[0];
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		int meta = world.getBlockMetadata(x, y, z);
		
		if (entity == null)
			return;
		
		entity.motionX = MathHelper.clamp_double(entity.motionX, ProxyCommon.CONFIG_MAX_SPEED_INVERSE, ProxyCommon.CONFIG_MAX_SPEED);
		entity.motionY = MathHelper.clamp_double(entity.motionY, ProxyCommon.CONFIG_MAX_SPEED_INVERSE, ProxyCommon.CONFIG_MAX_SPEED);
		entity.motionZ = MathHelper.clamp_double(entity.motionZ, ProxyCommon.CONFIG_MAX_SPEED_INVERSE, ProxyCommon.CONFIG_MAX_SPEED);
		
		if (meta == ForgeDirection.DOWN.ordinal())
			entity.addVelocity(0, -0.1, 0);
		else if (meta == ForgeDirection.UP.ordinal())
			entity.addVelocity(0, 0.1, 0);
		else if (meta == ForgeDirection.NORTH.ordinal())
			entity.addVelocity(0, 0, -0.1);
		else if (meta == ForgeDirection.SOUTH.ordinal())
			entity.addVelocity(0, 0, 0.1);
		else if (meta == ForgeDirection.EAST.ordinal())
			entity.addVelocity(0.1, 0, 0);
		else if (meta == ForgeDirection.WEST.ordinal())
			entity.addVelocity(-0.1, 0, 0);
		
		entity.fallDistance = 0f;
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
	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int s) {
		int meta = blockAccess.getBlockMetadata(x, y, z);
		
		if (meta == 0 || meta == 1)
			if (s != 0 && s != 1)
				return true;
		else if (meta == 2 || meta == 3)
			if (s != 2 && s != 3)
				return true;
		else if (meta == 4 || meta == 5)
			if (s != 4 && s != 5)
				return true;
		
		if (blockAccess.getBlock(x, y, z) == this)
			return false;
		
		else return true;
	}
	
	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity) {
		int meta = world.getBlockMetadata(x, y, z);
		List<AxisAlignedBB> axis = new ArrayList<AxisAlignedBB>();
		
		if (entity == null)
			return;
		
		if (meta == ForgeDirection.UP.ordinal() || meta == ForgeDirection.DOWN.ordinal()) {
			axis.add(AxisAlignedBB.getBoundingBox(x, y, z, x + 0.1, y + 1, z + 1)); // X
			axis.add(AxisAlignedBB.getBoundingBox(x + 0.9, y, z, x + 1, y + 1, z + 1)); // X
			axis.add(AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 0.1)); // Z
			axis.add(AxisAlignedBB.getBoundingBox(x, y, z + 0.9, x + 1, y + 1, z + 1)); // Z
		} else if (meta == ForgeDirection.NORTH.ordinal() || meta == ForgeDirection.SOUTH.ordinal()) {
			axis.add(AxisAlignedBB.getBoundingBox(x, y, z, x + 0.1, y + 1, z + 1)); // X
			axis.add(AxisAlignedBB.getBoundingBox(x + 0.9, y, z, x + 1, y + 1, z + 1)); // X
			axis.add(AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 0.1, z + 1)); // Y
			axis.add(AxisAlignedBB.getBoundingBox(x, y + 0.9, z, x + 1, y + 1, z + 1)); // Y
		} else if (meta == ForgeDirection.WEST.ordinal() || meta == ForgeDirection.EAST.ordinal()) {
			axis.add(AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 0.1)); // Z
			axis.add(AxisAlignedBB.getBoundingBox(x, y, z + 0.9, x + 1, y + 1, z + 1)); // Z
			axis.add(AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 0.1, z + 1)); // Y
			axis.add(AxisAlignedBB.getBoundingBox(x, y + 0.9, z, x + 1, y + 1, z + 1)); // Y
		}
		
		for (AxisAlignedBB a : axis) {
			if (a != null && axisAlignedBB.intersectsWith(a))
				list.add(a);
		}
	}
	
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, net.minecraftforge.common.util.ForgeDirection side) {
		return false;
	}
}
