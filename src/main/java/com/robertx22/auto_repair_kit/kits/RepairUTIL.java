package com.robertx22.auto_repair_kit.kits;

import com.robertx22.auto_repair_kit.compat.CurioCompat;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.List;

public class RepairUTIL {

    public static void tryRepair(Player p) {

        ItemStack kit = getBestKit(p);

        if (kit != null) {
            if (kit.getItem() instanceof RepairKitItem rep) {

                var gears = getGearToRepair(p);

                var data = new RepairEventData(kit);

                if (data.charges > 0) {

                    for (ItemStack gear : gears) {
                        tryRepair(gear, data);
                    }
                    if (ModList.get().isLoaded("curios")) {
                        CurioCompat.tryRepair(p, data);
                    }
                }
            }
        }
    }

    public static void tryRepair(ItemStack gear, RepairEventData data) {

        ItemStack kit = data.kit;

        if (kit != null) {
            if (kit.getItem() instanceof RepairKitItem rep) {

                if (gear.isDamaged() && data.charges > 0) {
                    int torepair = gear.getDamageValue();

                    if (torepair > data.charges) {
                        torepair = data.charges;
                    }
                    if (torepair > rep.tier.getTotalPerAction()) {
                        torepair = rep.tier.getTotalPerAction();
                    }

                    gear.setDamageValue(gear.getDamageValue() - torepair);
                    data.charges -= torepair;
                    addCharges(kit, -torepair, rep.tier);
                }

            }
        }
    }

    public static List<ItemStack> getGearToRepair(Player p) {
        List<ItemStack> list = new ArrayList<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            list.add(p.getItemBySlot(slot));
        }
        return list;

    }

    public static ItemStack getBestKit(Player p) {

        ItemStack kit = null;

        for (int i = 0; i < p.getInventory().getContainerSize(); i++) {
            ItemStack stack = p.getInventory().getItem(i);

            if (stack.getItem() instanceof RepairKitItem r) {
                if (kit == null) {
                    kit = stack;
                } else {
                    if (getCharges(stack) > getCharges(kit)) {
                        // the kit with most charges is used
                        kit = stack;
                    }
                }

            }
        }
        return kit;

    }

    public static void chargeKit(ItemStack stack, int amount) {


    }

    public static int getCharges(ItemStack stack) {
        return stack.getOrCreateTag().getInt("charges");
    }

    public static void addCharges(ItemStack stack, int amount, RepairKitTier tier) {
        int after = getCharges(stack) + amount;

        if (after < 0) {
            after = 0;
        }
        if (after > tier.getMax()) {
            after = tier.getMax();
        }

        float dura = 1F - after / (float) tier.getMax();

        int dmg = (int) (stack.getMaxDamage() * dura);

        if (dmg >= stack.getMaxDamage()) {
            dmg = stack.getMaxDamage() - 1;
        }

        stack.getOrCreateTag().putInt("charges", after);

        stack.setDamageValue(dmg);

    }
}
