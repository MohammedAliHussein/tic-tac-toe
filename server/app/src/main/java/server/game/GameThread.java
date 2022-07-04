package server.game;

import io.javalin.Javalin;

public class GameThread extends Thread
{
    private Javalin server;
    private String gameUrl;

    public GameThread(Javalin server, String gameUrl)
    {
        this.server = server;
        this.gameUrl = gameUrl;
    }

    @Override
    public void run()
    {
        this.server.ws(String.format("/%s", this.gameUrl), ws -> {
            new TicTacToe(ws).start();
        });
    }    
}
