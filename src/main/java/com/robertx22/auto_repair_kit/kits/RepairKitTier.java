package com.robertx22.auto_repair_kit.kits;

import com.robertx22.auto_repair_kit.configs.KitConfig;

public enum RepairKitTier {

    LEATHER("leather", new KitData(500, 10)),
    IRON("iron", new KitData(1000, 25)),
    GOLD("gold", new KitData(2500, 50)),
    DIAMOND("diamond", new KitData(5000, 100)),
    NETHERITE("netherite", new KitData(10000, 250));

    public String id;

    public KitData defaults;

    RepairKitTier(String id, KitData defaults) {
        this.id = id;
        this.defaults = defaults;
    }

    public int getMax() {
        return KitConfig.get().maxCharges.get(this).get();
    }

    public int getTotalPerAction() {
        return KitConfig.get().maxPerAction.get(this).get();
    }
}
