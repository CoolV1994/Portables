package com.coolv1994.portables.commands;

import com.coolv1994.portables.InvManager;
import com.coolv1994.portables.Plugin;
import com.coolv1994.portables.Utils;

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
                    player.sendMessage(Utils.getPhrase("noItemInHand"));
                    return true;
                }
		Material hand = player.getItemInHand().getType();

                Block target = player.getTargetBlock((Set<Material>) null, 100);
                if (target == null) {
                    player.sendMessage(Utils.getPhrase("notLookingAtBlock"));
                    return true;
                }

                if (!Plugin.getPortables().contains(hand)) {
                    player.sendMessage(Utils.getPhraseBlock("invalidItem", hand));
                    return true;
                }

                InvManager.link(player, player.getItemInHand(), target, null);
		return true;
	}
}
