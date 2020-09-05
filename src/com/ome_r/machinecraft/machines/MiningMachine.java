package com.ome_r.machinecraft.machines;

import com.ome_r.machinecraft.MachineCraft;
import com.ome_r.machinecraft.misc.ItemBuilder;
import com.ome_r.machinecraft.misc.MachineType;
import com.ome_r.machinecraft.misc.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class MiningMachine extends Machine{

    private int fortuneLevel;

    public MiningMachine(Location loc){
        super(loc, MachineType.MINING);
        this.fortuneLevel = 0;
        loadRunnable();
    }

    public void setFortuneLevel(int fortuneLevel){
        this.fortuneLevel = fortuneLevel;
        getSettings().setItem(2, new ItemBuilder(Material.ENCHANTED_BOOK).withName("§6Fortune Level")
                .withLore("§7Fortune " + fortuneLevel).asItemStack());
    }

    public int getFortuneLevel(){
        return fortuneLevel;
    }

    @Override
    public void loadSettings() {
        getSettings().setItem(0, new ItemBuilder(Material.DIAMOND_PICKAXE).withName("§aMining Machine").noAttributes()
                .withLore("§7Currently Mining: none").asItemStack());
        getSettings().setItem(1, new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 15).withName("§f").asItemStack());
        getSettings().setItem(2, new ItemBuilder(Material.ENCHANTED_BOOK).withName("§6Fortune Level")
                .withLore("§7Fortune " + fortuneLevel).asItemStack());
        getSettings().setItem(3, new ItemBuilder(Material.FEATHER).withGlowEffect().withName("§6Speed Level")
                .withLore("§7" + (MachineCraft.getInstance().getDataHandler().getSpeedTicks(getType()) / 20.0) + " sec / 1 block").asItemStack());
        getSettings().setItem(4, new ItemBuilder(Material.WATCH).withGlowEffect().withName("§6Running Status")
                .withLore(isRunning() ? "§7Running: §atrue" : "§7Running: §cfalse").asItemStack());
    }

    @Override
    public BukkitRunnable loadRunnable(){
          return new BukkitRunnable() {
            int x = 0, y = getLocation().getBlockY(), z = 0;

            @Override
            public void run() {
                for(Player pl : Bukkit.getOnlinePlayers()) {
                    if (pl.getLocation().distance(getLocation()) <= 16) {
                        Block b = getLocation().getChunk().getBlock(x, y, z);

                        if (b.getType().name().contains("ORE")) {
                            ItemStack is = (ItemStack) b.getDrops().toArray()[0];
                            is.setAmount(Methods.getDropCount(is.getType(), fortuneLevel, new Random()));
                            if (Methods.isInventoryEmpty(getInventory(), is))
                                getInventory().addItem(is);
                            else getLocation().getWorld().dropItemNaturally(getLocation(), is);
                            b.setType(Material.AIR);

                            getSettings().setItem(0, new ItemBuilder(Material.DIAMOND_PICKAXE).withName("§aMining Machine").noAttributes()
                                    .withLore("§7Currently Mining: " + b.getX() + " " + b.getY() + " " + b.getZ()).asItemStack());
                        }

                        x++;

                        if (x >= 16) {
                            x = 0;
                            z++;
                        }

                        if (z >= 16) {
                            x = 0;
                            z = 0;
                            y--;
                        }

                        if (y < 1)
                            cancel();

                        break;
                    }
                }
            }
        };
    }

}
