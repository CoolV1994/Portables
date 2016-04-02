package com.coolv1994.portables.listeners;

import com.coolv1994.nameitem.events.ItemSetLoreEvent;
import com.coolv1994.nameitem.events.ItemSetNameEvent;

import com.coolv1994.portables.InvManager;
import com.coolv1994.portables.Plugin;
import com.coolv1994.portables.Utils;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Created by Vinnie on 7/31/2015.
 */
public class NameItemListener implements Listener {

    @EventHandler
    public void onSetName(ItemSetNameEvent event) {
        if (!Plugin.getPortables().contains(event.getItem().getType())) {
            return;
        }
        if (Utils.canSkip(event.getItem())) {
            return;
        }
        if (!event.getPlayer().hasPermission("portables.customize." + event.getItem().getType().name())) {
            event.getPlayer().sendMessage("[Portables] You do not have permission to rename this item.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSetLore(ItemSetLoreEvent event) {
        if (!Plugin.getPortables().contains(event.getItem().getType())) {
            return;
        }
        if (Utils.canSkip(event.getItem())) {
            return;
        }
        if (!event.getPlayer().hasPermission("portables.customize." + event.getItem().getType().name())) {
            event.getPlayer().sendMessage("[Portables] You do not have permission to set lore on this item.");
            event.setCancelled(true);
            return;
        }
        Location location = InvManager.loreToLoc(event.getItem().getItemMeta());
        if (location == null) {
            return;
        }
        event.getPlayer().sendMessage("[Portables] Changing the lore will break the link.");
        event.setCancelled(true);
    }

}
