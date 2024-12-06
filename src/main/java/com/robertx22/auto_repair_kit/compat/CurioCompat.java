package com.robertx22.auto_repair_kit.compat;

import com.robertx22.auto_repair_kit.kits.RepairEventData;
import com.robertx22.auto_repair_kit.kits.RepairUTIL;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;

public class CurioCompat {

    public static void tryRepair(Player p, RepairEventData data) {
        var c = CuriosApi.getCuriosHelper().getEquippedCurios(p).orElse(null);
        if (c != null) {
            for (int i = 0; i < c.getSlots(); i++) {
                var stack = c.getStackInSlot(i).copy();
                if (stack.isDamaged()) {
                    RepairUTIL.tryRepair(stack, data);
                    c.setStackInSlot(i, stack);
                }
            }
        }
    }
}
