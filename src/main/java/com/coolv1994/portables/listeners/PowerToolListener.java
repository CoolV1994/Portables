package com.coolv1994.portables.listeners;

import com.coolv1994.portables.InvManager;
import com.coolv1994.portables.Plugin;
import com.coolv1994.portables.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinnie on 8/2/2015.
 */
public class PowerToolListener implements Listener {
        private final List<Material> usePortables;

	public PowerToolListener(Plugin plugin) {
            usePortables = new ArrayList<Material>();
            for (Material portable : Plugin.getPortables()) {
                if (plugin.getConfig().getBoolean("portables." + portable + ".PowerTool")) {
                    usePortables.add(portable);
                }
            }
	}

	@EventHandler
	public void onLeftClick(PlayerInteractEvent event) {
            if (!(event.getAction().equals(Action.LEFT_CLICK_BLOCK) ||
                    event.getAction().equals(Action.LEFT_CLICK_AIR))) {
                return;
            }
            if (event.getItem() == null) {
                return;
            }
            if (!usePortables.contains(event.getMaterial())) {
                return;
            }
            if (Utils.canSkip(event.getItem())) {
                return;
            }
            if (Utils.doesNotHavePermission(event.getPlayer(), event.getMaterial().name(), 2)) {
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Plugin.getInstance().getConfig().getString("noPermPowerTool"))
                        .replace("{block}", Plugin.getInstance().getConfig()
                                .getString("portables." + event.getMaterial().name() + ".Name")));
                return;
            }
            if (!Utils.canUseInWorld(event.getPlayer().getWorld())) {
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Plugin.getInstance().getConfig().getString("worldNotAllowed"))
                        .replace("{block}", Plugin.getInstance().getConfig()
                                .getString("portables." + event.getMaterial().name() + ".Name")));
                return;
            }

            if (Material.WORKBENCH.equals(event.getMaterial())) {
                event.setCancelled(true);
                event.getPlayer().openWorkbench(null, true);
                return;
            }
            if (Material.ENDER_CHEST.equals(event.getMaterial())) {
                event.setCancelled(true);
                event.getPlayer().openInventory(event.getPlayer().getEnderChest());
                return;
            }

            Location location = InvManager.loreToLoc(event.getItem().getItemMeta());
            if (location == null) {
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Plugin.getInstance().getConfig().getString("invalidLocation")));
                return;
            }
            if (!Utils.canHostInWorld(location.getWorld())) {
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Plugin.getInstance().getConfig().getString("hostWorldNotAllowed"))
                        .replace("{block}", Plugin.getInstance().getConfig()
                                .getString("portables." + event.getMaterial().name() + ".Name")));
                return;
            }
            if (!Utils.isInRange(event.getPlayer().getLocation(), location)) {
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Plugin.getInstance().getConfig().getString("outOfRange"))
                        .replace("{range}", String.valueOf(Utils.range)));
                return;
            }

            event.setCancelled(true);
            InvManager.openItem(event.getMaterial(), event.getPlayer(), location);
	}
}
