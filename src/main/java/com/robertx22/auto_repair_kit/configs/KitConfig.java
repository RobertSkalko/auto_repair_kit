package com.robertx22.auto_repair_kit.configs;

import com.robertx22.auto_repair_kit.kits.RepairKitTier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KitConfig {

    public static final ForgeConfigSpec SPEC;
    public static final KitConfig KIT_CONFIG;

    static {
        final Pair<KitConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(KitConfig::new);
        SPEC = specPair.getRight();
        KIT_CONFIG = specPair.getLeft();
    }


    public static KitConfig get() {
        return KIT_CONFIG;
    }

    public HashMap<RepairKitTier, ForgeConfigSpec.IntValue> maxCharges = new HashMap<>();
    public HashMap<RepairKitTier, ForgeConfigSpec.IntValue> maxPerAction = new HashMap<>();

    public ForgeConfigSpec.ConfigValue<List<? extends String>> CHARGE_MATS;

    public ForgeConfigSpec.IntValue REPAIR_INTERVAL;


    HashMap<Item, Integer> chargeMaterials = new HashMap<Item, Integer>();

    public HashMap<Item, Integer> getChargeMaterialMap() {

        if (chargeMaterials.isEmpty()) {
            for (String s : CHARGE_MATS.get()) {
                try {
                    var split = s.split(";");
                    ResourceLocation id = new ResourceLocation(split[0]);
                    int n = Integer.parseInt(split[1]);
                    var theitem = BuiltInRegistries.ITEM.get(id);
                    chargeMaterials.put(theitem, n);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return chargeMaterials;
    }

    public int getChargeMaterial(Item item) {
        getChargeMaterialMap();
        return chargeMaterials.getOrDefault(item, 0);
    }

    KitConfig(ForgeConfigSpec.Builder b) {
        b.comment("Auto Repair Kits Configs")
                .push("general");

        REPAIR_INTERVAL = b.comment("Example, at default setting of 100 ticks, (1 second is 20 ticks), the repair kits will automatically repair player's gear every 5 seconds").defineInRange("repair_interval", 100, 1, 5000);


        for (RepairKitTier tier : RepairKitTier.values()) {
            b.push(tier.name());

            ForgeConfigSpec.IntValue max = b.comment("The amount the Kit can be charged.").defineInRange("max_charges", tier.defaults.maxCharges, 0, Integer.MAX_VALUE);
            ForgeConfigSpec.IntValue action = b.comment("The total amount the kit can repair at once every 5s. (This is per each item)").defineInRange("max_repair_per_item_every_5s", tier.defaults.maxRepairedAtOnceTotal, 0, Integer.MAX_VALUE);

            maxCharges.put(tier, max);
            maxPerAction.put(tier, action);

            b.pop();
        }

        List<String> list = new ArrayList<>();
        list.add("minecraft:leather;10");
        list.add("minecraft:iron_ingot;25");
        list.add("minecraft:gold_ingot;50");
        list.add("minecraft:diamond;250");
        list.add("minecraft:netherite_ingot;500");

        CHARGE_MATS = b.comment("What items can be used to charge the repair kits. Usage: minecraft:diamond;50  to make diamonds charge the kit for 50 durability")
                .defineList("charge_material_items", list, x -> {
                    String str = (String) x;
                    return str.split(";").length == 2;
                });


        b.pop();
    }


}
