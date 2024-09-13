package fr.limpsword.ligames.listeners;

import fr.limpsword.ligames.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameListener implements Listener {

    private final Game<?> game;

    public GameListener(Game<?> game) {
        this.game = game;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(null);

        game.playerLogin(game.getPlugin(), event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        event.quitMessage(null);

        game.playerLogout(event.getPlayer());
    }
}