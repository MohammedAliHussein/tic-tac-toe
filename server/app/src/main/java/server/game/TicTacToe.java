package server.game;

import java.util.ArrayList;
import java.util.List;

import io.javalin.websocket.WsConfig;
import io.javalin.websocket.WsContext;

import org.json.JSONObject;

public class TicTacToe 
{
    private WsConfig socket;
    private WsContext[] connections;
    private int connectionCount;

    public TicTacToe(WsConfig socket)
    {
        this.socket = socket;
        this.connectionCount = 0;
        this.connections = new WsContext[2];
    }    

    public void start()
    {
        listenForPlayers();
        waitForPlayers();
        // startGameLoop();
        // restartGame();
    }

    private void listenForPlayers()
    {
        listenForConnections();
    }

    private void listenForConnections() 
    {
        this.socket.onConnect(context -> {
            if(this.connectionCount <= 1)
            {
                this.connections[this.connectionCount] = context;
                this.connectionCount++;
            }
        });
    }

    private void waitForPlayers()
    {
        while (this.connectionCount != 2)
        {
            for (WsContext wsContext : this.connections) 
            {
                if(wsContext != null && !wsContext.equals(null))    
                {
                    wsContext.send(this.getWaitingMessage());
                }
            }
        }
    }

    private void startGameLoop()
    {

    }

    private String getWaitingMessage()
    {
        JSONObject message = new JSONObject();

        message.accumulate("waiting", this.connectionCount == 2);
        message.accumulate("connected", this.connectionCount);

        return message.toString();
    }
}
