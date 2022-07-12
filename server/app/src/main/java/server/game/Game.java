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

import server.database.TicTacToeDb;

import org.json.JSONObject;

import com.mongodb.client.MongoDatabase;

import org.bson.*;

public class Game extends Thread
{
    Logger log = Logger.getLogger(Game.class.getName());

    private Javalin server;
    private MongoDatabase db;
    private String gameUrl;
    private Player[] players;
    private int connected;
    private char[] state;
    private boolean receivedChoice;
    private Choice currentChoice;
    private boolean hasEnded;
    private int totalMoves;

    public Game(Javalin server, String gameUrl, MongoDatabase db)
    {
        this.server = server;
        this.db = db;
        this.gameUrl = gameUrl;
        this.players = new Player[2];
        this.connected = 0;
        this.state = new char[9];
        this.receivedChoice = false;
        this.currentChoice = null;
        this.hasEnded = false;
        this.totalMoves = 0;
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
            currentMove = chooseNextMove(isFirstChoice, currentMove);
            isFirstChoice = false;
            signalPlayers(currentMove);
            waitForChoice();
            updateGameState(currentMove);
            sendNewMoveToPlayers(currentMove);
            gameEnded(currentMove);
        }
    }

    private void deleteGame()
    {
        TicTacToeDb.deleteGame(this.db, gameUrl);
    }

    private int chooseNextMove(boolean isFirstChoice, int currentMove)
    {
        if(isFirstChoice) 
        {
            return Math.random() < 0.5 ? 1 : 0;
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
            sleep();
        }

        this.receivedChoice = false;
    }

    private void updateGameState(int currentMove)
    {
        char icon = this.players[currentMove].getIcon();
        int cell = this.currentChoice.getCell();

        this.state[cell] = icon;
        this.players[currentMove].addMove(cell);
    }

    private void gameEnded(int currentMove)
    {
        playerHasWon(currentMove);
        playersHaveTied(currentMove);
    }

    private boolean playerHasWon(int currentMove)
    {
        if(this.players[currentMove].hasWon()) 
        {
            this.hasEnded = true;
            signalPlayerWin(this.players[currentMove]);
            return true;
        }

        return false;
    }

    private void playersHaveTied(int currentMove)
    {
        if(this.totalMoves == 9 && !playerHasWon(currentMove))
        {
            this.hasEnded = true;
            signalGameTie();
        }
    }

    private void sendNewMoveToPlayers(int currentMove)
    {
        for(Player player : this.players)
        {
            player.getConnection().send(getNewMoveMessage(currentMove));
        }
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
                if(player != null && !player.getSentInitialId())
                {
                    player.getConnection().send(getWaitingMessage(player));
                    player.setSentInitialId(true);
                }
            }

            sleep();
        }

        signalStarting();
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
        JSONObject _message = new JSONObject(message.message());

        String type = _message.get("type").toString();

        if(type != null && type.equals("new_move"))
        {
            int cell = Integer.parseInt(_message.get("cell").toString());
            this.currentChoice = new Choice(cell);
            this.receivedChoice = true;
            this.totalMoves++;
        }
    }

    private void handleClosedConnection(WsCloseContext close)
    {
        
    }

    private void signalStarting()
    {
        sleep();

        for(Player player : this.players)
        {
            if(player != null)
            {
                player.getConnection().send(getStartingMessage(player));
            }
        }
    }

    private void signalGameTie()
    {
        for (Player player : this.players) 
        {
            player.getConnection().send(getTieMessage());
        }
    }

    private void signalPlayerWin(Player _player)
    {
        for (Player player : this.players) 
        {
            player.getConnection().send(getWinMessage(_player));
        }
    }

    private char determineIcon()
    {
        return this.connected == 0 ? 'X' : 'O';
    }

    private void sleep()
    {
        try 
        {
            Thread.sleep(1000);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
    }

    private String getWaitingMessage(Player player)
    {
        JSONObject message = new JSONObject();

        message.accumulate("type", "waiting");

        message.accumulate("icon", player.getIcon());

        return message.toString();
    }

    private String getStartingMessage(Player player)
    {
        JSONObject message = new JSONObject();

        message.accumulate("type", "starting");

        message.accumulate("icon", player.getIcon());

        return message.toString();
    }

    private String getTurnMessage(int currentMove)
    {
        JSONObject message = new JSONObject();

        message.accumulate("type", "turn");

        message.accumulate("player", this.players[currentMove].getName());

        return message.toString();
    }

    private String getNewMoveMessage(int currentMove)
    {
        JSONObject message = new JSONObject();

        message.accumulate("type", "new_move");

        message.accumulate("icon", this.players[currentMove].getIcon());

        message.accumulate("cell", this.currentChoice.getCell());

        return message.toString();
    }

    private String getTieMessage()
    {
        JSONObject message = new JSONObject();

        message.accumulate("type", "tie");

        return message.toString();
    }

    private String getWinMessage(Player player)
    {
        JSONObject message = new JSONObject();

        message.accumulate("type", "win");

        message.accumulate("player", player.getName());

        message.accumulate("win_sequence", player.getWinSequence());

        return message.toString();        
    }
}
