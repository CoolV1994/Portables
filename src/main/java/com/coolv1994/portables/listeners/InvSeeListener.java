package com.coolv1994.portables.listeners;

import com.coolv1994.portables.Plugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

/**
 * Created by Vinnie on 7/31/2015.
 */
public class InvSeeListener implements Listener {

	public InvSeeListener(Plugin plugin) {
	}

	@EventHandler
	public void onClick(final InventoryClickEvent event) {
            if (event.getCurrentItem() == null)
                return;

            String invTitle = event.getWhoClicked().getOpenInventory().getTopInventory().getTitle();
            if (!(invTitle.endsWith("'s Inventory") ||
                    invTitle.endsWith("'s Equipment") ||
                    invTitle.endsWith("'s EnderChest")))
                return;

            if (!event.getWhoClicked().hasPermission("portables.modifyother")) {
                event.setCancelled(true);
                return;
            }

            // Prevent item duping
            if (invTitle.startsWith(event.getWhoClicked().getName())) {
                event.setCancelled(true);
                return;
            }
	}

	@EventHandler
	public void onClose(final InventoryCloseEvent event) {
            if (!event.getPlayer().hasPermission("portables.modifyother")) {
                return;
            }

            String invTitle = event.getPlayer().getOpenInventory().getTopInventory().getTitle();

            // Prevent item duping
            if (invTitle.startsWith(event.getPlayer().getName())) {
                return;
            }

            if (invTitle.endsWith("'s Inventory")) {
                Player target = Bukkit.getPlayer(invTitle.substring(0, invTitle.indexOf("'s Inventory")));
                Inventory inv = event.getInventory();
                target.getInventory().setStorageContents(inv.getContents());
                target.updateInventory();
                return;
            }
            if (invTitle.endsWith("'s EnderChest")) {
                Player target = Bukkit.getPlayer(invTitle.substring(0, invTitle.indexOf("'s EnderChest")));
                Inventory inv = event.getInventory();
                target.getEnderChest().setContents(inv.getContents());
                return;
            }
            if (invTitle.endsWith("'s Equipment")) {
                Player target = Bukkit.getPlayer(invTitle.substring(0, invTitle.indexOf("'s Equipment")));
                Inventory inv = event.getInventory();
                target.getInventory().setHelmet(inv.getItem(0));
                target.getInventory().setChestplate(inv.getItem(1));
                target.getInventory().setLeggings(inv.getItem(2));
                target.getInventory().setBoots(inv.getItem(3));
                target.getInventory().setItemInOffHand(inv.getItem(4));
                target.updateInventory();
                return;
            }
        }
}
