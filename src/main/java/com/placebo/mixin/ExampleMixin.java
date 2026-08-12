package com.placebo.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.placebo.Useless.ThismanagessomethingKt;

@Mixin(Minecraft.class)
public class ExampleMixin {
	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo info) {
ThismanagessomethingKt.tick();
	}
}