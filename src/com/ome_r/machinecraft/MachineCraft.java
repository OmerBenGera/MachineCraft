package com.ome_r.machinecraft;

import com.ome_r.machinecraft.commands.Command;
import com.ome_r.machinecraft.listeners.*;
import com.ome_r.machinecraft.machines.MachineHandler;
import com.ome_r.machinecraft.misc.DataHandler;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MachineCraft extends JavaPlugin{

    private static MachineCraft instance;

    private MachineHandler machineHandler;
    private DataHandler dataHandler;

    @Override
    public void onEnable() {
        setupClasses();
        if(getServer().getPluginManager().isPluginEnabled(this)){
            loadCommands();
            loadListeners();
        }
    }

    private void loadCommands(){
        getCommand("machinecraft").setExecutor(new Command());
    }

    private void loadListeners(){
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new MachineBreak(), this);
        pm.registerEvents(new MachineCrafting(), this);
        pm.registerEvents(new MachineOpen(), this);
        pm.registerEvents(new MachinePlace(), this);
        pm.registerEvents(new MachineStart(), this);
        pm.registerEvents(new SettingsInventory(), this);
        pm.registerEvents(new SpawnerPlace(), this);
    }

    private void setupClasses() {
        instance = this;
        machineHandler = new MachineHandler();
        dataHandler = new DataHandler();
    }

    public static MachineCraft getInstance(){
        return instance;
    }

    public MachineHandler getMachineHandler(){
        return machineHandler;
    }

    public DataHandler getDataHandler(){
        return dataHandler;
    }

}
