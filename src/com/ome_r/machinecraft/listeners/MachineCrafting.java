package com.ome_r.machinecraft.listeners;

import com.ome_r.machinecraft.MachineCraft;
import com.ome_r.machinecraft.misc.MachineType;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class MachineCrafting implements Listener{

    //Simulates the crafting table event (which isn't working well)
    @EventHandler
    public void onCraft(InventoryClickEvent e){
        if(e.getClickedInventory() == null || e.getClickedInventory().getType() != InventoryType.WORKBENCH) return;

        CraftingInventory inv = (CraftingInventory) e.getClickedInventory();

        new BukkitRunnable(){
            @Override
            public void run() {
                List<ItemStack> recipe = new ArrayList<>();
                for(ItemStack is : inv.getMatrix()) {
                    if(is == null)
                        recipe.add(new ItemStack(Material.AIR));
                    else recipe.add(is);
                }

                boolean hasSetResult = false;

                for(MachineType type : MachineType.values()){
                    if(MachineCraft.getInstance().getDataHandler().getRecipe(type).equals(recipe)) {
                        inv.setResult(MachineCraft.getInstance().getDataHandler().getMachineItem(type));
                        hasSetResult = true;
                        break;
                    }
                }

                if(!hasSetResult){
                    if(inv.getRecipe() == null)
                        inv.setResult(new ItemStack(Material.AIR));

                    else
                        inv.setResult(inv.getRecipe().getResult());
                }

                for(HumanEntity he : inv.getViewers())
                    if(he instanceof Player)
                        ((Player) he).updateInventory();

            }
        }.runTaskLater(MachineCraft.getInstance(), 1L);

    }

}
