package fr.limpsword.ligames.scoreboard.lines;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;

public class TitleLine implements ILineContent {

    private String title;

    private TextColor color;

    public TitleLine(String title, TextColor color) {
        this.title = title;
        this.color = color;
    }

    @Deprecated
    public TitleLine(String title, ChatColor chatColor) {
        this.title = title;
        TextColor color = NamedTextColor.BLACK;
        switch (chatColor)  {
            case RED -> color = NamedTextColor.RED;
            case DARK_BLUE -> color = NamedTextColor.DARK_BLUE;
            case DARK_GREEN -> color = NamedTextColor.DARK_GREEN;
            case DARK_AQUA -> color = NamedTextColor.DARK_AQUA;
            case DARK_RED -> color = NamedTextColor.DARK_RED;
            case DARK_PURPLE -> color = NamedTextColor.DARK_PURPLE;
            case GOLD -> color = NamedTextColor.GOLD;
            case GRAY -> color = NamedTextColor.GRAY;
            case DARK_GRAY -> color = NamedTextColor.DARK_GRAY;
            case BLUE-> color = NamedTextColor.BLUE;
            case GREEN -> color = NamedTextColor.GREEN;
            case AQUA -> color = NamedTextColor.AQUA;
            case LIGHT_PURPLE -> color = NamedTextColor.LIGHT_PURPLE;
            case YELLOW -> color = NamedTextColor.YELLOW;
            case WHITE -> color = NamedTextColor.WHITE;
        }
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TextColor getColor() {
        return color;
    }

    public void setColor(TextColor color) {
        this.color = color;
    }
}
