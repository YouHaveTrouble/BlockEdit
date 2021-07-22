package me.youhavetrouble.blockedit.api;

import com.google.common.collect.ImmutableSet;
import me.youhavetrouble.blockedit.BlockEdit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;
import java.util.HashMap;

public class BlockEditWands {

    private static final NamespacedKey wandKey = new NamespacedKey(BlockEdit.getPlugin(), "wand");
    private static final HashMap<String, BlockEditWand> wands = new HashMap<>();

    public static NamespacedKey getWandKey() {
        return wandKey;
    }

    /**
     * @param itemStack ItemStack to check
     * @return WandId if a wand, null otherwise
     */
    public static String isWand(ItemStack itemStack) {
        if (itemStack == null) return null;
        if (itemStack.getItemMeta() == null) return null;
        if (!itemStack.getItemMeta().getPersistentDataContainer().has(BlockEditWands.getWandKey(), PersistentDataType.STRING)) return null;
        return itemStack.getItemMeta().getPersistentDataContainer().get(BlockEditWands.getWandKey(), PersistentDataType.STRING);
    }

    /**
     * @return Immutable set of registered wand IDs
     */
    public static Collection<String> getWandIds() {
        return ImmutableSet.copyOf(wands.keySet());
    }

    public static ItemStack getWand(String wandId) {
        BlockEditWand wand = wands.get(wandId);
        if (wand == null) return null;
        ItemStack itemStack = new ItemStack(Material.WOODEN_AXE);
        ItemMeta meta = itemStack.getItemMeta();
        if (wand.getName() != null)
            meta.displayName(wand.getName());
        if (wand.getCustomModelData() != 0)
            meta.setCustomModelData(wand.getCustomModelData());
        meta.getPersistentDataContainer().set(BlockEditWands.getWandKey(), PersistentDataType.STRING, wandId);
        itemStack.setItemMeta(meta);
        return itemStack;
    }


    /**
     * PSA: Wand IDs will get converted to lowercase.
     * @return true if registered successfully, false if not
     */
    public static boolean registerWand(BlockEditWand wand) {
        if (wands.containsKey(wand.getId().toLowerCase())) {
            BlockEdit.getPlugin().getLogger().warning("Tried to register wand with id \""+wand.getId()+"\", but wand with that id already exists!");
            return false;
        }
        wands.put(wand.getId().toLowerCase(), wand);
        return true;
    }


}
