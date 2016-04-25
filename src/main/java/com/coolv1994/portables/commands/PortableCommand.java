package com.coolv1994.portables.commands;

import com.coolv1994.portables.InvManager;
import com.coolv1994.portables.Plugin;
import com.coolv1994.portables.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
				sender.sendMessage("Player is invalid or offline.");
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
                            InvManager.openItem(hand,
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

                if (player.getItemInHand() == null) {
                    player.sendMessage("You must have a portable item in your hand.");
                    return true;
                }

		Material hand = player.getItemInHand().getType();
                if (!Plugin.getPortables().contains(hand)) {
                    player.sendMessage("This is not a portable item.");
                    return true;
                }

                if (Utils.canSkip(player.getItemInHand()))
                    return true;

                if (Utils.doesNotHavePermission(player, hand.name(), 2)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Plugin.getInstance().getConfig().getString("noPermCommand"))
                            .replace("{block}", Plugin.getInstance().getConfig()
                                    .getString("portables." + hand.name() + ".Name")));
                    return true;
                }
                if (!Utils.canUseInWorld(player.getWorld())) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Plugin.getInstance().getConfig().getString("worldNotAllowed"))
                            .replace("{block}", Plugin.getInstance().getConfig()
                                    .getString("portables." + hand.name() + ".Name")));
                    return true;
                }

                if (Material.WORKBENCH.equals(hand)) {
                    player.openWorkbench(null, true);
                    return true;
                }
                if (Material.ENDER_CHEST.equals(hand)) {
                    player.openInventory(player.getEnderChest());
                    return true;
                }

                Location location = InvManager.loreToLoc(player.getItemInHand().getItemMeta());
                if (location == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Plugin.getInstance().getConfig().getString("invalidLocation")));
                    return true;
                }
                if (!Utils.canHostInWorld(location.getWorld())) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Plugin.getInstance().getConfig().getString("hostWorldNotAllowed"))
                            .replace("{block}", Plugin.getInstance().getConfig()
                                    .getString("portables." + hand.name() + ".Name")));
                    return true;
                }
                if (!Utils.isInRange(player.getLocation(), location)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Plugin.getInstance().getConfig().getString("outOfRange"))
                            .replace("{range}", String.valueOf(Utils.range)));
                    return true;
                }

                InvManager.openItem(hand, player, location);
		return true;
	}
}
