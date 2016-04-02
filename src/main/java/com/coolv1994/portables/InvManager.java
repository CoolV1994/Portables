package com.coolv1994.portables;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinnie on 8/1/2015.
 */
public class InvManager {
	public static String blockLocked = "";
	public static String blockMissing = "";

	// Get Location from Item's MetaData
	public static Location loreToLoc(ItemMeta metaData) {
            if (!metaData.hasLore())
                return null;

            List<String> lore = metaData.getLore();
            if (lore.size() != 4)
                return null;
            if (!lore.get(0).startsWith("W:"))
                return null;
            if (!lore.get(1).startsWith("X:"))
                return null;
            if (!lore.get(2).startsWith("Y:"))
                return null;
            if (!lore.get(3).startsWith("Z:"))
                return null;
            World w = Bukkit.getWorld(lore.get(0).substring(2));
            double x = Double.parseDouble(lore.get(1).substring(2));
            double y = Double.parseDouble(lore.get(2).substring(2));
            double z = Double.parseDouble(lore.get(3).substring(2));
            return new Location(w, x, y, z);
	}

	// Store Location in Item's MetaData
	public static void locToLore(Location location, ItemStack item) {
		ItemMeta metaData = item.getItemMeta();
		metaData.setDisplayName(
                        ChatColor.translateAlternateColorCodes('&',
                                Plugin.getInstance().getConfig().getString(
                                        "portables." + item.getType().name() + ".LinkedName")));
		ArrayList<String> lore = new ArrayList<>();
		lore.add("W:" + location.getWorld().getName());
		lore.add("X:" + location.getX());
		lore.add("Y:" + location.getY());
		lore.add("Z:" + location.getZ());
		metaData.setLore(lore);
		item.setItemMeta(metaData);
	}

        public static void openItem(Material material, Player player, Location location) {
            if (Material.WORKBENCH.equals(material)) {
                player.openWorkbench(location, true);
                return;
            }
            if (Material.ENDER_CHEST.equals(material)) {
                player.openInventory(player.getEnderChest());
                return;
            }
    
            Block block = location.getWorld().getBlockAt(location);
            if (!(block.getType().equals(material) || Plugin.getAltPortables(material).contains(block.getType()))) {
                player.sendMessage(blockMissing.replace("{block}", Plugin.getInstance()
                        .getConfig().getString("portables." + material.name() + ".Name")));
                return;
            }

            if (Utils.isNotAuthorized(player, block)) {
                player.sendMessage(blockLocked.replace("{block}", Plugin.getInstance()
                        .getConfig().getString("portables." + material.name() + ".Name")));
                return;
            }

            if (Material.ENCHANTMENT_TABLE.equals(material)) {
                player.openEnchanting(location, true);
                return;
            }
            if (block.getState() instanceof InventoryHolder) {
                InventoryHolder container = (InventoryHolder) block.getState();
                player.openInventory(container.getInventory());
            }
        }
}
