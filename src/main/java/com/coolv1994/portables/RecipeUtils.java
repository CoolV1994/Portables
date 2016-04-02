package com.coolv1994.portables;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.logging.Level;

/**
 * Created by Vinnie on 8/5/2015.
 */
public class RecipeUtils {
	private final Plugin plugin;

	public RecipeUtils(Plugin plugin) {
		this.plugin = plugin;
	}

	private String removeDuplicates(String s) {
		s = s.replaceAll(",", "");
		StringBuilder noDupes = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			String si = s.substring(i, i + 1);
			if (noDupes.indexOf(si) == -1) {
				noDupes.append(si);
			}
		}
		return noDupes.toString();
	}


	private Material getMaterial(char ingredient) {
		String name = plugin.getConfig().getString("recipeDefinitions." + ingredient);
		plugin.getLogger().log(Level.INFO, "Material [{0}] = {1}",
                        new Object[]{ingredient, name});
                if (name.startsWith("ID:")) {
                    try {
                        int id = Integer.parseInt(name.substring(3));
                        return Material.getMaterial(id);
                    } catch (Exception e) {
                        plugin.getLogger().log(Level.SEVERE, "Item ID Error", e);
                    }
                    return null;
                }
		return Material.matchMaterial(name);
	}

	private ShapedRecipe getRecipe(ItemStack item) {
		ShapedRecipe recipe = new ShapedRecipe(item);

		String recipeStr = plugin.getConfig().getString("portables." + item.getType() + ".Recipe");
		plugin.getLogger().log(Level.INFO, "Recipe = {0}", recipeStr);
		String[] parts = recipeStr.split(",");
		if (parts.length != 3)
			return null;

		recipe.shape(parts[0], parts[1], parts[2]);
		for (char ingredient : removeDuplicates(recipeStr).toCharArray()) {
			recipe.setIngredient(ingredient, getMaterial(ingredient));
		}
		return recipe;
	}

	private void setLore(ItemStack item) {
		ItemMeta metaData = item.getItemMeta();
		metaData.setDisplayName(
				ChatColor.translateAlternateColorCodes('&',
                                        plugin.getConfig().getString("portables." + item.getType() + ".CraftedName")));
		metaData.setLore(Arrays.asList(
				ChatColor.translateAlternateColorCodes('&',
                                        plugin.getConfig().getString("portables." + item.getType() + ".Lore"))
						.split("\\{N\\}")));
		item.setItemMeta(metaData);
	}

	private void setEnchantments(ItemStack item) {
		if (plugin.getConfig().getString("portables." + item.getType() + ".Enchantments") == null)
			return;

		ItemMeta metaData = item.getItemMeta();
		String[] enchantments = plugin.getConfig().getString("portables." + item.getType() + ".Enchantments").split(";");
		for (String enchantment : enchantments) {
			String[] enchant = enchantment.split(":");
			if (enchant.length == 2)
				metaData.addEnchant(Enchantment.getByName(enchant[0]), Integer.parseInt(enchant[1]), true);
			else
				metaData.addEnchant(Enchantment.getByName(enchant[0]), 1, true);
		}
		item.setItemMeta(metaData);
	}

	public void addRecipe(Material material) {
		if (plugin.getConfig().getBoolean("portables." + material + ".Craftable")) {
			ItemStack item = new ItemStack(material, 1);
			setLore(item);
			setEnchantments(item);
			plugin.getServer().addRecipe(getRecipe(item));
		}
	}
}
