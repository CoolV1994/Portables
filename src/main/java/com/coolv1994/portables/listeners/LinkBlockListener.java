package com.coolv1994.portables.listeners;

import com.coolv1994.portables.InvManager;
import com.coolv1994.portables.Plugin;
import com.coolv1994.portables.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinnie on 7/31/2015.
 */
public class LinkBlockListener implements Listener {
        private final List<Material> usePortables;

	public LinkBlockListener(Plugin plugin) {
            usePortables = new ArrayList<>();
            for (Material portable : Plugin.getPortables()) {
                if (plugin.getConfig().getBoolean("portables." + portable + ".Link")) {
                    usePortables.add(portable);
                }
            }
	}

	@EventHandler
	public void onLeftClick(PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK))
			return;
		if (event.getItem() == null)
			return;
		if (event.getClickedBlock() == null)
			return;

		Material hand = event.getItem().getType();
		Material clicked = event.getClickedBlock().getType();

                if (!usePortables.contains(event.getMaterial())) {
                    return;
                }
                if (!(clicked.equals(hand)||
                        Plugin.getAltPortables(hand).contains(clicked))) {
                    return;
                }
                if (Utils.canSkipLink(event.getItem())) {
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Plugin.getInstance().getConfig().getString("invalidItem"))
                            .replace("{block}", Plugin.getInstance().getConfig()
                                    .getString("portables." + event.getMaterial().name() + ".Name")));
                    return;
                }
                if (Utils.doesNotHavePermission(event.getPlayer(), event.getMaterial().name(), 1)) {
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Plugin.getInstance().getConfig().getString("noPermLink"))
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
                if (Utils.isNotAuthorized(event.getPlayer(), event.getClickedBlock())) {
                    event.getPlayer().sendMessage(InvManager.blockLocked
                            .replace("{block}", Plugin.getInstance().getConfig()
                                    .getString("portables." + event.getMaterial().name() + ".Name")));
                    return;
                }
                if (!Utils.canHostInWorld(event.getClickedBlock().getWorld())) {
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Plugin.getInstance().getConfig().getString("hostWorldNotAllowed"))
                            .replace("{block}", Plugin.getInstance().getConfig()
                                    .getString("portables." + hand.name() + ".Name")));
                    return;
                }
                event.setCancelled(true);
                InvManager.locToLore(event.getClickedBlock().getLocation(), event.getItem());
	}
}
