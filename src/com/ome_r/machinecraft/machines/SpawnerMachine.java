package com.ome_r.machinecraft.machines;

import com.ome_r.machinecraft.misc.MachineType;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnerMachine extends Machine {

    private EntityType entityType;

    public SpawnerMachine(Location loc, EntityType entityType){
        super(loc, MachineType.SPAWNER);
        this.entityType = entityType;
    }

    public EntityType getEntityType(){
        return entityType;
    }

    public void setEntityType(EntityType entityType){
        this.entityType = entityType;
    }

    @Override
    public void loadSettings() {}

    @Override
    public BukkitRunnable loadRunnable() {
        return null;
    }

}
