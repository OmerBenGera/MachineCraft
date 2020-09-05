package com.ome_r.machinecraft.listeners;

import com.ome_r.machinecraft.misc.Methods;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class SpawnerPlace implements Listener {

    @EventHandler
    public void onSpawnerPlace(BlockPlaceEvent e){
        if(e.getBlockPlaced().getType() != Material.MOB_SPAWNER) return;

        EntityType type = Methods.getSpawnerEntity(e.getPlayer().getInventory().getItemInMainHand());

        if(type != null){
            BlockState state = e.getBlockPlaced().getState();
            CreatureSpawner spawner = (CreatureSpawner) state;
            spawner.setSpawnedType(type);
            state.update();
        }

    }

}
