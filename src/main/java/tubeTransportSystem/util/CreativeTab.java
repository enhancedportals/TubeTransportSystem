package tubeTransportSystem.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import tubeTransportSystem.TubeTransportSystem;
import tubeTransportSystem.item.ItemTube;

public class CreativeTab extends CreativeTabs {
    public CreativeTab() {
        super(TubeTransportSystem.MOD_ID);
    }

    @Override
    public Item getTabIconItem() {
        return ItemTube.instance;
    }
}
