package com.coolv1994.portables;

import com.coolv1994.portables.commands.EnderChestCommand;
import com.coolv1994.portables.commands.LinkCommand;
import com.coolv1994.portables.commands.PortableCommand;
import com.coolv1994.portables.commands.WorkBenchCommand;
import com.coolv1994.portables.listeners.InvShortcutListener;
import com.coolv1994.portables.listeners.LinkBlockListener;
import com.coolv1994.portables.listeners.NameItemListener;
import com.coolv1994.portables.listeners.PowerToolListener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

/**
 * Created by Vinnie on 7/31/2015.
 */
public class Plugin extends JavaPlugin {

	private static Plugin instance;
        private static List <Material> portables;
        private static Map<Material, List<Material>> altPortables;

	public static Plugin getInstance() {
		return instance;
	}

	public static List<Material> getPortables() {
		return portables;
	}

	public static List<Material> getAltPortables(Material item) {
		return altPortables.get(item);
	}

	private void loadPortableItems() {
            portables = new ArrayList<>();
            altPortables = new HashMap<>();
            /*List<String> items = getConfig().getStringList("portables");
            System.out.println("Portables: " + items);
            for (String item : items) {
                System.out.println("Portable Item: " + item);
                if (item.startsWith("ID:")) {
                    try {
                        int id = Integer.parseInt(item.substring(3));
                        portables.add(Material.getMaterial(id));
                    } catch (Exception e) {
                        getLogger().log(Level.SEVERE, "Item ID Error", e);
                    }
                    continue;
                }
                portables.add(Material.getMaterial(item));
            }*/
            Set<String> keys = getConfig().getKeys(true);
            for (String key : keys) {
                if (key.startsWith("portables.")) {
                    int nameEnd = key.indexOf(".", 10);
                    if (nameEnd > 0)
                        continue;
                    key = key.substring(10);
                    Material portable = Material.getMaterial(key);
                    if (portable == null) {
                        continue;
                    }
                    getLogger().log(Level.INFO, "Portable Item: {0}", portable.name());
                    portables.add(portable);

                    List<Material> altBlocks = new ArrayList<>();
                    List<String> alts = getConfig().getStringList("portables." + portable.name() + ".AltBlocks");
                    if (alts == null || alts.isEmpty()) {
                        altPortables.put(portable, altBlocks);
                        continue;
                    }
                    for (String alt : alts) {
                        altBlocks.add(Material.getMaterial(alt));
                    }
                    altPortables.put(portable, altBlocks);
                }
            }
            getLogger().log(Level.INFO, "Portable Items: {0}", portables);
	}

        private void loadMessages() {
            InvManager.blockLocked = ChatColor.translateAlternateColorCodes('&',
                    getConfig().getString("blockLocked"));
            InvManager.blockMissing = ChatColor.translateAlternateColorCodes('&',
                    getConfig().getString("blockMissing"));
        }

	private void setLockPlugin() {
		String plugin = getConfig().getString("lockPlugin").toLowerCase();
		if (plugin.equals("deadbolt")) {
			Utils.lockPlugin = 1;
			return;
		}
		if (plugin.equals("lwc")) {
			Utils.lockPlugin = 2;
			return;
		}
		if (plugin.equals("lockette")) {
			Utils.lockPlugin = 3;
			return;
		}
		if (plugin.equals("lockettepro")) {
			Utils.lockPlugin = 4;
			return;
		}
		Utils.lockPlugin = 0;
	}

	private void enableCommands() {
		getCommand("enderchest").setExecutor(new EnderChestCommand());
		if (!getConfig().getBoolean("portables.ENDER_CHEST.CommandPerm"))
			getCommand("enderchest").setPermission(null);
		getCommand("portable").setExecutor(new PortableCommand());
		getCommand("linkportable").setExecutor(new LinkCommand());
		getCommand("workbench").setExecutor(new WorkBenchCommand());
		if (!getConfig().getBoolean("portables.WORKBENCH.CommandPerm"))
			getCommand("workbench").setPermission(null);
	}

	private void enableListeners() {
		getServer().getPluginManager().registerEvents(new InvShortcutListener(this), this);
		getServer().getPluginManager().registerEvents(new PowerToolListener(this), this);
		getServer().getPluginManager().registerEvents(new LinkBlockListener(this), this);
                getServer().getPluginManager().registerEvents(new NameItemListener(), this);
	}

	private void addCraftingRecipes() {
		RecipeUtils utils = new RecipeUtils(this);
                for (Material portable : portables) {
                    utils.addRecipe(portable);
                }
	}

        private void loadWorlds() {
            Utils.range = getConfig().getInt("range");
            Utils.useWorldBlackList = getConfig().getBoolean("useWorldBlackList");
            Utils.useInWorlds = getConfig().getStringList("useInWorlds");
            Utils.hostWorldBlackList = getConfig().getBoolean("hostWorldBlackList");
            Utils.hostWorlds = getConfig().getStringList("hostWorlds");
        }

	@Override
	public void onEnable() {
            instance = this;
            saveDefaultConfig();
            reloadConfig();
            loadMessages();
            loadPortableItems();
            loadWorlds();
            setLockPlugin();
            enableCommands();
            enableListeners();
            addCraftingRecipes();
	}

	@Override
	public void onDisable() {
	}
}
