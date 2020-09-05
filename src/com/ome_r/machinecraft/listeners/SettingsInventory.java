package com.ome_r.machinecraft.listeners;

import com.ome_r.machinecraft.MachineCraft;
import com.ome_r.machinecraft.machines.FarmingMachine;
import com.ome_r.machinecraft.machines.Machine;
import com.ome_r.machinecraft.machines.MiningMachine;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class SettingsInventory implements Listener {

    @EventHandler
    public void onSettingsClick(InventoryClickEvent e){
        if(!e.getInventory().getTitle().equals("§6Machine Settings") ||
                !(e.getWhoClicked() instanceof Player)) return;

        Player pl = (Player) e.getWhoClicked();

        if(e.getCurrentItem() == null)
            return;

        if(e.getRawSlot() <= 4) {
            e.setCancelled(true);
            ItemStack is = pl.getInventory().getItemInMainHand();

            if(e.getCurrentItem().getType() == Material.ENCHANTED_BOOK){
                MiningMachine machine = (MiningMachine) getMachine(e.getInventory().getItem(0).getItemMeta().getLore().get(0));

                int level = getFortuneLevel(is);

                if (level == 0) {
                    pl.sendMessage("§cYou must hold an item with fortune on it.");
                    return;
                }

                if (machine.getFortuneLevel() >= level) {
                    pl.sendMessage("§cYou cannot downgrade the fortune level of the machine.");
                    return;
                }

                machine.setFortuneLevel(level);
                removeFortuneLevel(is);

                pl.sendMessage("§aYou upgraded the mining machine to a fortune level " + level + ".");
                pl.openInventory(machine.getSettings());
            }

            else if(e.getCurrentItem().getType() == Material.STICK){
                if(is.getType() != Material.DIAMOND){
                    pl.sendMessage("§cYou must hold diamonds to repair the farming machine.");
                    return;
                }

                FarmingMachine machine = (FarmingMachine) getMachine(e.getInventory().getItem(0).getItemMeta().getLore().get(0));
                int amount = pl.getInventory().getItemInMainHand().getAmount();
                int dur = amount * 20;

                if(dur > MachineCraft.getInstance().getDataHandler().getFarmerDurability()){
                    dur = MachineCraft.getInstance().getDataHandler().getFarmerDurability();
                }

                if(machine.getDurability() + dur > MachineCraft.getInstance().getDataHandler().getFarmerDurability()){
                    dur = MachineCraft.getInstance().getDataHandler().getFarmerDurability() - machine.getDurability();
                }

                machine.setDurability(machine.getDurability() + dur);

                is.setAmount(is.getAmount() - (int) Math.round((dur / 20.0)));

            }

        }

    }

    private Machine getMachine(String str){
        String[] split = str.split(" ");
        return MachineCraft.getInstance().getMachineHandler().getMachine(Integer.valueOf(split[1]), Integer.valueOf(split[2]), Integer.valueOf(split[3]));
    }

    private int getFortuneLevel(ItemStack is){
        if(is.getType() != Material.ENCHANTED_BOOK)
            return is.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);

        return ((EnchantmentStorageMeta) is.getItemMeta()).getStoredEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);
    }

    private void removeFortuneLevel(ItemStack is){
        if(is.getType() != Material.ENCHANTED_BOOK)
            is.removeEnchantment(Enchantment.LOOT_BONUS_BLOCKS);
        else is.setType(Material.BOOK);
    }

}
