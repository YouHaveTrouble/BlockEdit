package me.youhavetrouble.blockedit;

import com.google.common.collect.ImmutableSet;
import me.youhavetrouble.blockedit.api.BlockEditWand;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;
import java.util.HashMap;

public class WandsHandler {

    private final NamespacedKey wandKey;
    private static final HashMap<String, BlockEditWand> wands = new HashMap<>();

    protected WandsHandler(BlockEdit plugin) {
        wandKey = new NamespacedKey(plugin, "wand");

    }

    /**
     * Gets wand id from ItemStack
     * @param itemStack ItemStack to check
     * @return WandId if Itemstack is a wand, null otherwise
     */
    public String getWandId(ItemStack itemStack) {
        if (itemStack == null) return null;
        if (itemStack.getItemMeta() == null) return null;
        if (!itemStack.getItemMeta().getPersistentDataContainer().has(wandKey, PersistentDataType.STRING)) return null;
        return itemStack.getItemMeta().getPersistentDataContainer().get(wandKey, PersistentDataType.STRING);
    }

    /**
     * @return Immutable set of registered wand IDs
     */
    public Collection<String> getWandIds() {
        return ImmutableSet.copyOf(wands.keySet());
    }

    /**
     * Gets wand ItemStack from wand id
     * @param wandId Wand id
     * @return Wand ItemStack if wand with given ID exists, null otherwise
     */
    public ItemStack getWand(String wandId) {
        BlockEditWand wand = wands.get(wandId);
        if (wand == null) return null;
        ItemStack itemStack = new ItemStack(Material.WOODEN_AXE);
        ItemMeta meta = itemStack.getItemMeta();
        if (wand.getName() != null)
            meta.displayName(wand.getName());
        if (wand.getCustomModelData() != 0)
            meta.setCustomModelData(wand.getCustomModelData());
        meta.getPersistentDataContainer().set(wandKey, PersistentDataType.STRING, wandId);
        itemStack.setItemMeta(meta);
        return itemStack;
    }


    /**
     * PSA: Wand IDs will get converted to lowercase.
     * @return true if registered successfully, false if not
     */
    public boolean registerWand(BlockEditWand wand) {
        if (wands.containsKey(wand.getId().toLowerCase())) {
            BlockEdit.getPlugin().getLogger().warning("Tried to register wand with id \""+wand.getId()+"\", but wand with that id already exists!");
            return false;
        }
        wands.put(wand.getId().toLowerCase(), wand);
        return true;
    }


}
