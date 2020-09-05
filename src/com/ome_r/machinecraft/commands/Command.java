package com.ome_r.machinecraft.commands;

import com.ome_r.machinecraft.MachineCraft;
import com.ome_r.machinecraft.misc.MachineType;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {

        if(sender instanceof Player){
            Player pl = (Player) sender;

            for(MachineType type : MachineType.values())
                pl.getInventory().addItem(MachineCraft.getInstance().getDataHandler().getMachineItem(type));
        }

        return false;
    }

}
