package com.placebo.Core.Classes


open class Module(var name: String) {
    open var state = true

    open var description: String? = null
    open var category: String = "Misc"
    open var mode = 0
    fun toggle(){
        state = !state
    }
    open fun Tick(){

    }



}
