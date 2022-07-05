package server.game;

import java.util.function.Consumer;
import java.util.logging.Logger;

import io.javalin.Javalin;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConfig;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsContext;
import io.javalin.websocket.WsMessageContext;

import org.json.JSONObject;

public class Game extends Thread
{
    Logger log = Logger.getLogger(Game.class.getName());

    private Javalin server;
    private String gameUrl;
    private Player[] players;
    private int connected;
    private char[][] state;

    public Game(Javalin server, String gameUrl)
    {
        this.server = server;
        this.gameUrl = gameUrl;
        this.players = new Player[2];
        this.connected = 0;
        this.state = new char[3][3];
    }

    @Override
    public void run()
    {
        listenForPlayers();
        waitForPlayers();
        gameLoop();
        deleteGame();
    }   

    private void gameLoop()
    {
        // while(!hasEnded())
        // {
        //     //if first iteration, randomly choose first
        //     //wait for choice
        //     //update game state
        //     //check for win
        //     //send game state to players
        //     //switch to next player if no win
        // }
    }

    private void listenForPlayers()
    {
        this.server.ws(String.format("/%s", this.gameUrl), ws -> {
            ws.onConnect(connection -> {
                handleNewConnection(connection);
            });

            ws.onMessage(message -> {
                handleNewMessage(message);
            });

            ws.onClose(close -> {
                handleClosedConnection(close);
            });
        });
    }

    private void waitForPlayers()
    {
        while(this.connected != 2)
        {
            for(Player player : this.players)
            {
                if(player != null)
                {
                    player.getConnection().send(getWaitingMessage(player));
                }
            }

            sleep();
        }

        signalCountdown();
    }

    private void handleNewConnection(WsConnectContext connection)
    {
        if(this.connected <= 1)
        {
            this.players[this.connected] = new Player(connection, determineIcon(), connection.queryParam("name"));
            this.connected++;
        }
        else
        {
            connection.send(new UnauthorizedResponse("Game already has 2 players.").toString());
        }
    }

    private void handleNewMessage(WsMessageContext message)
    {

    }

    private void handleClosedConnection(WsCloseContext close)
    {

    }

    private void deleteGame()
    {

    }

    private void signalCountdown()
    {
        for(Player player : this.players)
        {
            if(player != null)
            {
                player.getConnection().send(getWaitingMessage(player));
            }
        }
    }

    private char determineIcon()
    {
        if(this.connected == 0)
        {
            return 'X';
        }

        return 'O';
    }

    private void sleep()
    {
        try 
        {
            Thread.sleep(800);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
    }

    private String getWaitingMessage(Player player)
    {
        JSONObject message = new JSONObject();

        message.accumulate("waiting", this.connected == 2);
        message.accumulate("connected", this.connected);
        message.accumulate("icon", player.getIcon());
        message.accumulate("turn", player.getTurn());

        return message.toString();
    }
}
