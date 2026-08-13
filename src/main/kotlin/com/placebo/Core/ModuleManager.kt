package com.placebo.Core

import com.placebo.Core.Classes.Module
import com.placebo.Modules.Joke.*
import com.placebo.Modules.Combat.*
import com.placebo.Modules.Movement.*
import com.placebo.Modules.Visual.*
import com.placebo.Modules.World.*

val modules = mutableListOf<Module>(
    Jokekillaura(),
    Jokefast(),
    Jokebidge(),
    Jokebackknocking(),
    Jokegoodmode(),
    Jokenicechat(),
    Jokeautobighit(),
    Jokesteel(),
    Jokeenabler(),
    Jokebefriendbots(),
    Jokeheystopfalling(),
    Jokeblinkliterally(),
    Jokesunnyday(),
    Jokefuckingstopmefromthetortureofmakingthis(),
    Esp(),
    Chams(),
    Nametags(),
    Velocity(),
    Speed(),
    AutoTool()



)//for some reason liquid bounce hardcodes instead of using ksp or smth lowkey my lazy ass would do that same

fun tick(){
    for (module in modules){
        module.Tick()
    }
}
