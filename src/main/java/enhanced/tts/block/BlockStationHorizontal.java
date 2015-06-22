package enhanced.tts.block;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import enhanced.base.repack.codechicken.lib.raytracer.IndexedCuboid6;
import enhanced.base.repack.codechicken.lib.vec.BlockCoord;
import enhanced.base.repack.codechicken.lib.vec.Vector3;
import enhanced.tts.client.RenderStation;
import enhanced.tts.item.ItemStation;
import enhanced.tts.util.IConnectable;
import enhanced.tts.util.Utilities;

public class BlockStationHorizontal extends Block implements IConnectable {
    public static BlockStationHorizontal instance;
    public static final int SHIFT = 8;

    public BlockStationHorizontal(String n) {
        super(Material.rock);
        setBlockName(n);
        instance = this;
        setLightOpacity(1);
        setHardness(5f);
    }

    @Override
    public int getRenderType() {
        return RenderStation.ID;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {

    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> list = new ArrayList<ItemStack>();
        list.add(new ItemStack(BlockStation.instance, 1));
        return list;
    }
    
    @Override
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int s) {
        int meta = blockAccess.getBlockMetadata(x, y, z);

        if (meta == ForgeDirection.NORTH.ordinal()) {
            if (s == 1) return BlockStation.entr2;
            else if (s == 0) return BlockStation.side1;
            else if (s == 4) return BlockStation.side4;
            else if (s == 5) return BlockStation.side3;
        } else if (meta == ForgeDirection.NORTH.ordinal() + SHIFT) {
            if (s == 1) return BlockStation.entr1;
            else if (s == 0) return BlockStation.side2;
            else if (s == 4) return BlockStation.side3;
            else if (s == 5) return BlockStation.side4;
        } else if (meta == ForgeDirection.EAST.ordinal()) {
            if (s == 1) return BlockStation.entr3;
            else if (s == 0) return BlockStation.side3;
            else if (s == 2) return BlockStation.side4;
            else if (s == 3) return BlockStation.side3;
        } else if (meta == ForgeDirection.EAST.ordinal() + SHIFT) {
            if (s == 1) return BlockStation.entr4;
            else if (s == 0) return BlockStation.side4;
            else if (s == 2) return BlockStation.side3;
            else if (s == 3) return BlockStation.side4;
        } else if (meta == ForgeDirection.SOUTH.ordinal()) {
            if (s == 1) return BlockStation.entr1;
            else if (s == 0) return BlockStation.side2;
            else if (s == 4) return BlockStation.side3;
            else if (s == 5) return BlockStation.side4;
        } else if (meta == ForgeDirection.SOUTH.ordinal() + SHIFT) {
            if (s == 1) return BlockStation.entr2;
            else if (s == 0) return BlockStation.side1;
            else if (s == 4) return BlockStation.side4;
            else if (s == 5) return BlockStation.side3;
        } else if (meta == ForgeDirection.WEST.ordinal()) {
            if (s == 1) return BlockStation.entr4;
            else if (s == 0) return BlockStation.side4;
            else if (s == 2) return BlockStation.side3;
            else if (s == 3) return BlockStation.side4;
        } else if (meta == ForgeDirection.WEST.ordinal() + SHIFT) {
            if (s == 1) return BlockStation.entr3;
            else if (s == 0) return BlockStation.side3;
            else if (s == 2) return BlockStation.side4;
            else if (s == 3) return BlockStation.side3;
        }
        
        return BlockStation.side_misc;
    }

