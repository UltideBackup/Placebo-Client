package com.placebo.Core

import com.placebo.Core.Classes.Module
import com.placebo.Modules.Joke.*
import com.placebo.Modules.Test.*

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
    PacketTest()



)//for some reason liquid bounce hardcodes instead of using ksp or smth lowkey my lazy ass would do that same

fun tick(){
    for (module in modules){
        module.Tick()
    }
}
