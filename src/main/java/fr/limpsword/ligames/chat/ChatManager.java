package fr.limpsword.ligames.chat;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatManager {

    @Setter
    @Getter
    public static String prefix = "§7[§bLiGames§7] ";

    public static void sendGlobalMessage(String message, Object... args) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(prefix + String.format(message, args));
        }
    }

    public static void sendGlobalMessage(Component component) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(prefix + component);
        }
    }
}
