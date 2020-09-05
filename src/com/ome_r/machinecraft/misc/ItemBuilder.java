package com.ome_r.machinecraft.misc;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class ItemBuilder {

    private ItemStack is;
    private ItemMeta im;

    public ItemBuilder(ItemStack is){
        this.is = is.clone();
        this.im = this.is.getItemMeta();
    }

    public ItemBuilder(Material type){
        this(new ItemStack(type));
    }

    public ItemBuilder(Material type, short damage){
        this(new ItemStack(type, 1, damage));
    }

    public ItemBuilder withName(String name){
        im.setDisplayName(name);
        return this;
    }

    public ItemBuilder withLore(String lore){
        im.setLore(Collections.singletonList(lore));
        return this;
    }

    public ItemBuilder withLore(List<String> lore){
        im.setLore(lore);
        return this;
    }

    public ItemBuilder withGlowEffect(){
        im.addEnchant(Enchantment.DURABILITY, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder noAttributes(){
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        return this;
    }

    public ItemStack asItemStack() {
        is.setItemMeta(im);
        return is;
    }
}
