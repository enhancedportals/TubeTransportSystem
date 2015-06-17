package tubeTransportSystem.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
import tubeTransportSystem.util.Utilities;

public class BlockStation extends Block {
    public static BlockStation instance;
    public static IIcon side1, side2, side3, side4;
    public static IIcon entr1, entr2, entr3, entr4;
    public static IIcon side_misc;

    static final int SHIFT = 8;

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
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);  // TODO
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

        ForgeDirection d = Utilities.entityGetDirection(entityLiving);
        boolean isUpper = world.getBlock(x, y - 1, z) == this && world.getBlockMetadata(x, y - 1, z) < SHIFT;

        world.setBlockMetadataWithNotify(x, y, z, isUpper ? d.ordinal() + SHIFT : d.ordinal(), 3);
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
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity) {
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
}
