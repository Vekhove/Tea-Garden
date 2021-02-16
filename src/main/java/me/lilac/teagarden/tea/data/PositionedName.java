package me.lilac.teagarden.tea.data;

public class PositionedName {

    private String name;
    private int position;

    public PositionedName(String name, int position) {
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }
}
