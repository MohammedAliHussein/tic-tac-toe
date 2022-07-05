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
    private char[] state;
    private boolean receivedChoice;
    private Choice currentChoice;
    private boolean hasEnded;

    public Game(Javalin server, String gameUrl)
    {
        this.server = server;
        this.gameUrl = gameUrl;
        this.players = new Player[2];
        this.connected = 0;
        this.state = new char[9];
        this.receivedChoice = false;
        this.currentChoice = null;
        this.hasEnded = false;
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
        boolean isFirstChoice = true;
        int currentMove = -1;

        while(!this.hasEnded)
        {
            if(!this.hasEnded)
            {
                currentMove = chooseNextMove(isFirstChoice, currentMove);
                signalPlayers(currentMove);
                waitForChoice();
                updateGameState(currentMove);
                boolean hasWon = playerHasWon(currentMove);
                sendNewMoveToPlayers(currentMove);
                if(!hasWon) currentMove = chooseNextMove(isFirstChoice, currentMove);
            }
        }
    }

    private int chooseNextMove(boolean isFirstChoice, int currentMove)
    {
        if(isFirstChoice) 
        {
            return Math.random() <= 0.5 ? 1 : 0;
        }

        return currentMove == 1 ? 0 : 1;
    }

    private void signalPlayers(int currentMove)
    {
        for(Player player : this.players)
        {
            player.getConnection().send(getTurnMessage(currentMove));
        }
    }

    private void waitForChoice()
    {
        while(!this.receivedChoice) 
        {
            System.out.println("still here");
            sleep();
        }

        this.receivedChoice = false;
    }

    private void updateGameState(int currentMove)
    {
        char icon = this.currentChoice.getIcon();
        int cell = this.currentChoice.getCell();

        this.state[cell] = icon;
        this.players[currentMove].addMove(cell);
    }

    private boolean playerHasWon(int currentMove)
    {
        if(this.players[currentMove].hasWon()) 
        {
            this.hasEnded = true;
            return true;
        }

        return false;
    }

    private void sendNewMoveToPlayers(int currentMove)
    {
        for(Player player : this.players)
        {
            player.getConnection().send(getNewMoveMessage(currentMove));
        }
    }

    private void listenForPlayers() //listen on new thread incase waiting for player move so dont block
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
        this.currentChoice = new Choice('X', 0);
        this.receivedChoice = true;
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

        message.accumulate("waiting", !(this.connected == 2));
        message.accumulate("connected", this.connected);
        message.accumulate("icon", player.getIcon());
        message.accumulate("turn", player.getName());

        return message.toString();
    }

    private String getTurnMessage(int currentMove)
    {
        JSONObject message = new JSONObject();

        message.accumulate("turn", this.players[currentMove].getName());
        message.accumulate("icon", "");
        message.accumulate("cell", "");
        message.accumulate("ended", this.hasEnded);

        return message.toString();
    }

    private String getNewMoveMessage(int currentMove)
    {
        JSONObject message = new JSONObject();

        message.accumulate("turn", this.players[currentMove].getName());
        message.accumulate("icon", this.currentChoice.getIcon());
        message.accumulate("cell", this.currentChoice.getCell());
        message.accumulate("ended", this.hasEnded);

        return message.toString();
    }
}
