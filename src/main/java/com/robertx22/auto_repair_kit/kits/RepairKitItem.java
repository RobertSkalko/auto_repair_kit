package com.robertx22.auto_repair_kit.kits;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class RepairKitItem extends Item {

    public RepairKitTier tier;

    public RepairKitItem(RepairKitTier tier) {
        super(new Properties().stacksTo(1).durability(100));
        this.tier = tier;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        try {
            tooltip.addAll(KitTooltips.getTooltip(stack));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}