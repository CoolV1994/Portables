package com.coolv1994.portables.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;

/**
 * Created by Vinnie on 7/31/2015.
 */
public class InvSeeCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			if (args.length == 2) {
				Player player = Bukkit.getPlayer(args[0]);
				if (player == null) {
                                        sender.sendMessage("Invalid source player.");
                                        return true;
                                }
                                Player target = Bukkit.getPlayer(args[1]);
                                if (target == null) {
                                        OfflinePlayer offline = Bukkit.getOfflinePlayer(args[1]);
                                        if (offline == null) {
                                                sender.sendMessage("Invalid target player.");
                                                return true;
                                        }
                                        target = offline.getPlayer();
                                }
                                player.openInventory(target.getEnderChest());
				return true;
			}
			return false;
		}

		Player player = (Player) sender;

		if (args.length > 0) {
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
                                OfflinePlayer offline = Bukkit.getOfflinePlayer(args[0]);
                                if (offline == null) {
                                        sender.sendMessage("Invalid player.");
                                        return true;
                                }
                                target = offline.getPlayer();
                        }
                        if (args.length == 2) {
                            if (args[1].equalsIgnoreCase("equipment") || args[1].equalsIgnoreCase("armor")) {
                                Inventory equipInv = Bukkit.createInventory(null, 9, target.getName() + "'s Equipment");
                                EntityEquipment equip = target.getEquipment();
                                equipInv.setItem(0, equip.getHelmet());
                                equipInv.setItem(1, equip.getChestplate());
                                equipInv.setItem(2, equip.getLeggings());
                                equipInv.setItem(3, equip.getBoots());
                                equipInv.setItem(4, equip.getItemInOffHand());
                                player.openInventory(equipInv);
                                return true;
                            }
                        }
                        Inventory playerInv = Bukkit.createInventory(null, 36, target.getName() + "'s Inventory");
                        playerInv.setContents(target.getInventory().getStorageContents());
                        player.openInventory(playerInv);
			return true;
		}

		return false;
	}
}
