package server.game;

import java.util.function.Consumer;

import io.javalin.Javalin;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.websocket.WsConfig;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsContext;
import io.javalin.websocket.WsMessageContext;

import org.json.JSONObject;

public class Game extends Thread
{
    private Javalin server;
    private String gameUrl;
    private WsContext[] players;
    private int connected;

    public Game(Javalin server, String gameUrl)
    {
        this.server = server;
        this.gameUrl = gameUrl;
        this.players = new WsContext[2];
        this.connected = 0;
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
        });
    }

    private void waitForPlayers()
    {
        while(this.connected != 2)
        {
            for(WsContext player : this.players)
            {
                if(player != null)
                {
                    player.send(getWaitingMessage());
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
            this.players[this.connected] = connection;
            this.connected++;
        }
        else
        {
            throw new UnauthorizedResponse("Game already has 2 players.");
        }
    }

    private void handleNewMessage(WsMessageContext message)
    {

    }

    private void deleteGame()
    {

    }

    private void signalCountdown()
    {
        for(WsContext player : this.players)
        {
            if(player != null)
            {
                player.send(getWaitingMessage());
            }
        }
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

    private String getWaitingMessage()
    {
        JSONObject message = new JSONObject();

        message.accumulate("waiting", this.connected == 2);
        message.accumulate("connected", this.connected);

        return message.toString();
    }
}
