package tubeTransportSystem.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import tubeTransportSystem.block.BlockStation;
import tubeTransportSystem.block.BlockTube;
import tubeTransportSystem.network.ProxyClient;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

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
        renderer.renderFaceYNeg(BlockStation.instance, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(BlockStation.instance, 0, 0xFFFFFF));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(BlockStation.instance, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(BlockStation.instance, 1, 0xFFFFFF));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(BlockStation.instance, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(BlockStation.instance, 2, 0xFFFFFF));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(BlockStation.instance, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(BlockStation.instance, 3, 0xFFFFFF));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(BlockStation.instance, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(BlockStation.instance, 4, 0xFFFFFF));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(BlockStation.instance, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(BlockStation.instance, 5, 0xFFFFFF));
        tessellator.draw();

        renderer.renderFromInside = false;

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(BlockStation.instance, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(BlockStation.instance, 0, 0xFFFFFF));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(BlockStation.instance, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(BlockStation.instance, 1, 0xFFFFFF));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(BlockStation.instance, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(BlockStation.instance, 2, 0xFFFFFF));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(BlockStation.instance, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(BlockStation.instance, 3, 0xFFFFFF));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(BlockStation.instance, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(BlockStation.instance, 4, 0xFFFFFF));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(BlockStation.instance, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(BlockStation.instance, 5, 0xFFFFFF));
        tessellator.draw();

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (ProxyClient.renderPass == 0) {
            renderer.setRenderFromInside(true);
            Block blockDown = world.getBlock(x, y - 1, z), blockUp = world.getBlock(x, y + 1, z);
            renderer.setRenderBounds(0.01, blockDown != BlockTube.instance && blockDown != BlockStation.instance ? 0.01 : 0, 0.01, 0.99, blockUp != BlockTube.instance && blockUp != BlockStation.instance ? 0.99 : 1, 0.99);
            renderer.renderStandardBlock(BlockStation.instance, x, y, z);
            renderer.setRenderFromInside(false);
        } else
            renderer.renderStandardBlock(BlockStation.instance, x, y, z);

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
