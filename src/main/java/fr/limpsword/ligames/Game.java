package fr.limpsword.ligames;

import fr.limpsword.ligames.chat.ChatManager;
import fr.limpsword.ligames.listeners.GameListener;
import fr.limpsword.ligames.timer.Timer;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public abstract class Game<GP extends GamePlayer> {

    public static int BEGIN_TIME = 30;

    protected final Plugin plugin;

    protected final String codeName;
    protected final String displayName;
    protected final String description;
    protected final String goal;

    protected final Class<GP> gpClass;

    protected final List<UUID> winners = new ArrayList<>();
    @Getter
    protected final List<UUID> staffs = new ArrayList<>();
    protected final HashMap<UUID, GP> players = new HashMap<>();
    @Getter
    protected final HashMap<UUID, GP> spectators = new HashMap<>();

    protected boolean allowReconnect = false;
    protected GameStep gameStep = GameStep.WAITING;
    protected long startTime = -1;
    protected int minToStart = 2;

    private BeginTimer beginTimer;

    public Game(Plugin plugin, String codeName, String displayName, String description, String goal, Class<GP> gpClass) {
        this.plugin = plugin;
        this.codeName = codeName;
        this.displayName = displayName;
        this.description = description;
        this.goal = goal;
        this.gpClass = gpClass;

        Bukkit.getPluginManager().registerEvents(new GameListener(this), plugin);

        Timer.repeated(plugin, "game-" + codeName, 5, 10, () -> {
            for (GP value : players.values()) {
                value.updateScoreboard();
            }
        });
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.gameStep = GameStep.IN_GAME;
    }

    public void playerLogin(Plugin plugin, Player player) {
        try {
            GP GP = this.gpClass.getConstructor(Game.class, Player.class).newInstance(this, player);
            this.players.put(player.getUniqueId(), GP);

            player.showTitle(Title.title(Component.text("§b" + displayName), Component.text("§3" + description)));

            if (gameStep == GameStep.WAITING) {
                ChatManager.sendGlobalMessage(player.displayName().color(NamedTextColor.GRAY).append(Component.space()).append(Component.text("§7a rejoint le jeu ! " + "§c(" + this.players.size() + "/∞)")));
            }

            if (beginTimer == null && this.players.size() >= minToStart && (gameStep == GameStep.WAITING)) {
                this.beginTimer = new BeginTimer(this);
                this.beginTimer.begin(plugin);
            }
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void spec(GamePlayer player) {
        spectators.put(player.uuid, (GP) player);
    }

    public void removePlayer(GamePlayer player) {
        players.remove(player.uuid);
    }

    private void checkSoloPlayer() {
        if (this.players.size() == 1) {
            winner(this.getOnlinePlayers().get(0).getUniqueId());
        }
    }

    public void playerLogout(Player p) {
        if (gameStep == GameStep.FINISH) {
            return;
        }

        if (this.players.containsKey(p.getUniqueId())) {
            //Player not spec
            if (!this.spectators.containsKey(p.getUniqueId())) {
                switch (gameStep) {
                    case WAITING -> {
                        players.remove(p.getUniqueId());
                        ChatManager.sendGlobalMessage("logout_waiting", p.getDisplayName());
                    }
                    case PREPARING, IN_GAME -> {
                        players.get(p.getUniqueId()).logout();
                        players.remove(p.getUniqueId());
                        ChatManager.sendGlobalMessage("logout_eliminated", p.getDisplayName());
                        checkSoloPlayer();
                    }
                    case FINISH -> {

                    }
                }
                //TODO : add max reco time
                //IElyBukkitAPI.sendGlobalMessage("logout_can_reconnect", p.getDisplayName());


            } else staffs.remove(p.getUniqueId());
        }

        if (beginTimer != null && this.players.size() < minToStart && (gameStep == GameStep.WAITING)) {
            beginTimer.reset();
            this.beginTimer = null;
        }

    }

    public void winner(UUID winner) {
        gameFinish();
    }

    public void gameFinish() {
        this.gameStep = GameStep.FINISH;

        Timer.later(plugin, "end", 20L * 9, () -> {
            Timer.cancel("win");

            for (GP player : players.values()) {
                player.getPlayer().kick(Component.text("§cLa partie est terminée !"));
            }
        });

        Timer.later(plugin, "shutdown", 20L * 10, Bukkit::shutdown);
    }

    public GP getPlayer(UUID uuid) {
        return this.players.getOrDefault(uuid, null);
    }

    public GP getPlayer(Player player) {
        return this.players.getOrDefault(player.getUniqueId(), null);
    }

    public List<Player> getOnlinePlayers() {
        return this.players.values().stream().map(GamePlayer::getPlayer).filter(Objects::nonNull).toList();
    }

    public List<GP> getPlayers() {
        return this.players.values().stream().toList();
    }

    public boolean hasPlayer(Player p) {
        return this.players.containsKey(p.getUniqueId());
    }

    public boolean isStarted() {
        return this.gameStep == GameStep.IN_GAME || this.gameStep == GameStep.STARTING || this.gameStep == GameStep.FINISH || this.gameStep == GameStep.PREPARING;
    }
}
