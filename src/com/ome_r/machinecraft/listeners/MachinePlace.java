package com.ome_r.machinecraft.listeners;

import com.ome_r.machinecraft.MachineCraft;
import com.ome_r.machinecraft.misc.MachineType;
import com.ome_r.machinecraft.misc.Methods;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class MachinePlace implements Listener {

    @EventHandler
    public void onMachinePlace(BlockPlaceEvent e){
        Player pl = e.getPlayer();

        MachineType type = Methods.getMachineType(pl.getInventory().getItemInMainHand());

        if (type != null) {
            if(type == MachineType.SPAWNER){
                Block under = pl.getWorld().getBlockAt(e.getBlockPlaced().getLocation().subtract(0, 1, 0));

                if(under.getType() == Material.MOB_SPAWNER){
                    under.getWorld().dropItemNaturally(under.getLocation(),
                            Methods.getSpawnerStack(((CreatureSpawner) under.getState()).getSpawnedType()));
                    e.getBlockPlaced().setType(Material.AIR);
                    under.setType(Material.AIR);
                }

                else{
                    pl.sendMessage("§cThis machine must be placed on a spawner.");
                    e.getBlockPlaced().setType(Material.AIR);
                    if(pl.getGameMode() != GameMode.CREATIVE)
                        e.getBlockPlaced().getWorld().dropItemNaturally(e.getBlockPlaced().getLocation(),
                                MachineCraft.getInstance().getDataHandler().getMachineItem(MachineType.SPAWNER));
                }

                return;
            }
            pl.sendMessage("§aPlaced " + MachineCraft.getInstance().getDataHandler().getMachineItem(type).getItemMeta().getDisplayName() + " §a!");
            MachineCraft.getInstance().getMachineHandler().newMachine(e.getBlockPlaced().getLocation(), type);
        }

    }

}
