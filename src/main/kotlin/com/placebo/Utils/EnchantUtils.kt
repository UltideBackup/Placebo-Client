package com.placebo.Utils

import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment

fun ItemStack.getEnchantLevel(enchantKey: ResourceKey<Enchantment>): Int {
    val enchants = this.get(DataComponents.ENCHANTMENTS) ?: return 0
    return enchants.entrySet().firstOrNull { it.key.`is`(enchantKey) }?.intValue ?: 0
}