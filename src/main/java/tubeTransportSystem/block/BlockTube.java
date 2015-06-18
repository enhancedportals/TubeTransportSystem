package tubeTransportSystem.block;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tubeTransportSystem.TubeTransportSystem;
import tubeTransportSystem.client.RenderTube;
import tubeTransportSystem.network.ProxyClient;
import tubeTransportSystem.repack.codechicken.lib.raytracer.IndexedCuboid6;
import tubeTransportSystem.repack.codechicken.lib.vec.BlockCoord;
import tubeTransportSystem.repack.codechicken.lib.vec.Vector3;
import tubeTransportSystem.util.ConnectedTextures;
import tubeTransportSystem.util.IConnectable;
import tubeTransportSystem.util.Utilities;

public class BlockTube extends Block implements IConnectable {
    public static BlockTube instance;
    ConnectedTextures textures[] = new ConnectedTextures[12];

    public BlockTube(String n) {
        super(Material.rock);
        setBlockName(n);
        instance = this;
        setLightOpacity(0);
        setHardness(5f);
        setCreativeTab(TubeTransportSystem.creativeTab);
        textures[0] = new ConnectedTextures(TubeTransportSystem.MOD_ID + ":tube0/%s", this, 0);
        textures[1] = new ConnectedTextures(TubeTransportSystem.MOD_ID + ":tube1/%s", this, 1);
        textures[2] = new ConnectedTextures(TubeTransportSystem.MOD_ID + ":tube2/%s", this, 2);
        textures[3] = new ConnectedTextures(TubeTransportSystem.MOD_ID + ":tube3/%s", this, 3);
        textures[4] = new ConnectedTextures(TubeTransportSystem.MOD_ID + ":tube4/%s", this, 4);
        textures[5] = new ConnectedTextures(TubeTransportSystem.MOD_ID + ":tube5/%s", this, 5);
        textures[6] = new ConnectedTextures(TubeTransportSystem.MOD_ID + ":tube/%s", this, 0);
        textures[7] = new ConnectedTextures(TubeTransportSystem.MOD_ID + ":tube/%s", this, 1);
        textures[8] = new ConnectedTextures(TubeTransportSystem.MOD_ID + ":tube/%s", this, 2);
        textures[9] = new ConnectedTextures(TubeTransportSystem.MOD_ID + ":tube/%s", this, 3);
        textures[10] = new ConnectedTextures(TubeTransportSystem.MOD_ID + ":tube/%s", this, 4);
        textures[11] = new ConnectedTextures(TubeTransportSystem.MOD_ID + ":tube/%s", this, 5);
    }

    @Override
    public int getRenderType() {
        return RenderTube.ID;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        for (int i = 0; i < 7; i++)
            textures[i].registerIcons(iconRegister);
        
        for (int i = 7; i < textures.length; i++)
            textures[i].registerIcons(textures[6]);
    }

