package fr.limpsword.ligames.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class InventorySaver {

    private static final Map<Player, Map<Integer, ItemStack>> playerInventories = new HashMap<>();

    public static void saveInventory(Player player) {
        if (hasInventory(player)) return; // in case we switch inventories for example
        Map<Integer, ItemStack> inventory = new HashMap<>();
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            inventory.put(i, player.getInventory().getItem(i));
            player.getInventory().setItem(i, null);
        }
        playerInventories.put(player, inventory);
    }

    public static boolean hasInventory(Player player) {
        return playerInventories.containsKey(player);
    }

    public static void recoverInventory(Player player) {
        if (hasInventory(player)) {
            Map<Integer, ItemStack> inventory = playerInventories.get(player);
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                player.getInventory().setItem(i, inventory.get(i));
            }
            playerInventories.remove(player);
        }
    }
}