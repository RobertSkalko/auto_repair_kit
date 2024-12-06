package com.robertx22.auto_repair_kit.kits;

import net.minecraft.world.item.ItemStack;

public class RepairEventData {

    public ItemStack kit;

    public int charges = 0;

    public RepairEventData(ItemStack kit) {
        this.kit = kit;
        this.charges = RepairUTIL.getCharges(kit);
    }
}
