package com.coolv1994.portables;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinnie on 8/1/2015.
 */
public class InvManager {

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
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("W:" + location.getWorld().getName());
		lore.add("X:" + location.getX());
		lore.add("Y:" + location.getY());
		lore.add("Z:" + location.getZ());
		metaData.setLore(lore);
		item.setItemMeta(metaData);
	}

        public static void openInventory(Material material, Player player, Location location) {
            Block block = location.getWorld().getBlockAt(location);
            if (!(block.getType().equals(material) || Plugin.getAltPortables(material).contains(block.getType()))) {
                player.sendMessage(Utils.getPhraseBlock("blockMissing", material));
                return;
            }

            if (Utils.isNotAuthorized(player, block)) {
                player.sendMessage(Utils.getPhraseBlock("blockLocked", material));
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

        public static void open(final Player player, ItemStack item, Cancellable event) {
            final Material hand = item.getType();

            if (Utils.canSkip(item)) {
                return;
            }
            if (event != null) {
                event.setCancelled(true);
            }
            if (!player.hasPermission("portables." + hand.name() + ".use")) {
                player.sendMessage(Utils.getPhraseBlock("noPermUse", hand));
                return;
            }
            if (!Utils.canUseInWorld(player.getWorld())) {
                player.sendMessage(Utils.getPhrase("worldNotAllowed"));
                return;
            }

            if (Material.WORKBENCH.equals(hand)) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getInstance(),
                        new Runnable() {
                            @Override
                            public void run() {
                                player.openWorkbench(null, true);
                            }
                        }, 1L);
                return;
            }
            if (Material.ENDER_CHEST.equals(hand)) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getInstance(),
                        new Runnable() {
                            @Override
                            public void run() {
                                player.openInventory(player.getEnderChest());
                            }
                        }, 1L);
                return;
            }

            final Location location = InvManager.loreToLoc(item.getItemMeta());
            if (location == null) {
                player.sendMessage(Utils.getPhrase("invalidLocation"));
                return;
            }
            if (!Utils.canHostInWorld(location.getWorld())) {
                player.sendMessage(Utils.getPhrase("hostWorldNotAllowed"));
                return;
            }
            if (!Utils.isSameWorld(player.getWorld(), location.getWorld())) {
                player.sendMessage(Utils.getPhrase("worldNotSame"));
                return;
            }
            if (!Utils.isInRange(player.getLocation(), location)) {
                player.sendMessage(Utils.getPhrase("outOfRange")
                        .replace("{range}", String.valueOf(Utils.range)));
                return;
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getInstance(),
                    new Runnable() {
                        @Override
                        public void run() {
                            openInventory(hand, player, location);
                        }
                    }, 1L);
	}

        public static void link(Player player, ItemStack item, Block block, Cancellable event) {
            Material hand = item.getType();
            Material looking = block.getType();

            if (!(hand.equals(looking) ||
                    Plugin.getAltPortables(hand).contains(looking))) {
                player.sendMessage(Utils.getPhraseBlock("invalidItem", looking));
                return;
            }
            if (Utils.canSkipLink(player.getItemInHand())) {
                player.sendMessage(Utils.getPhraseBlock("invalidItem", hand));
                return;
            }
            if (!player.hasPermission("portables." + hand.name() + ".link")) {
                player.sendMessage(Utils.getPhraseBlock("noPermLink", hand));
                return;
            }
            if (!Utils.canUseInWorld(player.getWorld())) {
                player.sendMessage(Utils.getPhrase("worldNotAllowed"));
                return;
            }
            if (!Utils.canHostInWorld(block.getWorld())) {
                player.sendMessage(Utils.getPhrase("hostWorldNotAllowed"));
                return;
            }
            if (!Utils.isSameWorld(player.getWorld(), block.getWorld())) {
                player.sendMessage(Utils.getPhrase("worldNotSame"));
                return;
            }
            if (!Utils.isInRange(player.getLocation(), block.getLocation())) {
                player.sendMessage(Utils.getPhrase("outOfRange")
                        .replace("{range}", String.valueOf(Utils.range)));
                return;
            }
            if (Utils.isNotAuthorized(player, block)) {
                player.sendMessage(Utils.getPhraseBlock("blockLocked", looking));
                return;
            }
            if (event != null) {
                event.setCancelled(true);
            }
            locToLore(block.getLocation(), item);
        }

}
