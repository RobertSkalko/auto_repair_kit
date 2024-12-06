package com.robertx22.auto_repair_kit.main;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MixinConnector implements IMixinConnector {

    @Override
    public void connect() {
        Mixins.addConfiguration(
                "temporary_spawners-mixins.json"
        );
    }
}