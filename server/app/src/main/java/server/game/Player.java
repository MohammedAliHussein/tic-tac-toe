package server.game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.javalin.websocket.WsContext;

public class Player 
{
    private static final int[][] possibleWins = new int[][] {{0,1,2}, 
                                                            {3,4,5}, 
                                                            {6,7,8}, 
                                                            {0,3,6}, 
                                                            {1,4,7}, 
                                                            {2,5,8}, 
                                                            {0,4,8}, 
                                                            {2,4,6}};

    private Map<Integer, Integer> moves;
    private WsContext connection;
    private boolean turn;
    private char icon;
    private String name;
    private boolean sentInitialId;
    private boolean hasWon;

    public Player(WsContext connection, char icon, String name)
    {
        this.moves = new HashMap<Integer, Integer>();
        this.connection = connection;
        this.turn = false;
        this.icon = icon;
        this.name = name;
        this.sentInitialId = false;
        this.hasWon = false;
    }    

    /**
    0 | 1 | 2
    3 | 4 | 5
    6 | 7 | 8
    */

    public boolean hasWon()
    {
        for(int[] win : possibleWins)
        {
            int foundMoves = 0;

            for(int i = 0; i < 3; i++)
            {
                if(this.moves.get(win[i]) != null)
                {
                    foundMoves++;
                }
            }

            if(foundMoves == 3) 
            {
                this.hasWon = true;
                return true;
            }
            else
            {
                foundMoves = 0;
            }
        }

        return false;
    }

    public String getWinSequence()
    {
        if(this.hasWon)
        {
            for(int[] win : possibleWins)
            {
                int foundMoves = 0;

                for(int i = 0; i < 3; i++)
                {
                    if(this.moves.get(win[i]) != null)
                    {
                        foundMoves++;
                    }
                }

                if(foundMoves == 3) 
                {
                    return Arrays.toString(win);
                }
                else
                {
                    foundMoves = 0;
                }
            }
        }

        return null;
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

    public boolean getSentInitialId()
    {
        return this.sentInitialId;
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

    public void setSentInitialId(boolean sentInitialId)
    {
        this.sentInitialId = sentInitialId;
    }
}