    @Override
    public IIcon getIcon(int s, int m) {
        return s == 2 ? ItemStation.entrance : ItemStation.side;
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {
        List<IndexedCuboid6> cuboids = new LinkedList<IndexedCuboid6>();
        Utilities.addCuboidsForRaytraceStationHorizontal(cuboids, world, x, y, z);
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

    ChunkCoordinates getPartner(World world, int x, int y, int z) {
        return getPartner(x, y, z, world.getBlockMetadata(x, y, z));
    }
    
    ChunkCoordinates getPartner(int x, int y, int z, int m) {
        ForgeDirection d;
        
        if (m < SHIFT)
            d = ForgeDirection.getOrientation(m).getOpposite();
        else
            d = ForgeDirection.getOrientation(m - SHIFT);

        return new ChunkCoordinates(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
    }
    
    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int s) {
        if (s == 0) return true;
        
        ChunkCoordinates self = Utilities.getCoordinatesFromSide(x, y, z, s);
        int meta = blockAccess.getBlockMetadata(self.posX, self.posY, self.posZ);
        
        if (meta == ForgeDirection.SOUTH.ordinal()) {
            if (s == 3 && blockAccess.getBlock(x, y, z) == BlockTube.instance)
                return false;
            
            return s == 2 ? false : true;
        } else if (meta == ForgeDirection.SOUTH.ordinal() + SHIFT) {
            if (s == 2 && blockAccess.getBlock(x, y, z) == BlockTube.instance)
                return false;
            
            return s == 3 ? false : true;
        } else if (meta == ForgeDirection.NORTH.ordinal()) {
            if (s == 2 && blockAccess.getBlock(x, y, z) == BlockTube.instance)
                return false;
            
            return s == 3 ? false : true;
        } else if (meta == ForgeDirection.NORTH.ordinal() + SHIFT) {
            if (s == 3 && blockAccess.getBlock(x, y, z) == BlockTube.instance)
                return false;
            
            return s == 2 ? false : true;
        } else if (meta == ForgeDirection.EAST.ordinal()) {
            if (s == 5 && blockAccess.getBlock(x, y, z) == BlockTube.instance)
                return false;
            
            return s == 4 ? false : true;
        } else if (meta == ForgeDirection.EAST.ordinal() + SHIFT) {
            if (s == 4 && blockAccess.getBlock(x, y, z) == BlockTube.instance)
                return false;
            
            return s == 5 ? false : true;
        } else if (meta == ForgeDirection.WEST.ordinal()) {
            if (s == 4 && blockAccess.getBlock(x, y, z) == BlockTube.instance)
                return false;
            
            return s == 5 ? false : true;
        } else if (meta == ForgeDirection.WEST.ordinal() + SHIFT) {
            if (s == 5 && blockAccess.getBlock(x, y, z) == BlockTube.instance)
                return false;
            
            return s == 4 ? false : true;
        }
        
        return true;
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity) {
        if (entity == null)
            return;
        
        List<AxisAlignedBB> axis = new ArrayList<AxisAlignedBB>();
        ForgeDirection d;
        int meta = world.getBlockMetadata(x, y, z);

        if (meta == ForgeDirection.NORTH.ordinal() || meta == ForgeDirection.NORTH.ordinal() + SHIFT || meta == ForgeDirection.SOUTH.ordinal() || meta == ForgeDirection.SOUTH.ordinal() + SHIFT) {
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.EAST));
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.WEST));
        } else if (meta == ForgeDirection.EAST.ordinal() || meta == ForgeDirection.EAST.ordinal() + SHIFT || meta == ForgeDirection.WEST.ordinal() || meta == ForgeDirection.WEST.ordinal() + SHIFT) {
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.NORTH));
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.SOUTH));
        }
        
        if (meta >= SHIFT)
            d = ForgeDirection.getOrientation(meta - SHIFT).getOpposite(); // Get the one above the top
        else
            d = ForgeDirection.getOrientation(meta); // Get the one below the bottom
        
        if (entity.isSneaking() || world.getBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ) != BlockTube.instance)
            axis.add(Utilities.getCollisionBoxPart(x, y, z, d));

        axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.DOWN));
        
        for (AxisAlignedBB a : axis)
            if (a != null && axisAlignedBB.intersectsWith(a))
                list.add(a);
    }

    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
        ChunkCoordinates partner = getPartner(world, x, y, z);
       
        if (world.getBlock(partner.posX, partner.posY, partner.posZ) == this)
            world.setBlock(partner.posX, partner.posY, partner.posZ, Blocks.air);

        super.onBlockExploded(world, x, y, z, explosion);
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
        ChunkCoordinates partner = getPartner(x, y, z, meta);

        if (world.getBlock(partner.posX, partner.posY, partner.posZ) == this)
            world.setBlock(partner.posX, partner.posY, partner.posZ, Blocks.air);
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return false;
    }
    
    @Override
    public boolean canConnectTo(IBlockAccess blockAccess, int x, int y, int z, ForgeDirection d) {
        if (d == ForgeDirection.UP || d == ForgeDirection.DOWN) return false;
        Block block = blockAccess.getBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
        int meta = blockAccess.getBlockMetadata(x + d.offsetX, y + d.offsetY, z + d.offsetZ), thisMeta = blockAccess.getBlockMetadata(x, y, z);
        return block == this && thisMeta >= SHIFT ? meta == thisMeta - SHIFT : thisMeta + SHIFT == meta;
    }

    @Override
    public boolean canConnectToStrict(IBlockAccess blockAccess, int x, int y, int z, ForgeDirection d) {
        return canConnectTo(blockAccess, x, y, z, d);
    }
}
