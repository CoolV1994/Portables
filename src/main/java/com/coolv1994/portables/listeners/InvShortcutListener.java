package com.coolv1994.portables.listeners;

import com.coolv1994.portables.InvManager;
import com.coolv1994.portables.Plugin;
import com.coolv1994.portables.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinnie on 7/31/2015.
 */
public class InvShortcutListener implements Listener {
        private final List<Material> usePortables;

	public InvShortcutListener(Plugin plugin) {
            usePortables = new ArrayList<>();
            for (Material portable : Plugin.getPortables()) {
                if (plugin.getConfig().getBoolean("portables." + portable + ".InvShortcut")) {
                    usePortables.add(portable);
                }
            }
	}

	@EventHandler
	public void onRightClick (final InventoryClickEvent event) {
            if (!event.isRightClick()) {
                return;
            }
            if (event.getCurrentItem() == null) {
                return;
            }

            final Material hand = event.getCurrentItem().getType();
            if (!usePortables.contains(hand)) {
                return;
            }
            if (Utils.canSkip(event.getCurrentItem())) {
                return;
            }

            final Player player = Bukkit.getPlayer(event.getWhoClicked().getUniqueId());
            if (Utils.doesNotHavePermission(player, hand.name(), 3)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Plugin.getInstance().getConfig().getString("noPermInvShortcut"))
                        .replace("{block}", Plugin.getInstance().getConfig()
                                .getString("portables." + hand.name() + ".Name")));
                return;
            }
            if (!Utils.canUseInWorld(player.getWorld())) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Plugin.getInstance().getConfig().getString("worldNotAllowed")));
                return;
            }

            if (Material.WORKBENCH.equals(hand)) {
                event.setCancelled(true);
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
                event.setCancelled(true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getInstance(),
                        new Runnable() {
                            @Override
                            public void run() {
                                player.openInventory(player.getEnderChest());
                            }
                        }, 1L);
                return;
            }

            final Location location = InvManager.loreToLoc(event.getCurrentItem().getItemMeta());
            if (location == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Plugin.getInstance().getConfig().getString("invalidLocation")));
                return;
            }
            if (!Utils.canHostInWorld(location.getWorld())) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Plugin.getInstance().getConfig().getString("hostWorldNotAllowed")));
                return;
            }
            if (!Utils.isInRange(player.getLocation(), location)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Plugin.getInstance().getConfig().getString("outOfRange"))
                        .replace("{range}", String.valueOf(Utils.range)));
                return;
            }

            event.setCancelled(true);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getInstance(),
                    new Runnable() {
                        @Override
                        public void run() {
                            InvManager.openItem(hand, player, location);
                        }
                    }, 1L);
	}
}
