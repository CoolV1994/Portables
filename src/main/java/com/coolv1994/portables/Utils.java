package com.coolv1994.portables;

import com.daemitus.deadbolt.Deadbolt;
import com.griefcraft.lwc.LWC;
import me.crafter.mc.lockettepro.LocketteProAPI;
import org.yi.acru.bukkit.Lockette.Lockette;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;

import java.util.List;

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

    public static boolean isSameWorld(World item, World host) {
        if (Plugin.getInstance().getConfig().getBoolean("forceSameWorld")) {
            return item.getName().equals(host.getName());
        }
        return true;
    }

    public static double distanceSquared(Location loc1, Location loc2) {
        return NumberConversions.square(loc1.getX() - loc2.getX()) +
                NumberConversions.square(loc1.getY() - loc2.getY()) +
                NumberConversions.square(loc1.getZ() - loc2.getZ());
    }

    public static double measureDistance(Location loc1, Location loc2) {
        return Math.sqrt(distanceSquared(loc1, loc2));
    }

    public static boolean isInRange(Location player, Location block) {
        if (range == -1) {
            return true;
        }
        //return player.distance(block) <= range;
        return measureDistance(player, block) <= range;
    }

    public static boolean isNotAuthorized(Player player, Block block) {
        // Bypass lock
        if (player.hasPermission("portables.admin.bypasslock"))
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
                    getPhrase("portables." + item.getType().name() + ".CraftedName"));
        }
        if (!item.hasItemMeta())
            return false;
        if (!item.getItemMeta().hasDisplayName())
            return false;
        return item.getItemMeta().getDisplayName().equals(
                getPhrase("portables." + item.getType().name() + ".LinkedName"));
    }

    public static String getPhrase(String id) {
        String phrase = Plugin.getInstance().getConfig().getString(id);
        return ChatColor.translateAlternateColorCodes('&', phrase);
    }

    public static String getPhraseBlock(String id, Material item) {
        return getPhrase(id).replace("{block}", item.name());
    }
}
