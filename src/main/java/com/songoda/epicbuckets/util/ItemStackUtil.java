package com.songoda.epicbuckets.util;

import com.songoda.epicbuckets.EpicBuckets;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * ItemStackUtil created by: SoFocused
 * Date Created: oktober 03 2018
 * Time created: 16:26
 */
public class ItemStackUtil {

    public static ItemStack createItemStack(String name, List<String> lore, Material m, int amount, int i, boolean enchantItem) {
        ItemStack itemStack = new ItemStack(m, amount, (short) i);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (name != null)
            itemMeta.setDisplayName(ChatUtil.colorString(name));
        if (lore != null)
            itemMeta.setLore(ChatUtil.colorList(lore));

        if (enchantItem) {

            itemMeta.addEnchant(Enchantment.DURABILITY, 1, false);

            if (!EpicBuckets.getInstance().getServer().getVersion().contains("1.7"))
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }


}
