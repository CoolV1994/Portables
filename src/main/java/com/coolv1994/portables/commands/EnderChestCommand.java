package com.coolv1994.portables.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Vinnie on 7/31/2015.
 */
public class EnderChestCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			if (args.length == 1) {
				Player target = Bukkit.getPlayer(args[0]);
                                if (target == null) {
                                    sender.sendMessage("Player is offline or invalid username.");
                                    return true;
                                }
                                target.openInventory(target.getEnderChest());
				return true;
			}
                        if (args.length == 2) {
                            Player target = Bukkit.getPlayer(args[0]);
                            if (target == null) {
                                sender.sendMessage("Target player is offline.");
                                return true;
                            }
                            Player source = Bukkit.getPlayer(args[1]);
                            if (source == null) {
                                sender.sendMessage("Source player is offline.");
                                return true;
                            }
                            target.openInventory(source.getEnderChest());
                            return true;
                        }
			return false;
		}

		Player player = (Player) sender;

                if (args.length == 1) {
                    if (!player.hasPermission("portables.enderchest.other")) {
                        player.sendMessage("You do not have permisison to view others enderchest.");
                        return true;
                    }
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        player.sendMessage("Player is offline.");
                        return true;
                    }
                    player.openInventory(target.getEnderChest());
                    return true;
                }
    
		player.openInventory(player.getEnderChest());
		return true;
	}
}
