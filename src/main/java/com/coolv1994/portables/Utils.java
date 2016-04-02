package com.coolv1994.portables;

import com.daemitus.deadbolt.Deadbolt;
import com.griefcraft.lwc.LWC;
import me.crafter.mc.lockettepro.LocketteProAPI;
import org.yi.acru.bukkit.Lockette.Lockette;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import org.bukkit.Location;

/**
 *
 * @author Vinnie
 */
public class Utils {
    public static byte lockPlugin = 0;
    public static boolean useWorldBlackList;
    public static List<String> useInWorlds;
    public static boolean hostWorldBlackList;
    public static List<String> hostWorlds;
    public static int range = 0;

    public static boolean doesNotHavePermission(Player player, String item, int action) {
        // Link Item
        if (action == 1) {
            return Plugin.getInstance().getConfig().getBoolean(item + ".LinkPerm") &&
                    !player.hasPermission("portables." + item + ".link");
        }
        // PowerTool
        if (action == 2) {
            return Plugin.getInstance().getConfig().getBoolean(item + ".PowerToolPerm") &&
                    !player.hasPermission("portables." + item + ".powertool");
        }
        // Inv Shortcut
        if (action == 3) {
            return Plugin.getInstance().getConfig().getBoolean(item + ".InvShortcutPerm") &&
                    !player.hasPermission("portables." + item + ".invshortcut");
        }
        return true;
    }

    public static boolean canUseInWorld(World world) {
        if (useWorldBlackList) {
            return !useInWorlds.contains(world.getName());
        }
        return useInWorlds.contains(world.getName());
    }

    public static boolean canHostInWorld(World world) {
        if (hostWorldBlackList) {
            return !hostWorlds.contains(world.getName());
        }
        return hostWorlds.contains(world.getName());
    }

    public static boolean isInRange(Location player, Location block) {
        if (range == -1) {
            return true;
        }
        return player.distance(block) <= range;
    }

    public static boolean isNotAuthorized(Player player, Block block) {
        // Bypass lock
        if (player.hasPermission("portables.bypasslock"))
            return false;
        // Deadbolt
        if (lockPlugin == 1)
            return Deadbolt.isProtected(block) && !Deadbolt.isAuthorized(player, block);
        // LWC
        if (lockPlugin == 2)
            return !LWC.getInstance().canAccessProtection(player, block);
        // Lockette
        if (lockPlugin == 3)
            return Lockette.isProtected(block) && !Lockette.isUser(block, player, true);
        // LockettePro
        if (lockPlugin == 4)
            return LocketteProAPI.isLocked(block) && !LocketteProAPI.isUser(block, player.getName());
        // No protection
        return false;
    }

    public static boolean canSkip(ItemStack item) {
        if (Plugin.getInstance().getConfig().getBoolean("portables." + item.getType() + ".CraftOnly")) {
            if (!item.hasItemMeta())
                return true;
            if (!item.getItemMeta().hasDisplayName())
                return true;
            if (!item.getItemMeta().hasLore())
                return true;
        }
        return false;
    }

    public static boolean canSkipLink(ItemStack item) {
        if (Plugin.getInstance().getConfig().getBoolean("portables." + item.getType() + ".CraftOnly")) {
            if (!item.hasItemMeta())
                return true;
            if (!item.getItemMeta().hasDisplayName())
                return true;
            if (!item.getItemMeta().hasLore())
                return true;
            return !item.getItemMeta().getDisplayName().equals(
                            ChatColor.translateAlternateColorCodes('&',
                                    Plugin.getInstance().getConfig().getString(
                                            "portables." + item.getType().name() + ".CraftedName")));
        }
        if (!item.hasItemMeta())
            return false;
        if (!item.getItemMeta().hasDisplayName())
            return false;
        return item.getItemMeta().getDisplayName().equals(
                ChatColor.translateAlternateColorCodes('&',
                        Plugin.getInstance().getConfig().getString(
                                "portables." + item.getType().name() + ".LinkedName")));
    }
}
