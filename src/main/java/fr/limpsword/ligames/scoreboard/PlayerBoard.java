package fr.limpsword.ligames.scoreboard;

import fr.limpsword.ligames.scoreboard.lines.BlankLine;
import fr.limpsword.ligames.scoreboard.lines.ILineContent;
import fr.limpsword.ligames.scoreboard.lines.ItemLine;
import fr.limpsword.ligames.scoreboard.lines.TextMultiLine;
import fr.limpsword.ligames.scoreboard.lines.TitleLine;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerBoard {

    @Getter
    private final Player player;
    private final List<ILineContent> lines;

    private final FastBoardBase<String> gameBoard;

    @Setter
    private String footer = ChatColor.GOLD + "   ▶ " + ChatColor.YELLOW + "play.mssclick.fr" + ChatColor.GOLD + " ◀";

    public PlayerBoard(Player player) {
        this.player = player;

        this.gameBoard = new FastBoard(player);
        lines = new ArrayList<>();
    }

    public void setTitle(String title) {
        this.gameBoard.updateTitle(ChatColor.RED + "" + ChatColor.BOLD + "✪ " + ChatColor.YELLOW + "" + ChatColor.BOLD + title + ChatColor.RED + "" + ChatColor.BOLD + " ✪");
    }

    public void setTitleRaw(String title) {
        this.gameBoard.updateTitle(title);
    }

    public void addContent(ILineContent content) {
        this.lines.add(content);
    }

    public void update() {

        int lineNumber = 0;

        if (!player.isOnline()) {
            return;
        }
        ArrayList<Integer> lineUsed = new ArrayList<>();

        for (ILineContent content : lines) {

            if (lineNumber >= 13) {
                Bukkit.getLogger().warning("Cannot add " + content.getClass() + " to scoreboard (maximum lines reached)");
            } else if (content instanceof BlankLine) {
                this.gameBoard.updateLine(lineNumber, "");
                lineUsed.add(lineNumber);
                lineNumber++;
            } else if (content instanceof ItemLine itemLine) {
                this.gameBoard.updateLine(lineNumber, "§7●§f " + itemLine.getKey() + " §7: §a" + itemLine.getValue());
                lineUsed.add(lineNumber);
                lineNumber++;
            } else if (content instanceof TitleLine titleLine) {
                this.gameBoard.updateLine(lineNumber, titleLine.getTitle());
                lineUsed.add(lineNumber);
                lineNumber++;
            } else if (content instanceof TextMultiLine textMultiLine) {
                for (String line : textMultiLine.getLines()) {
                    if (lineNumber >= 13) {
                        Bukkit.getLogger().warning("Cannot add " + content.getClass() + " to scoreboard (maximum lines reached)");
                    } else {
                        lineUsed.add(lineNumber);
                        this.gameBoard.updateLine(lineNumber, line);
                        lineNumber++;
                    }

                }
            }
        }

        this.gameBoard.updateLine(lineNumber, " ");
        lineUsed.add(lineNumber);
        lineNumber++;
        this.gameBoard.updateLine(lineNumber, footer);
        lineUsed.add(lineNumber);

        for (int i = 0; i < 15; i++) {
            if (!lineUsed.contains(i)) {
                this.gameBoard.removeLine(i);
            }

        }
        lines.clear();
    }
}