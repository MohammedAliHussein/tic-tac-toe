package server.game;

import java.util.HashMap;
import java.util.Map;

import io.javalin.websocket.WsContext;

public class Player 
{
    private Map<Integer, Integer> moves;
    private WsContext connection;
    private boolean turn;
    private char icon;
    private String name;

    public Player(WsContext connection, char icon, String name)
    {
        this.moves = new HashMap<Integer, Integer>();
        this.connection = connection;
        this.turn = false;
        this.icon = icon;
        this.name = name;
    }    

    public boolean hasWon()
    {
        return false;
    }

    public void addMove(int cell)
    {
        this.moves.put(cell, cell);
    }

    public Map<Integer, Integer> getMoves()
    {
        return this.moves;
    }

    public WsContext getConnection()
    {
        return this.connection;
    }

    public boolean getTurn()
    {
        return this.turn;
    }

    public char getIcon()
    {
        return this.icon;
    }

    public String getName()
    {
        return this.name;
    }

    public void setMoves(Map<Integer, Integer> moves)
    {
        this.moves = moves;
    }

    public void setConnection(WsContext connection)
    {
        this.connection = connection;
    }

    public void setTurn(boolean turn)
    {
        this.turn = turn;
    }

    public void setIcon(char icon)
    {
        this.icon = icon;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
