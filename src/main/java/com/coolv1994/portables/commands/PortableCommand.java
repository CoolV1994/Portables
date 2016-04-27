package com.coolv1994.portables.commands;

import com.coolv1994.portables.InvManager;
import com.coolv1994.portables.Plugin;
import com.coolv1994.portables.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Vinnie on 7/31/2015.
 */
public class PortableCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			Player player = Bukkit.getPlayerExact(args[0]);
			if (player == null) {
				sender.sendMessage("Player is offline or invalid username.");
				return true;
			}
			if (args.length == 6) {
                            Material hand = Material.matchMaterial(args[1]);
                            if (Material.WORKBENCH.equals(hand)) {
                                player.openWorkbench(null, true);
                                return true;
                            }
                            if (Material.ENDER_CHEST.equals(hand)) {
                                player.openInventory(player.getEnderChest());
                                return true;
                            }
                            InvManager.openInventory(hand,
                                    player, new Location(
                                            Bukkit.getWorld(args[2]),
                                            Double.parseDouble(args[3]),
                                            Double.parseDouble(args[4]),
                                            Double.parseDouble(args[5])
                            ));
                            return true;
			}
			return false;
		}

		Player player = (Player) sender;

                if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                    if (player.hasPermission("portables.admin.reload")) {
                        Plugin.getInstance().reload();
                        player.sendMessage("[Portables] Config reloaded.");
                    } else {
                        player.sendMessage("[Portables] You do not have permission to reload config.");
                    }
                    return true;
                }

                if (player.getItemInHand() == null) {
                    player.sendMessage(Utils.getPhrase("noItemInHand"));
                    return true;
                }

                if (!Plugin.getPortables().contains(player.getItemInHand().getType())) {
                    player.sendMessage(Utils.getPhraseBlock("invalidItem", player.getItemInHand().getType()));
                    return true;
                }

                InvManager.open(player, player.getItemInHand(), null);
		return true;
	}
}
