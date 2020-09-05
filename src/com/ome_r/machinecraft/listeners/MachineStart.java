package com.ome_r.machinecraft.listeners;

import com.ome_r.machinecraft.MachineCraft;
import com.ome_r.machinecraft.machines.Machine;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class MachineStart implements Listener {

    @EventHandler
    public void onMachineHit(PlayerInteractEvent e){
        if(!e.getAction().equals(Action.LEFT_CLICK_BLOCK) ||
                e.getPlayer().getInventory().getItemInMainHand().getType() != Material.STICK) return;

        Machine machine = MachineCraft.getInstance().getMachineHandler().getMachine(e.getClickedBlock().getLocation());

        if(machine == null)
            return;

        else{
            e.setCancelled(true);
            if(machine.isRunning()) {
                e.getPlayer().sendMessage("§aYou turned off the machine.");
                machine.stopMachine();
            }

            else if(!machine.canRunAgain())
                e.getPlayer().sendMessage("§cThe machine is too hot. Try again later.");

            else{
                e.getPlayer().sendMessage("§aYou turned on the machine.");
                machine.startMachine();
            }
        }

    }

}
