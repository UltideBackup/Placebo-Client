package com.placebo.mixin;

import com.placebo.Core.EventManagerKt;
import com.placebo.Events.FallEvent;
import com.placebo.Events.LedgeEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerHooks {
    @Inject(at = @At("HEAD"), method = "causeFallDamage", cancellable = true)
    private void FallEvent(double fallDistance, float damageModifier, DamageSource damageSource,CallbackInfoReturnable<Boolean> cir) {
        FallEvent event = new FallEvent(false);
        EventManagerKt.callEvent(event);

        if(event.isCancelled() == true){
            cir.cancel();
        }
    }

    @Inject(method = "isStayingOnGroundSurface", at = @At("HEAD"), cancellable = true)
    private void Ledge(CallbackInfoReturnable<Boolean> cir) {
        LedgeEvent event = new LedgeEvent(false);
        EventManagerKt.callEvent(event);
        if(event.getClip()){
            cir.setReturnValue(true);
        }
    }
    }