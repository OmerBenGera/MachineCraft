package com.ome_r.machinecraft.listeners;

import com.ome_r.machinecraft.MachineCraft;
import com.ome_r.machinecraft.machines.Machine;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class MachineBreak implements Listener {

    @EventHandler
    public void onMachineBreak(BlockBreakEvent e){
        Player pl = e.getPlayer();

        Machine machine = MachineCraft.getInstance().getMachineHandler().getMachine(e.getBlock().getLocation());

        if(machine != null) {
            pl.sendMessage("§aBroke " + MachineCraft.getInstance().getDataHandler().getMachineItem(machine.getType()).getItemMeta().getDisplayName() + " §a!");

            for(ItemStack is : machine.getInventory().getContents()) {
                if(is != null && is.getType() != Material.AIR)
                    machine.getLocation().getWorld().dropItemNaturally(machine.getLocation(), is);
            }

            MachineCraft.getInstance().getMachineHandler().deleteMachine(machine);
        }

    }

}
