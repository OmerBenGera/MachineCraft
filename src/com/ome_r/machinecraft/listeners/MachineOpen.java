package com.ome_r.machinecraft.listeners;

import com.ome_r.machinecraft.MachineCraft;
import com.ome_r.machinecraft.machines.Machine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class MachineOpen implements Listener {

    @EventHandler
    public void onMachineOpen(PlayerInteractEvent e){
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        Machine miner = MachineCraft.getInstance().getMachineHandler().getMachine(e.getClickedBlock().getLocation());

        if(miner != null){
            e.setCancelled(true);
            if(e.getPlayer().isSneaking())
                e.getPlayer().openInventory(miner.getSettings());
            else e.getPlayer().openInventory(miner.getInventory());
        }

    }

}
