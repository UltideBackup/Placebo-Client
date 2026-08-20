package com.placebo.mixin;

import com.placebo.Core.EventManagerKt;
import com.placebo.Events.RotationEvent;
import com.placebo.Modules.Combat.AimAssist;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(LocalPlayer.class)
public class SaimHelper {
    @Unique private float savedYaw;
    @Unique private float savedPitch;
    @Unique private static boolean shouldAttack;
    @Unique private static Player attackTarget;

    @Inject(at = @At("HEAD"), method = "sendPosition")
    private void onSendMovementPackets(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        savedYaw = player.yRot;
        savedPitch = player.xRot;
        RotationEvent event = new RotationEvent(player.yRot, player.xRot);
        EventManagerKt.callEvent(event);
        player.yRot = event.getYaw();
        player.xRot = event.getPitch();
    }

    @Inject(at = @At("RETURN"), method = "sendPosition")
    private void afterSendMovementPackets(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        player.yRot = savedYaw;
        player.xRot = savedPitch;

    }
}