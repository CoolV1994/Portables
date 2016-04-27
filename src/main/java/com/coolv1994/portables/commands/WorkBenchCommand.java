package com.coolv1994.portables.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Vinnie on 7/31/2015.
 */
public class WorkBenchCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			if (args.length == 1) {
				Player target = Bukkit.getPlayer(args[0]);
                                if (target == null) {
                                    sender.sendMessage("Player is offline or invalid username.");
                                    return true;
                                }
                                target.openWorkbench(null, true);
				return true;
			}
			return false;
		}

		Player player = (Player) sender;
		player.openWorkbench(null, true);
		return true;
	}
}
