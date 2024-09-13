package fr.limpsword.ligames.scoreboard.lines;

public class TextMultiLine implements ILineContent {

    private String[] lines;

    public TextMultiLine(String[] lines) {
        this.lines = lines;
    }

    public String[] getLines() {
        return lines;
    }

    public void setLines(String[] lines) {
        this.lines = lines;
    }

}
