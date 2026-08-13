package com.placebo.Utils


fun red(): Int{
    return 0xFF0000.toInt()
}

fun green(): Int{
    return 0x00FF00.toInt()
}

fun blue(): Int{
    return 0x0000FF.toInt()
}
//gemini gave me these i made the first few i just didnt feel like getting all these colors
fun yellow(): Int {
    return 0xFFFF00.toInt() // Red + Green
}

fun cyan(): Int {
    return 0x00FFFF.toInt() // Green + Blue
}

fun magenta(): Int {
    return 0xFF00FF.toInt() // Red + Blue
}

fun white(): Int {
    return 0xFFFFFF.toInt()
}

fun black(): Int {
    return 0x000000.toInt()
}

fun gray(): Int {
    return 0x808080.toInt() // 50% Gray
}

fun lightGray(): Int {
    return 0xD3D3D3.toInt()
}

fun darkGray(): Int {
    return 0xA9A9A9.toInt()
}

fun rainbow(): Int {
    val time = System.currentTimeMillis()//this will give us the current time in milliseconds
    val hue = (time % 3600L) / 3600f//this is a modulo it gives us the remainder after division
    //3600L just means this numbe ri s long so were doing 3.6seocnds
    //basically ths / gives us the points to move up by
    val color = java.awt.Color.HSBtoRGB(hue, 1f, 1f)
    //hue is which color which we did before saturation is how saturated it is and brightness is obvious
    //the java just gives us our color to rgb
    return color

}
