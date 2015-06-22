package enhanced.tts.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import enhanced.tts.block.BlockTube;
import enhanced.tts.item.ItemTube;

public class RenderTube implements ISimpleBlockRenderingHandler {
    public static int ID;
    public static boolean IS_INTERNAL = false;
    
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        renderer.setOverrideBlockTexture(ItemTube.instance.getIconFromDamage(metadata));
        renderer.renderBlockAsItem(Blocks.stone, 0, 0xFFFFFF);
        renderer.clearOverrideBlockTexture();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        boolean rendered = false;
        boolean[] renderBound = new boolean[6];

        for (int i = 0; i < 6; i++) {
            ForgeDirection d = ForgeDirection.UNKNOWN;

            if (i == 0)
                d = ForgeDirection.WEST;
            else if (i == 1)
                d = ForgeDirection.DOWN;
            else if (i == 2)
                d = ForgeDirection.NORTH;
            else if (i == 3)
                d = ForgeDirection.EAST;
            else if (i == 4)
                d = ForgeDirection.UP;
            else if (i == 5)
                d = ForgeDirection.SOUTH;

            renderBound[i] = BlockTube.instance.canConnectTo(world, x, y, z, d);
        }

        renderer.setRenderFromInside(true);
        renderer.setRenderBounds(renderBound[0] ? 0 : 0.01, renderBound[1] ? 0 : 0.01, renderBound[2] ? 0 : 0.01, renderBound[3] ? 1 : 0.99, renderBound[4] ? 1 : 0.99, renderBound[5] ? 1 : 0.99);

        renderer.uvRotateBottom = 0;
        renderer.uvRotateTop = 0;
        renderer.flipTexture = true;
        IS_INTERNAL = true;

        rendered = renderer.renderStandardBlock(BlockTube.instance, x, y, z);

        IS_INTERNAL = false;
        renderer.flipTexture = false;
        renderer.uvRotateBottom = 0;
        renderer.uvRotateTop = 0;
        renderer.setRenderFromInside(false);

        //------------------------
        
        renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
        rendered = renderer.renderStandardBlock(block, x, y, z) || rendered;

        return rendered;
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