    @Override
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int s) {
        int meta = blockAccess.getBlockMetadata(x, y, z);
        
        if (meta <= 5) {
            ForgeDirection d = ForgeDirection.getOrientation(meta);
            
            if ((d == ForgeDirection.UP || d == ForgeDirection.DOWN) && (s == 0 || s == 1))
                return RenderTube.IS_INTERNAL ? textures[meta + 6].getIconForSideForInternal(blockAccess, x, y, z, s) : textures[meta + 6].getIconForSide(blockAccess, x, y, z, s);
            else if  ((d == ForgeDirection.NORTH || d == ForgeDirection.SOUTH) && (s == 2 || s == 3))
                return RenderTube.IS_INTERNAL ? textures[meta + 6].getIconForSideForInternal(blockAccess, x, y, z, s) : textures[meta + 6].getIconForSide(blockAccess, x, y, z, s);
            else if  ((d == ForgeDirection.EAST || d == ForgeDirection.WEST) && (s == 4 || s == 5))
                    return RenderTube.IS_INTERNAL ? textures[meta + 6].getIconForSideForInternal(blockAccess, x, y, z, s) : textures[meta + 6].getIconForSide(blockAccess, x, y, z, s);
                   
            return RenderTube.IS_INTERNAL ? textures[meta].getIconForSideForInternal(blockAccess, x, y, z, s) : textures[meta].getIconForSide(blockAccess, x, y, z, s);
        }
            
        return textures[0].getBaseIcon();
    }

    @Override
    public IIcon getIcon(int s, int m) {
        if (m >= 10 && m <= 15)
            m = m - 10;

        if (m > 5)
            m = 0;
        
        return textures[m].getBaseIcon();
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (entity == null)
            return;

        Utilities.entityAccelerate(entity, ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z)));
        Utilities.entityLimitSpeed(entity);
        Utilities.entityResetFall(entity);
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
        return pass == 1;
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int s) {
        /*int meta = blockAccess.getBlockMetadata(x, y, z);

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

        else
            return true;*/
        
        ChunkCoordinates self = Utilities.getCoordinatesFromSide(x, y, z, s);
        return !canConnectToStrict(blockAccess, self.posX, self.posY, self.posZ, Utilities.getDirectionFromSide(x, y, z, s));
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity) {
        if (entity == null)
            return;
        
        ForgeDirection dir = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));
        List<AxisAlignedBB> axis = new ArrayList<AxisAlignedBB>();

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            if (!canConnectTo(world, x, y, z, d)) {
                if ((dir == ForgeDirection.UP || dir == ForgeDirection.DOWN) && (d == ForgeDirection.UP || d == ForgeDirection.DOWN)) continue;
                if ((dir == ForgeDirection.NORTH || dir == ForgeDirection.SOUTH) && (d == ForgeDirection.NORTH || d == ForgeDirection.SOUTH)) continue;
                if ((dir == ForgeDirection.EAST || dir == ForgeDirection.WEST) && (d == ForgeDirection.EAST || d == ForgeDirection.WEST)) continue;
                axis.add(Utilities.getCollisionBoxPart(x, y, z, d));
            }
        }
        
        /*if (meta == ForgeDirection.UP.ordinal() || meta == ForgeDirection.DOWN.ordinal()) {
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.EAST));
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.WEST));
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.SOUTH));
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.NORTH));
        } else if (meta == ForgeDirection.NORTH.ordinal() || meta == ForgeDirection.SOUTH.ordinal()) {
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.DOWN));
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.UP));
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.EAST));
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.WEST));
        } else if (meta == ForgeDirection.WEST.ordinal() || meta == ForgeDirection.EAST.ordinal()) {
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.NORTH));
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.SOUTH));
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.UP));
            axis.add(Utilities.getCollisionBoxPart(x, y, z, ForgeDirection.DOWN));
        }*/

        for (AxisAlignedBB a : axis)
            if (a != null && axisAlignedBB.intersectsWith(a))
                list.add(a);
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, net.minecraftforge.common.util.ForgeDirection side) {
        return false;
    }
    
    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {
        List<IndexedCuboid6> cuboids = new LinkedList<IndexedCuboid6>();
        Utilities.addCuboidsForRaytraceTube(cuboids, world, x, y, z);
        return Utilities.rayTracer.rayTraceCuboids(new Vector3(start), new Vector3(end), cuboids, new BlockCoord(x, y, z), this);
    }
    
    @Override
    public boolean canConnectTo(IBlockAccess blockAccess, int x, int y, int z, ForgeDirection d) {
        return blockAccess.getBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ) == this;
    }

    @Override
    public boolean canConnectToStrict(IBlockAccess blockAccess, int x, int y, int z, ForgeDirection d) {
        return blockAccess.getBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ) == this && blockAccess.getBlockMetadata(x + d.offsetX, y + d.offsetY, z + d.offsetZ) == blockAccess.getBlockMetadata(x, y, z);
    }
}
