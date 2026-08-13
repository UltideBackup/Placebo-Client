package com.placebo.mixin;


import com.placebo.Core.EventManagerKt;
import com.placebo.Events.MoveEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(Entity.class)
public class EntityHooks {
    @Inject(at = @At("HEAD"), method = "move",cancellable = true)
    private void MoveEvent(MoverType moverType, Vec3 delta, CallbackInfo ci) {
        if ((Object) this != Minecraft.getInstance().player) return;
        MoveEvent event = new MoveEvent(delta);
        EventManagerKt.callEvent(event);

    }

}