package fr.limpsword.ligames;

import fr.limpsword.ligames.scoreboard.PlayerBoard;
import fr.limpsword.ligames.scoreboard.lines.BlankLine;
import fr.limpsword.ligames.scoreboard.lines.ItemLine;
import fr.limpsword.ligames.scoreboard.lines.TextMultiLine;
import fr.limpsword.ligames.scoreboard.lines.TitleLine;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public abstract class GamePlayer {

    protected final Game<? extends GamePlayer> game;

    protected final PlayerBoard playerBoard;
    protected final UUID uuid;

    public GamePlayer(Game<? extends GamePlayer> game, @NotNull Player player) {
        Objects.requireNonNull(player);

        this.game = game;
        this.uuid = player.getUniqueId();

        playerBoard = new PlayerBoard(player);
        playerBoard.setTitle(game.getDisplayName());
    }

    public final boolean isOnline() {
        Player player = getPlayer();
        return player != null && player.isOnline();
    }

    public final OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public final Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void updateScoreboard() {
        GameStep gameStep = game.getGameStep();

        if (gameStep == GameStep.WAITING) {
            String goal = game.getGoal();
            String[] goalAsList = goal.split("\\|");

            playerBoard.addContent(new BlankLine());
            playerBoard.addContent(new TitleLine("§3Informations", NamedTextColor.DARK_AQUA));
            playerBoard.addContent(new ItemLine("Joueurs", game.getPlayers().size() + "/∞"));
            playerBoard.addContent(new ItemLine("Joueurs requis", String.valueOf(game.getMinToStart())));
            if (game.getBeginTimer() != null) {
                playerBoard.addContent(
                        new ItemLine("Démarre dans", (game.getBeginTimer().getTimeLeft() + 1) + " sec")
                );
            }

            playerBoard.addContent(new BlankLine());
            playerBoard.addContent(new TitleLine("§6But du jeu", NamedTextColor.GOLD));
            playerBoard.addContent(new TextMultiLine(goalAsList));
            playerBoard.update();
        }
    }

    public void logout() {

    }

    public void lose() {
        if (game.getGameStep() == GameStep.IN_GAME) {
            // create method in IGame instead
            game.removePlayer(this);
            game.spec(this);

            getPlayer().setGameMode(GameMode.SPECTATOR);
            getPlayer().sendMessage("Vous avez perdu");

            //TODO : team mode

            // check win
            if (game.getPlayers().size() == 1) {
                game.winner(game.getPlayers().get(0).uuid);
            }
        }
    }
}
