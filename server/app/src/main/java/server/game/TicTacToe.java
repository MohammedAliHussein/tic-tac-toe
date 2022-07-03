package server.game;

import java.util.ArrayList;
import java.util.List;

import io.javalin.websocket.WsConfig;
import io.javalin.websocket.WsContext;

public class TicTacToe 
{
    WsConfig socket;
    List<WsContext> connections;

    public TicTacToe(WsConfig socket)
    {
        this.socket = socket;
        this.connections = new ArrayList<WsContext>();
    }    

    public void start()
    {
        listenForPlayers();
        waitForPlayers();
        startGameLoop();
    }

    private void listenForPlayers()
    {
        if(this.connections.size() < 2) 
        {
            this.socket.onConnect(ctx -> {
                this.connections.add(ctx);
            });
        }
    }

    private void waitForPlayers()
    {
        while(this.connections.size() != 2)
        {
            for(WsContext ctx : this.connections)
            {
                //Signal still waiting
            }
        }
    }

    private void startGameLoop()
    {

    }
}
