package com.placebo.Core

import com.placebo.Core.Classes.Module
import com.placebo.Modules.Joke.*
import com.placebo.Modules.Combat.*
import com.placebo.Modules.Movement.*
import com.placebo.Modules.Visual.*
import com.placebo.Modules.World.*

val modules = mutableListOf<Module>(
    Esp(),
    Chams(),
    Nametags(),
    Velocity(),
    Speed(),
    AutoTool(),
    Nofall(),
    Blink(),
    AirJump(),
    ChestStealer(),
    Triggerbot(),
    Killaura(),
    ShieldBreaker(),
    AntiShieldBreak(),
    FastPlace(),
    SafeWalk(),
//    AutoSwap() removed till i can find a good use case for this
    Step(),
    SuperJump(),
    FakeLag(),
//    AntiMaceSmash() removed till i fix implementation
    Scaffold(),
    MLG(),
    AimAssist()
)

fun tick(){
    for (module in modules){
        module.Tick()
    }
}
