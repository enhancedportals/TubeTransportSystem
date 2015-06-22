package enhanced.tts.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import enhanced.tts.block.BlockStation;
import enhanced.tts.block.BlockStationHorizontal;
import enhanced.tts.block.BlockTube;

public class RenderStation implements ISimpleBlockRenderingHandler {
    public static int ID;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;

        renderer.unlockBlockBounds();
        renderer.setRenderBounds(0, 0, 0, 1, 2, 1);
        renderer.renderFromInside = true;

        GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.45F, -0.3F);
        GL11.glScalef(0.75f, 0.55f, 0.75f);

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
        tessellator.draw();

        renderer.renderFromInside = false;

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
        tessellator.draw();

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        renderer.setRenderFromInside(true);

        if (block == BlockStation.instance) {
            Block blockDown = world.getBlock(x, y - 1, z), blockUp = world.getBlock(x, y + 1, z);
            renderer.setRenderBounds(0.01, blockDown != BlockTube.instance && blockDown != block ? 0.01 : 0, 0.01, 0.99, blockUp != BlockTube.instance && blockUp != block ? 0.99 : 1, 0.99);
            renderer.renderStandardBlock(block, x, y, z);
        } else {
            int meta = world.getBlockMetadata(x, y, z);
            ForgeDirection d = ForgeDirection.getOrientation(meta < BlockStationHorizontal.SHIFT ? meta : meta - BlockStationHorizontal.SHIFT);
            ForgeDirection o = d.getOpposite();
            Block blockDown = world.getBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ), blockUp = world.getBlock(x + o.offsetX, y + o.offsetY, z + o.offsetZ);
            double posX = 0, negX = 0, posY = 0, negY = 0, posZ = 0, negZ = 0;

            if (d == ForgeDirection.NORTH) {
                if (blockDown == BlockStationHorizontal.instance || blockDown == BlockTube.instance)
                    negZ = -0.01;
                if (blockUp == BlockStationHorizontal.instance || blockUp == BlockTube.instance)
                    posZ = 0.01;
            } else if (d == ForgeDirection.SOUTH) {
                if (blockUp == BlockStationHorizontal.instance || blockUp == BlockTube.instance)
                    negZ = -0.01;
                if (blockDown == BlockStationHorizontal.instance || blockDown == BlockTube.instance)
                    posZ = 0.01;
            } else if (d == ForgeDirection.EAST) {
                if (blockUp == BlockStationHorizontal.instance || blockUp == BlockTube.instance)
                    negX = -0.01;
                if (blockDown == BlockStationHorizontal.instance || blockDown == BlockTube.instance)
                    posX = 0.01;
            } else if (d == ForgeDirection.WEST) {
                if (blockDown == BlockStationHorizontal.instance || blockDown == BlockTube.instance)
                    negX = -0.01;
                if (blockUp == BlockStationHorizontal.instance || blockUp == BlockTube.instance)
                    posX = 0.01;
            }

            renderer.setRenderBounds(0.01 + negX, 0.01 + negY, 0.01 + negZ, 0.99 + posX, 0.99 + posY, 0.99 + posZ);

            renderer.uvRotateBottom = d == ForgeDirection.NORTH || d == ForgeDirection.SOUTH ? 0 : 3;
            renderer.uvRotateEast = 3;
            renderer.uvRotateWest = 3;
            renderer.uvRotateSouth = 3;
            renderer.uvRotateNorth = 3;
            renderer.renderStandardBlock(block, x, y, z);
            renderer.uvRotateNorth = 0;
            renderer.uvRotateSouth = 0;
            renderer.uvRotateEast = 0;
            renderer.uvRotateWest = 0;
            renderer.uvRotateBottom = 0;
        }

        renderer.setRenderFromInside(false);

        // -----------------

        renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
        renderer.renderStandardBlock(block, x, y, z);

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
