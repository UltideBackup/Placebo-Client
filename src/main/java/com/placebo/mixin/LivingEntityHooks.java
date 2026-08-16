package com.placebo.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.placebo.Core.EventManagerKt;
import com.placebo.Events.FallEvent;
import com.placebo.Events.JumpEvent;
import com.placebo.Events.LedgeEvent;
import com.placebo.Events.StepEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityHooks {
    @ModifyReturnValue(at = @At("RETURN"), method = "maxUpStep")
    private float StepEvent(float original) {
        StepEvent event = new StepEvent(original);
        EventManagerKt.callEvent(event);

       return (event.getStepHeight());
    }

    @Inject(at = @At("RETURN"), method = "getJumpPower", cancellable = true)
    private void PlayerJump(CallbackInfoReturnable<Float> cir) {
        JumpEvent event = new JumpEvent(cir.getReturnValue());
        EventManagerKt.callEvent(event);


        cir.setReturnValue(event.getJumpHeight());


    }
}