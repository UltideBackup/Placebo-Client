package com.placebo.Utils

import net.minecraft.gizmos.GizmoStyle
import net.minecraft.gizmos.Gizmos
import net.minecraft.gizmos.TextGizmo
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

fun makeBox(box: AABB, color: Int, fill: Boolean) {
    if (fill != true) {
        Gizmos.cuboid(box, GizmoStyle.stroke(color)).setAlwaysOnTop()
    }else{
        Gizmos.cuboid(box, GizmoStyle.fill(color)).setAlwaysOnTop()
    }
}

fun makeName(x: Double,y:Double, z: Double,entityName: String, color: Int,distance: String? ) {
    if (distance != null) {
        Gizmos.billboardText("$entityName $distance", Vec3(x, y, z), TextGizmo.Style.forColorAndCentered(color) ).setAlwaysOnTop()
    }else{
        Gizmos.billboardText(entityName, Vec3(x, y, z), TextGizmo.Style.forColorAndCentered(color) ).setAlwaysOnTop()
    }

}