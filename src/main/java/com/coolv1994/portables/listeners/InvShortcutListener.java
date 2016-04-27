package com.coolv1994.portables.listeners;

import com.coolv1994.portables.InvManager;
import com.coolv1994.portables.Plugin;

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
            usePortables = new ArrayList<Material>();
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
            if (event.getWhoClicked() instanceof Player) {
                InvManager.open((Player) event.getWhoClicked(), event.getCurrentItem(), event);
            }
	}
}
