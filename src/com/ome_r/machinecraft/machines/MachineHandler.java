package com.ome_r.machinecraft.machines;

import com.ome_r.machinecraft.misc.MachineType;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

public class MachineHandler {

    private Set<Machine> machines;

    public MachineHandler(){
        machines = new HashSet<>();
    }

    public Machine newMachine(Location loc, MachineType type){
        Machine machine;

        switch(type){
            case MINING:
                machine = new MiningMachine(loc);
                break;
            case FARMING:
                machine = new FarmingMachine(loc);
                break;
            default:
                return null;
        }

        machines.add(machine);
        return machine;
    }

    public Machine getMachine(Location loc){
        return getMachine(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public Machine getMachine(int x, int y, int z){
        for(Machine machine : machines)
            if(machine.getLocation().getBlockX() == x && machine.getLocation().getBlockY() == y &&
                    machine.getLocation().getBlockZ() == z)
                return machine;

        return null;
    }

    public Set<Machine> getMachines(){
        return machines;
    }

    public void deleteMachine(Machine machine){
        try {
            machine.stopMachine();
        } catch (IllegalStateException ignored) {}
        machines.remove(machine);
    }

}
