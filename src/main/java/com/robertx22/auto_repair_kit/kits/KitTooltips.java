package com.robertx22.auto_repair_kit.kits;

import com.robertx22.auto_repair_kit.configs.KitConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KitTooltips {


    public static List<MutableComponent> getTooltip(ItemStack stack) {

        List<MutableComponent> tip = new ArrayList<>();

        if (stack.getItem() instanceof RepairKitItem item) {
            tip.add(Component.translatable("auto_repair_kit.charges", RepairUTIL.getCharges(stack), item.tier.getMax()).withStyle(ChatFormatting.GREEN));


            tip.add(Component.empty());

            if (!Screen.hasShiftDown()) {
                tip.add(Component.translatable("auto_repair_kit.press_shift").withStyle(ChatFormatting.BLUE));
            } else {
                tip.add(Component.translatable("auto_repair_kit.charge_mats").withStyle(ChatFormatting.YELLOW));

                for (Map.Entry<Item, Integer> en : KitConfig.get().getChargeMaterialMap().entrySet()) {
                    tip.add(Component.literal(" - ").append(en.getKey().getDefaultInstance().getDisplayName()).append(": " + en.getValue()));
                }
            }
        }

        return tip;

    }
}
