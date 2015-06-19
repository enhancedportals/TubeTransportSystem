package tubeTransportSystem.block;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.util.ForgeDirection;
import tubeTransportSystem.TubeTransportSystem;
import tubeTransportSystem.client.RenderStation;
import tubeTransportSystem.item.ItemStation;
import tubeTransportSystem.network.ProxyClient;
import tubeTransportSystem.repack.codechicken.lib.raytracer.IndexedCuboid6;
import tubeTransportSystem.repack.codechicken.lib.raytracer.RayTracer;
import tubeTransportSystem.repack.codechicken.lib.vec.BlockCoord;
import tubeTransportSystem.repack.codechicken.lib.vec.Cuboid6;
import tubeTransportSystem.repack.codechicken.lib.vec.Vector3;
import tubeTransportSystem.util.IConnectable;
import tubeTransportSystem.util.Utilities;

public class BlockStation extends Block implements IConnectable {
    public static BlockStation instance;
    public static IIcon side1, side2, side3, side4;
    public static IIcon entr1, entr2, entr3, entr4;
    public static IIcon side_misc;

    public static final int SHIFT = 8;

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
        side1 = iconRegister.registerIcon(TubeTransportSystem.MOD_ID + ":station_side1");
        side2 = iconRegister.registerIcon(TubeTransportSystem.MOD_ID + ":station_side2");
        side3 = iconRegister.registerIcon(TubeTransportSystem.MOD_ID + ":station_side3");
        side4 = iconRegister.registerIcon(TubeTransportSystem.MOD_ID + ":station_side4");
        entr1 = iconRegister.registerIcon(TubeTransportSystem.MOD_ID + ":station_entr1");
        entr2 = iconRegister.registerIcon(TubeTransportSystem.MOD_ID + ":station_entr2");
        entr3 = iconRegister.registerIcon(TubeTransportSystem.MOD_ID + ":station_entr3");
        entr4 = iconRegister.registerIcon(TubeTransportSystem.MOD_ID + ":station_entr4");
        side_misc = iconRegister.registerIcon(TubeTransportSystem.MOD_ID + ":station_misc");
    }

    @Override
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int s) {
        int meta = blockAccess.getBlockMetadata(x, y, z);

        if (meta == ForgeDirection.NORTH.ordinal() && s == 2)
            return entr1;
        else if (meta == ForgeDirection.NORTH.ordinal() + SHIFT && s == 2)
            return entr2;
        else if (meta == ForgeDirection.EAST.ordinal() && s == 5)
            return entr1;
        else if (meta == ForgeDirection.EAST.ordinal() + SHIFT && s == 5)
            return entr2;
        else if (meta == ForgeDirection.SOUTH.ordinal() && s == 3)
            return entr1;
        else if (meta == ForgeDirection.SOUTH.ordinal() + SHIFT && s == 3)
            return entr2;
        else if (meta == ForgeDirection.WEST.ordinal() && s == 4)
            return entr1;
        else if (meta == ForgeDirection.WEST.ordinal() + SHIFT && s == 4)
            return entr2;

        return s == 0 || s == 1 ? side_misc : meta >= SHIFT ? side1 : side2;
    }

    @Override
    public IIcon getIcon(int s, int m) {
        return s == 2 ? ItemStation.entrance : ItemStation.side;
    }
    
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        return super.getDrops(world, x, y, z, 0, fortune);
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {
        List<IndexedCuboid6> cuboids = new LinkedList<IndexedCuboid6>();
        Utilities.addCuboidsForRaytraceStation(cuboids, world, x, y, z);
        return Utilities.rayTracer.rayTraceCuboids(new Vector3(start), new Vector3(end), cuboids, new BlockCoord(x, y, z), this);
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
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int s) {
        if (s > 1 && s <= 5)
            return true;

        if (blockAccess.getBlock(x, y, z) == this)
            if (blockAccess.getBlockMetadata(x, y, z) < SHIFT && s == 1)
                return true;
            else
                return false;

        if (blockAccess.getBlock(x, y, z) == BlockTube.instance)
            return false;
        else
            return true;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
        
        return super.getCollisionBoundingBoxFromPool(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
    }
    
    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity) {
        setBlockBounds(0, 0, 0, 1, 1, 1);
        if (entity == null) return;
        int meta = world.getBlockMetadata(x, y, z);
        List<AxisAlignedBB> axis = new ArrayList<AxisAlignedBB>();

        if (meta == ForgeDirection.NORTH.ordinal() || meta == ForgeDirection.NORTH.ordinal() + SHIFT) {
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.SOUTH));
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.EAST));
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.WEST));
        } else if (meta == ForgeDirection.SOUTH.ordinal() || meta == ForgeDirection.SOUTH.ordinal() + SHIFT) {
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.NORTH));
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.EAST));
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.WEST));
        } else if (meta == ForgeDirection.EAST.ordinal() || meta == ForgeDirection.EAST.ordinal() + SHIFT) {
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.WEST));
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.NORTH));
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.SOUTH));
        } else if (meta == ForgeDirection.WEST.ordinal() || meta == ForgeDirection.WEST.ordinal() + SHIFT) {
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.EAST));
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.NORTH));
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.SOUTH));
        }

        if (meta >= SHIFT) { // top
            if (entity.isSneaking() && world.getBlockMetadata(x, y + 1, z) == ForgeDirection.UP.ordinal())
                axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.UP));
            else if (world.getBlock(x, y + 1, z) != BlockTube.instance)
                axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.UP));
        } else if (entity.posY >= y)
            if (entity.isSneaking() && world.getBlockMetadata(x, y - 1, z) == ForgeDirection.DOWN.ordinal())
                axis.add(Utilities.getCollisionBoxPartFloor(x, y, z));
            else if (world.getBlock(x, y - 1, z) != BlockTube.instance)
                axis.add(Utilities.getCollisionBoxPartFloor(x, y, z));
            else if (world.getBlockMetadata(x, y - 1, z) != ForgeDirection.DOWN.ordinal())
                axis.add(Utilities.getCollisionBoxPartFloor(x, y, z));

        for (AxisAlignedBB a : axis)
            if (a != null && axisAlignedBB.intersectsWith(a))
                list.add(a);
    }

    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
        int meta = world.getBlockMetadata(x, y, z);
        
        if (meta >= SHIFT && world.getBlock(x, y - 1, z) == this)
            world.setBlock(x, y - 1, z, Blocks.air);
        else if (meta < SHIFT && world.getBlock(x, y + 1, z) == this)
            world.setBlock(x, y + 1, z, Blocks.air);

        super.onBlockExploded(world, x, y, z, explosion);
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
        if (meta >= SHIFT && world.getBlock(x, y - 1, z) == this)
            world.setBlock(x, y - 1, z, Blocks.air);
        else if (meta < SHIFT && world.getBlock(x, y + 1, z) == this)
            world.setBlock(x, y + 1, z, Blocks.air);
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return false;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (entity == null) return;
        int meta = world.getBlockMetadata(x, y, z);

        if (!entity.isSneaking() && meta >= SHIFT && world.getBlock(x, y + 1, z) == BlockTube.instance && world.getBlockMetadata(x, y + 1, z) == ForgeDirection.UP.ordinal()) {
            Utilities.entityAccelerate(entity, ForgeDirection.UP);
            Utilities.entityAccelerate(entity, ForgeDirection.UP);
        }

        Utilities.entityLimitSpeed(entity);
    }

    @Override
    public boolean canConnectTo(IBlockAccess blockAccess, int x, int y, int z, ForgeDirection d) {
        if (d != ForgeDirection.UP && d != ForgeDirection.DOWN) return false;
        Block block = blockAccess.getBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
        int meta = blockAccess.getBlockMetadata(x + d.offsetX, y + d.offsetY, z + d.offsetZ), thisMeta = blockAccess.getBlockMetadata(x, y, z);
        return block == this && thisMeta >= SHIFT ? meta == thisMeta - SHIFT : thisMeta + SHIFT == meta;
    }
    
    @Override
    public boolean canConnectToStrict(IBlockAccess blockAccess, int x, int y, int z, ForgeDirection d) {
        return canConnectTo(blockAccess, x, y, z, d);
    }
}
