package com.ome_r.machinecraft.misc;

import com.ome_r.machinecraft.MachineCraft;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class DataHandler {

    private Map<String, ItemStack> customItems;
    private Map<MachineType, List<ItemStack>> recipes;
    private Map<MachineType, Integer> speedTicks;
    private int farmerDur;

    public DataHandler(){
        customItems = new HashMap<>();
        recipes = new HashMap<>();
        speedTicks = new HashMap<>();
        loadConfigFile();
    }

    public ItemStack getMachineItem(MachineType type){
        if(customItems.containsKey(type.name().toLowerCase() + "_machine"))
            return customItems.get(type.name().toLowerCase() + "_machine");
        return null;
    }

    public List<ItemStack> getRecipe(MachineType type){
        return recipes.get(type);
    }

    public int getSpeedTicks(MachineType type){
        if(speedTicks.containsKey(type))
            return speedTicks.get(type);
        return -1;
    }

    public int getFarmerDurability(){
        return farmerDur;
    }

    private void loadConfigFile(){
        File file = new File("plugins/machinecraft/config.yml");

        if(!file.exists())
            MachineCraft.getInstance().saveResource("config.yml", true);

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        //LOAD CUSTOM ITEMS SECTION

        for(String itemName : cfg.getConfigurationSection("items").getKeys(false)){
            if(!cfg.contains("items." + itemName + ".type")){
                Bukkit.getLogger().log(Level.INFO, "Error while loading the item '" + itemName + "' - MISSING TYPE");
                continue;
            }

            Material type;

            try{
                type = Material.valueOf(cfg.getString("items." + itemName + ".type"));
            } catch (IllegalArgumentException e){
                Bukkit.getLogger().log(Level.INFO, "Error while loading the item '" + itemName + "' - UNKNOWN TYPE");
                Bukkit.getPluginManager().disablePlugin(MachineCraft.getInstance());
                return;
            }

            ItemStack is = new ItemStack(type);
            ItemMeta im = is.getItemMeta();

            if(cfg.contains("items." + itemName + ".data"))
                is.setDurability((short) cfg.getInt("items." + itemName + ".data"));

            if(cfg.contains("items." + itemName + ".name"))
                im.setDisplayName(ChatColor.translateAlternateColorCodes('&', cfg.getString("items." + itemName + ".name")));

            if(cfg.contains("items." + itemName + ".lore")){
                List<String> lore = new ArrayList<>();

                for(String line : cfg.getStringList("items." + itemName + ".lore"))
                    lore.add(ChatColor.translateAlternateColorCodes('&', line));

                im.setLore(lore);
            }

            if(cfg.contains("items." + itemName + ".enchants")){
                for(String enchant : cfg.getStringList("items." + itemName + ".enchants")){
                    Enchantment enchantment = Enchantment.getByName(enchant.split(":")[0]);

                    if(enchantment == null){
                        Bukkit.getLogger().log(Level.INFO, "Error while loading the item '" + itemName + "' - UNKNOWN ENCHANTMENT");
                        Bukkit.getPluginManager().disablePlugin(MachineCraft.getInstance());
                    }else {
                        int level;

                        try{
                            level = Integer.valueOf(enchant.split(":")[1]);
                        } catch(IllegalArgumentException e){
                            Bukkit.getLogger().log(Level.INFO, "Error while loading the item '" + itemName + "' - UNKNOWN ENCHANTMENT LEVEL");
                            Bukkit.getPluginManager().disablePlugin(MachineCraft.getInstance());
                            return;
                        }

                        im.addEnchant(enchantment, level, true);
                    }
                }
            }

            is.setItemMeta(im);

            if(cfg.contains("items." + itemName + ".glow") && cfg.getBoolean("items." + itemName + ".glow"))
                is = new ItemBuilder(is).withGlowEffect().asItemStack();

            customItems.put(itemName, is);
        }

        //LOAD MACHINES SETTINGS

        for(String machine : cfg.getConfigurationSection("machines").getKeys(false)){
            MachineType type;

            try{
                type = MachineType.valueOf(machine.split("_")[0].toUpperCase());
            } catch (IllegalArgumentException e){
                Bukkit.getLogger().log(Level.INFO, "Error while loading the machine '" + machine + "' - UNKNOWN MACHINE TYPE");
                Bukkit.getPluginManager().disablePlugin(MachineCraft.getInstance());
                return;
            }

            if(cfg.contains("machines." + machine + ".interval"))
                speedTicks.put(type, cfg.getInt("machines." + machine + ".interval"));

            if(cfg.contains("machines." + machine + ".durability"))
                farmerDur = cfg.getInt("machines." + machine + ".durability");

            if(!cfg.contains("machines." + machine + ".item")){
                Bukkit.getLogger().log(Level.INFO, "Error while loading the machine '" + machine + "' - NO ITEM SECTION");
                Bukkit.getPluginManager().disablePlugin(MachineCraft.getInstance());
                return;
            }

            if(!customItems.containsKey(cfg.getString("machines." + machine + ".item"))){
                Bukkit.getLogger().log(Level.INFO, "Error while loading the machine '" + machine + "' - UNKNOWN ITEM");
                Bukkit.getPluginManager().disablePlugin(MachineCraft.getInstance());
                return;
            }

            ItemStack is = customItems.get(cfg.getString("machines." + machine + ".item"));
            customItems.remove(cfg.getString("machines." + machine + ".item"));
            customItems.put(machine, is);

            if(!cfg.contains("machines." + machine + ".recipe")){
                Bukkit.getLogger().log(Level.INFO, "Error while loading the machine '" + machine + "' - NO RECIPE SECTION");
                Bukkit.getPluginManager().disablePlugin(MachineCraft.getInstance());
                return;
            }

            recipes.put(type, getRecipe(cfg, "machines." + machine + ".recipe"));

        }

    }

    private List<ItemStack> getRecipe(YamlConfiguration cfg, String path){
        List<ItemStack> recipe = new ArrayList<>();
        for(String line : cfg.getStringList(path)) {
            for (String split : line.split(", ")) {
                String material = split.contains(":") ? split.split(":")[0] : split;
                int data = 0;

                if (split.contains(":"))
                    try {
                        data = Integer.valueOf(split.split(":")[1]);
                    } catch (IllegalArgumentException e) {
                        Bukkit.getLogger().log(Level.INFO, "Error while loading the mining machine recipe - UNKNOWN ITEM DATA FOR '" + split + "'");
                        Bukkit.getPluginManager().disablePlugin(MachineCraft.getInstance());
                        return null;
                    }

                ItemStack is;

                try {
                    Material type = Material.valueOf(material);
                    is = new ItemStack(type, 1, (short) data);
                } catch (IllegalArgumentException e) {
                    if (!customItems.containsKey(material)) {
                        Bukkit.getLogger().log(Level.INFO, "Error while loading the mining machine recipe - UNKNOWN ITEM TYPE '" + material + "'");
                        Bukkit.getPluginManager().disablePlugin(MachineCraft.getInstance());
                        return null;
                    }
                    is = customItems.get(material);
                }

                recipe.add(is);
            }
        }

        return recipe;
    }

}
