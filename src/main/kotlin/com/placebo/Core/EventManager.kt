package com.placebo.Core

private val listeners = mutableListOf<Listener>()
interface Event

interface Listener{
    fun onEvent(event:Event)
}

fun registerListener(listener: Listener){
listeners.add(listener)
}
fun callEvent(event: Event){
    for (listener in listeners){
        listener.onEvent(event)
    }
}