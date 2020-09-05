package com.ome_r.machinecraft.machines;

import com.ome_r.machinecraft.MachineCraft;
import com.ome_r.machinecraft.misc.ItemBuilder;
import com.ome_r.machinecraft.misc.MachineType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Collections;

public abstract class Machine {

    private final Location loc;
    private final Inventory inv, settings;
    private final MachineType type;

    private Runnable run;
    private int taskID;
    private boolean isRunning, canRunAgain;

    public Machine(Location loc, MachineType type){
        this.loc = loc;
        this.type = type;
        this.settings = Bukkit.createInventory(null, InventoryType.HOPPER, "§6Machine Settings");
        this.inv = Bukkit.createInventory(null, 9 * 3, MachineCraft.getInstance().getDataHandler().getMachineItem(type).getItemMeta().getDisplayName());
        this.run = loadRunnable();
        this.isRunning = false;
        this.canRunAgain = true;
        loadSettings();
    }

    public abstract void loadSettings();

    public abstract BukkitRunnable loadRunnable();

    public Location getLocation() {
        return loc;
    }

    public MachineType getType() {
        return type;
    }

    public Inventory getInventory() {
        return inv;
    }

    public Inventory getSettings() {
        return settings;
    }

    public void startMachine(){
        isRunning = true;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(MachineCraft.getInstance(), run, 0, MachineCraft.getInstance().getDataHandler().getSpeedTicks(type));
        settings.setItem(4, new ItemBuilder(settings.getItem(4)).withLore("§7Running: §atrue").asItemStack());
    }

    public void stopMachine(){
        isRunning = false;
        canRunAgain = false;
        Bukkit.getScheduler().cancelTask(taskID);
        settings.setItem(4, new ItemBuilder(settings.getItem(4)).withLore("§7Running: §cfalse").asItemStack());
        Bukkit.getScheduler().runTaskLater(MachineCraft.getInstance(), () -> canRunAgain = true, MachineCraft.getInstance().getDataHandler().getSpeedTicks(type));
    }

    public boolean isRunning(){
        return isRunning;
    }

    public boolean canRunAgain(){
        return canRunAgain;
    }

}
