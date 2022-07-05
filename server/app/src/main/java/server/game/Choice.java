package server.game;

public class Choice 
{
    private char icon;
    private int cell;
    
    public Choice(char icon, int cell)
    {
        this.icon = icon;
        this.cell = cell;
    }

    public char getIcon()
    {
        return this.icon;
    }

    public int getCell()
    {
        return this.cell;
    }

    public void setIcon(char icon)
    {
        this.icon = icon;
    }

    public void setCell(int cell)
    {
        this.cell = cell;
    }
}
