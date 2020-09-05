package com.ome_r.machinecraft.misc;

import com.ome_r.machinecraft.MachineCraft;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class Methods {

    private static DataHandler data;
    private static String version;

    static{
        data = MachineCraft.getInstance().getDataHandler();
        version = MachineCraft.getInstance().getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    public static int getDropCount(Material mat, int fortuneLevel, Random random){
        if (fortuneLevel > 0){
            int drops = random.nextInt(fortuneLevel + 2) - 1;
            if (drops < 0)
                drops = 0;

            int i = mat == Material.LAPIS_ORE ? 4 + random.nextInt(5) : 1;
            return i * (drops + 1);
        }

        return 1;
    }

    public static MachineType getMachineType(ItemStack is){
        ItemStack itemStack = is.clone();
        itemStack.setAmount(1);

        for(MachineType type : MachineType.values())
            if(itemStack.equals(data.getMachineItem(type)))
                return type;

        return null;
    }

    public static boolean isSameStack(ItemStack is1, ItemStack is2){
        ItemStack itemStack1 = is1.clone(), itemStack2 = is2.clone();
        itemStack1.setAmount(1);
        itemStack2.setAmount(1);
        return itemStack1.equals(itemStack2);
    }

    public static boolean isInventoryEmpty(Inventory inv, ItemStack is){
        for(ItemStack itemStack : inv.getContents()) {
            if (itemStack == null || itemStack.getType() == Material.AIR)
                return true;
            else if (itemStack.getType() == is.getType() && itemStack.getDurability() == is.getDurability() &&
                    itemStack.getAmount() <= (itemStack.getMaxStackSize() - 1))
                return true;
        }

        return false;
    }

    public static ItemStack getSpawnerStack(EntityType entity){
        try {
            Object itemStack = getBukkitClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class)
                    .invoke(null, new ItemStack(Material.MOB_SPAWNER));

            Object tag = itemStack.getClass().getMethod("getTag").invoke(itemStack);

            if(tag == null)
                tag = getNMSClass("NBTTagCompound").newInstance();

            tag.getClass().getMethod("setString", String.class, String.class)
                    .invoke(tag, "entitySpawnType", entity.name());

            itemStack.getClass().getMethod("setTag", tag.getClass())
                    .invoke(itemStack, tag);

            return (ItemStack) getBukkitClass("inventory.CraftItemStack").getMethod("asBukkitCopy", itemStack.getClass())
                    .invoke(null, itemStack);
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException |
                IllegalAccessException | InstantiationException e){
            e.printStackTrace();
            return null;
        }
    }

    public static EntityType getSpawnerEntity(ItemStack is){
        try{
            Object itemStack = getBukkitClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class)
                    .invoke(null, is);

            Object tag = itemStack.getClass().getMethod("getTag").invoke(itemStack);

            if(tag != null){
                Object entitySpawnType = tag.getClass().getMethod("getString", String.class).invoke(tag, "entitySpawnType");
                if(entitySpawnType != null)
                    return EntityType.valueOf(entitySpawnType.toString());
            }

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        }
        return null;
    }

    private static Class getNMSClass(String name){
        try{
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    private static Class getBukkitClass(String name){
        try{
            return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
        } catch (ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

}
