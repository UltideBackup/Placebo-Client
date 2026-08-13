package com.placebo.Useless

import com.placebo.Useless.uselessclasses.coide
import com.placebo.codethatidontknowwhatitdoes.*
import com.placebo.codethatidontknowwhatitdoes.decoy.decoyModules

val modules = mutableListOf<coide>(
    killaura(),
    fast(),
    bidge(),
    backknocking(),
    goodmode(),
    nicechat(),
    autobighit(),
    steel(),
    enabler(),
    befriendbots(),
    heystopfalling(),
    blinkliterally(),
    sunnyday(),
    fuckingstopmefromthetortureofmakingthis(),

    // decoy modules (155 generated stubs to bulk up the clickgui)
    // pulled in from com/placebo/codethatidontknowwhatitdoes/decoy/_registry.kt
).apply { addAll(decoyModules) }

fun tick(){
    for (module in modules){
        module.Tiick()
    }
}
