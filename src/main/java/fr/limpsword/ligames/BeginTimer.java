package fr.limpsword.ligames;

import fr.limpsword.ligames.chat.ChatManager;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BeginTimer extends BukkitRunnable {

    private final Game<?> game;
    @Getter
    private int timeLeft = 30;
    @Getter
    private boolean running;

    public BeginTimer(Game<?> game) {
        this.game = game;
    }

    public void begin(Plugin plugin) {
        this.timeLeft = Game.BEGIN_TIME;
        this.runTaskTimer(plugin, 1L, 20L);
    }

    public void reset() {
        ChatManager.sendGlobalMessage("no_more_player");
        this.running = false;
        this.timeLeft = Game.BEGIN_TIME;
        this.cancel();

        for (Player player : game.getOnlinePlayers()) {
            player.setLevel(Game.BEGIN_TIME);
        }
    }

    @Override
    public void run() {
        if (this.timeLeft < 0) {
            this.cancel();
            return;
        } else if (this.timeLeft == 0) {
            this.game.start();
        }

        this.running = true;

        for (Player p : game.getOnlinePlayers()) {
            p.setLevel(timeLeft);
        }
        sendSoundTimer(timeLeft);

        ChatColor c = switch (timeLeft) {
            case 30, 20 -> ChatColor.GREEN;
            case 10 -> ChatColor.GOLD;
            case 5, 4, 3, 2, 1 -> ChatColor.RED;
            default -> null;
        };

        if (c != null) {
            ChatManager.sendGlobalMessage("La partie démarre dans %s%s §esecondes", c, timeLeft);
        }

        timeLeft--;
    }

    public void sendSoundTimer(int timer) {
        //System.out.println(timer);
        float pitch = 1;
        switch (timer) {
            case 30, 20, 0:
                break;
            case 10:
                pitch = 2;
                break;
            case 5, 4, 3, 2, 1:
                pitch = 2.5F;
                break;
            default:
                return;
        }

        for (Player p : game.getOnlinePlayers()) {
            Sound s = Sound.BLOCK_NOTE_BLOCK_PLING;
            if (timer == 0) {
                s = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
            }
            p.playSound(p.getLocation(), s, 5F, pitch);
        }
    }
}