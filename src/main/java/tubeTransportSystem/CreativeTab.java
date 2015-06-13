package tubeTransportSystem;

import tubeTransportSystem.block.BlockTube;
import tubeTransportSystem.item.ItemTube;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTab extends CreativeTabs {
	public CreativeTab() {
		super(TubeTransportSystem.MOD_ID);
	}

	@Override
	public Item getTabIconItem() {
		return ItemTube.instance;
	}
}
