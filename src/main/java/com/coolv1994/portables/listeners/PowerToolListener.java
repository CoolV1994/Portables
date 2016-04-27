package com.coolv1994.portables.listeners;

import com.coolv1994.portables.InvManager;
import com.coolv1994.portables.Plugin;

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
            InvManager.open(event.getPlayer(), event.getItem(), event);
	}
}
