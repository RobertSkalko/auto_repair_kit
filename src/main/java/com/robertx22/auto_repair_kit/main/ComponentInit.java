package com.robertx22.auto_repair_kit.main;

import com.robertx22.auto_repair_kit.configs.KitConfig;
import com.robertx22.auto_repair_kit.kits.RepairKitItem;
import com.robertx22.auto_repair_kit.kits.RepairUTIL;
import com.robertx22.library_of_exile.main.ApiForgeEvents;
import com.robertx22.library_of_exile.utils.SoundUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;

public class ComponentInit {


    public static void reg() {


        ApiForgeEvents.registerForgeEvent(TickEvent.PlayerTickEvent.class, event ->
        {
            if (!event.player.level().isClientSide) {
                if (event.phase == TickEvent.Phase.END) {
                    Player p = event.player;

                    int interval = KitConfig.get().REPAIR_INTERVAL.get();

                    if (p.tickCount % (interval) == 0) {
                        RepairUTIL.tryRepair(p);
                    }
                }
            }
        });

        ApiForgeEvents.registerForgeEvent(ItemStackedOnOtherEvent.class, x -> {
            Player player = x.getPlayer();

            if (player.level().isClientSide) {
                return;
            }

            ItemStack material = x.getStackedOnItem();
            ItemStack kit = x.getCarriedItem();

            if (material.getCount() < 1) {
                return;
            }

            int num = KitConfig.get().getChargeMaterial(material.getItem());

            if (num < 1) {
                return;
            }
            if (kit.getItem() instanceof RepairKitItem rep) {
                if (RepairUTIL.getCharges(kit) < rep.tier.getMax()) {
                    material.shrink(1);
                    RepairUTIL.addCharges(kit, num, rep.tier);

                    SoundUtils.ding(player.level(), player.blockPosition());
                    SoundUtils.playSound(player.level(), player.blockPosition(), SoundEvents.ANVIL_USE, 0.7F, 1);
                    x.setCanceled(true);
                }
            }

        }, EventPriority.HIGHEST);
    }

}
