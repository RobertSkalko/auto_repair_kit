package com.robertx22.auto_repair_kit.main;

import com.robertx22.auto_repair_kit.configs.KitConfig;
import com.robertx22.auto_repair_kit.kits.RepairKitItem;
import com.robertx22.auto_repair_kit.kits.RepairKitTier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

@Mod("auto_repair_kit")
public class CommonInit {

    public static String ID = "auto_repair_kit";

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ID);

    public static HashMap<RepairKitTier, RegistryObject<RepairKitItem>> items = new HashMap<>();


    public CommonInit() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, KitConfig.SPEC);
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
        CREATIVE_TAB.register(bus);
        bus.addListener(this::commonSetupEvent);

        for (RepairKitTier tier : RepairKitTier.values()) {
            RegistryObject<RepairKitItem> reg = ITEMS.register(tier.id + "_kit", () -> new RepairKitItem(tier));
            items.put(tier, reg);
        }

        CREATIVE_TAB.register("tab", () -> new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 2)
                .icon(() -> items.get(RepairKitTier.NETHERITE).get().getDefaultInstance())
                .title(Component.translatable("auto_repair_kit.name").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD))
                .displayItems(new CreativeModeTab.DisplayItemsGenerator() {
                    @Override
                    public void accept(CreativeModeTab.ItemDisplayParameters param, CreativeModeTab.Output output) {
                        for (Map.Entry<RepairKitTier, RegistryObject<RepairKitItem>> en : CommonInit.items.entrySet()) {
                            output.accept(en.getValue().get());
                        }
                    }
                })
                .build());

        System.out.println("Auto Repair Kits loaded.");
    }

    public void commonSetupEvent(FMLCommonSetupEvent event) {
        ComponentInit.reg();
    }
}
