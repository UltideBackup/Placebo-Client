package com.placebo.Core.Classes

import com.placebo.Core.Event

open class Module(var name: String) {
    open var state = true

    // Optional human-readable description shown in the UI on hover.
    // If null, the HTTP server falls back to a hash-picked placeholder.
    open var description: String? = null
    open var category: String = "Misc"
    open var mode = 0
    open fun Tick(){

    }//a

}
