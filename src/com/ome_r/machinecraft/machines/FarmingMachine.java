package com.ome_r.machinecraft.machines;

import com.ome_r.machinecraft.MachineCraft;
import com.ome_r.machinecraft.misc.ItemBuilder;
import com.ome_r.machinecraft.misc.MachineType;
import com.ome_r.machinecraft.misc.Methods;

import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.Random;

public class FarmingMachine extends Machine{

    private int durability;

    public FarmingMachine(Location loc){
        super(loc, MachineType.FARMING);
        this.durability = MachineCraft.getInstance().getDataHandler().getFarmerDurability();
        loadSettings();
    }

    public void setDurability(int durability){
        this.durability = durability;
        getSettings().setItem(2, new ItemBuilder(Material.STICK).withName("§6Durability").noAttributes().withGlowEffect()
                .withLore("§7Durability: " + durability + " / " + MachineCraft.getInstance().getDataHandler().getFarmerDurability()).asItemStack());
    }

    public int getDurability(){
        return durability;
    }

    @Override
    public void loadSettings() {
        getSettings().setItem(0, new ItemBuilder(Material.DIAMOND_HOE).withName("§aFarming Machine").noAttributes()
                .withLore("§7Location: " + getLocation().getBlockX() + " " + getLocation().getBlockY() + " " + getLocation().getBlockZ()).asItemStack());
        getSettings().setItem(1, new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 15).withName("§f").asItemStack());
        getSettings().setItem(2, new ItemBuilder(Material.STICK).withName("§6Durability").noAttributes().withGlowEffect()
                .withLore("§7Durability: " + durability + " / " + MachineCraft.getInstance().getDataHandler().getFarmerDurability()).asItemStack());
        getSettings().setItem(3, new ItemBuilder(Material.FEATHER).withGlowEffect().withName("§6Speed Level")
                .withLore("§7Every " + (MachineCraft.getInstance().getDataHandler().getSpeedTicks(getType()) / 20.0) + " seconds").asItemStack());
        getSettings().setItem(4, new ItemBuilder(Material.WATCH).withGlowEffect().withName("§6Running Status")
                .withLore(isRunning() ? "§7Running: §atrue" : "§7Running: §cfalse").asItemStack());
    }

    @Override
    public BukkitRunnable loadRunnable(){
        //TODO: Add cocoa beans checker
        return new BukkitRunnable() {
            int minX = getLocation().getBlockX() - 10, maxX = getLocation().getBlockX() + 10,
                    minZ = getLocation().getBlockZ() - 10, maxZ = getLocation().getBlockZ() + 10;
            @Override
            public void run() {
                if(durability >= 0){
                    for(int x = minX; x <= maxX; x++)
                        for(int z = minZ; z <= maxZ; z++){
                            if(durability <= 0)
                                continue;

                            Block b = getLocation().getWorld().getBlockAt(x, getLocation().getBlockY(), z);
                            MaterialData md = b.getState().getData();

                            if(md instanceof Crops){
                                if(((Crops) md).getState() != CropState.RIPE)
                                    continue;

                                for(ItemStack is : b.getDrops()) {
                                    if(is.getType() == Material.CARROT_ITEM || is.getType() == Material.POTATO_ITEM)
                                        is.setAmount(new Random().nextInt(3) + 1);

                                    if(Methods.isInventoryEmpty(getInventory(), is))
                                        getInventory().addItem(is);
                                    else getLocation().getWorld().dropItemNaturally(getLocation(), is);
                                }

                                b.setType(b.getType());
                            }

                            else if(b.getType() == Material.MELON_BLOCK || b.getType() == Material.PUMPKIN ||
                                    b.getType() == Material.SUGAR_CANE_BLOCK){

                                for(ItemStack is : b.getDrops())
                                    if(Methods.isInventoryEmpty(getInventory(), is))
                                        getInventory().addItem(is);
                                    else getLocation().getWorld().dropItemNaturally(getLocation(), is);

                                b.setType(Material.AIR);
                            }

                            else continue;

                            setDurability(durability - 1);
                        }
                }
            }
        };
    }

}
