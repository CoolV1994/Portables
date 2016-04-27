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
 * Created by Vinnie on 7/31/2015.
 */
public class LinkBlockListener implements Listener {
        private final List<Material> usePortables;

	public LinkBlockListener(Plugin plugin) {
            usePortables = new ArrayList<Material>();
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

                if (!usePortables.contains(event.getMaterial())) {
                    return;
                }
                InvManager.link(event.getPlayer(), event.getItem(), event.getClickedBlock(), null);
	}
}
