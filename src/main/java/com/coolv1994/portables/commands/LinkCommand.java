package com.coolv1994.portables.commands;

import com.coolv1994.portables.InvManager;
import com.coolv1994.portables.Plugin;
import com.coolv1994.portables.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Created by Vinnie on 7/31/2015.
 */
public class LinkCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Must be used as player.");
			return true;
		}

		Player player = (Player) sender;

                if (player.getItemInHand() == null) {
                    player.sendMessage("You must have a portable item in your hand.");
                    return true;
                }

		Material hand = player.getItemInHand().getType();
                Block target = player.getTargetBlock((Set<Material>) null, 100);
		Material looking = target.getType();

                if (!Plugin.getPortables().contains(hand)) {
                    player.sendMessage("This is not a portable item.");
                    return true;
                }

                if (!(hand.equals(looking) ||
                        Plugin.getAltPortables(hand).contains(looking))) {
                    return true;
                }
                if (Utils.canSkipLink(player.getItemInHand())) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Plugin.getInstance().getConfig().getString("invalidItem"))
                            .replace("{block}", Plugin.getInstance().getConfig()
                                    .getString("portables." + looking.name() + ".Name")));
                    return true;
                }
                if (Utils.doesNotHavePermission(player, looking.name(), 1)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Plugin.getInstance().getConfig().getString("noPermLink"))
                            .replace("{block}", Plugin.getInstance().getConfig()
                                    .getString("portables." + looking.name() + ".Name")));
                    return true;
                }
                if (Utils.isNotAuthorized(player, target)) {
                    player.sendMessage(InvManager.blockLocked
                            .replace("{block}", Plugin.getInstance().getConfig()
                                    .getString("portables." + looking.name() + ".Name")));
                    return true;
                }
                InvManager.locToLore(target.getLocation(), player.getItemInHand());
		return true;
	}
}
