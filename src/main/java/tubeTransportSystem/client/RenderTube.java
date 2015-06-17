package tubeTransportSystem.client;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tubeTransportSystem.block.BlockStation;
import tubeTransportSystem.block.BlockTube;
import tubeTransportSystem.network.ProxyClient;
import tubeTransportSystem.repack.codechicken.lib.raytracer.IndexedCuboid6;
import tubeTransportSystem.repack.codechicken.lib.vec.BlockCoord;
import tubeTransportSystem.repack.codechicken.lib.vec.Vector3;
import tubeTransportSystem.util.Utilities;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderTube implements ISimpleBlockRenderingHandler {
    public static int ID;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        renderer.setOverrideBlockTexture(BlockTube.instance.getBlockTextureFromSide(0));
        renderer.renderBlockAsItem(Blocks.stone, 0, 0xFFFFFF);
        renderer.clearOverrideBlockTexture();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (ProxyClient.renderPass == 0) {
            renderer.setRenderFromInside(true);
            renderer.flipTexture = true;

            int meta = world.getBlockMetadata(x, y, z);

            if (meta == 0 || meta == 1) {
                Block blockCheck = world.getBlock(x, y - 1, z);
                renderer.setRenderBounds(0.01, blockCheck != BlockTube.instance && blockCheck != BlockStation.instance ? 0.01 : 0, 0.01, 0.99, 1, 0.99);
            } else if (meta == 2 || meta == 3) {
                Block blockCheck = world.getBlock(x, y, z - 1), blockCheck2 = world.getBlock(x, y, z + 1);
                renderer.setRenderBounds(0.01, 0.01, blockCheck != BlockTube.instance && blockCheck != BlockStation.instance ? 0.01 : 0, 0.99, 0.99, blockCheck2 != BlockTube.instance && blockCheck2 != BlockStation.instance ? 0.99 : 1);
            } else if (meta == 4 || meta == 5) {
                renderer.uvRotateBottom = 3;
                renderer.uvRotateTop = 3;
                Block blockCheck = world.getBlock(x - 1, y, z), blockCheck2 = world.getBlock(x + 1, y, z);
                renderer.setRenderBounds(blockCheck != BlockTube.instance && blockCheck != BlockStation.instance ? 0.01 : 0, 0.01, 0.01, blockCheck2 != BlockTube.instance && blockCheck2 != BlockStation.instance ? 0.99 : 1, 0.99, 0.99);
            }

            renderer.renderStandardBlock(BlockTube.instance, x, y, z);

            renderer.uvRotateBottom = 0;
            renderer.uvRotateTop = 0;
            renderer.flipTexture = false;
            renderer.setRenderFromInside(false);
        } else {
            renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
            renderer.renderStandardBlock(block, x, y, z);
        }

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return ID;
    }
}
