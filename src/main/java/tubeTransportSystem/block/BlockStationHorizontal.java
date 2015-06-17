package tubeTransportSystem.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tubeTransportSystem.TubeTransportSystem;
import tubeTransportSystem.client.RenderStation;
import tubeTransportSystem.item.ItemStation;
import tubeTransportSystem.network.ProxyClient;
import tubeTransportSystem.util.Utilities;

public class BlockStationHorizontal extends Block {
    public static BlockStationHorizontal instance;
    public static final int SHIFT = 8;

    public BlockStationHorizontal(String n) {
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
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        return super.getSelectedBoundingBoxFromPool(world, x, y, z); // TODO
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
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
        ForgeDirection d = Utilities.entityGetDirection(entityLiving).getOpposite();
        int x2 = x + d.offsetX, y2 = y + d.offsetY, z2 = z + d.offsetZ;
        boolean isUpper = world.getBlock(x2, y2, z2) == this && world.getBlockMetadata(x2, y2, z2) < SHIFT;
        world.setBlockMetadataWithNotify(x, y, z, isUpper ? d.ordinal() + SHIFT : d.ordinal(), 3);
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
        
        ChunkCoordinates self = Utilities.getBlock(x, y, z, s);
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
}
