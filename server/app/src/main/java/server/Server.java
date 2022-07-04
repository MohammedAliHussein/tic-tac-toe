package server;

import java.util.logging.Logger;

import io.javalin.Javalin;
import io.javalin.http.ConflictResponse;
import server.database.TicTacToeDb;

import com.mongodb.client.MongoDatabase;

import org.json.*;

import server.game.GameThread;
import server.game.TicTacToe;

public class Server 
{
    private Logger log = Logger.getLogger(Server.class.getName()); 

    private Javalin server;
    private MongoDatabase db;

    public Server()
    {
        this.server = Javalin.create(config -> {
            config.enableCorsForAllOrigins();
        });
        this.db = TicTacToeDb.initDbConnection("tic-tac-toe");
        this.registerHandlers(this.server);
    }
    
    public void run()
    {
        this.server.start(3001);
    }

    private void registerHandlers(Javalin server)
    {
        server.post("/create", context -> {
            JSONObject body = new JSONObject(context.body());
            body = new JSONObject((body.get("data")).toString());

            System.out.println(body);
            
            String displayName = (body.get("display_name")).toString();
            String gameId = (body.get("game_id")).toString();
            String password = (body.get("password")).toString();

            try 
            {    
                String gameUrl = TicTacToeDb.createGame(this.db, gameId, password, displayName);
                new GameThread(server, gameUrl).start(); //start new ws listening on different thread
                context.result((new JSONObject().accumulate("connection_url", gameUrl)).toString());
            } 
            catch (Exception e) 
            {
                throw new ConflictResponse(e.getMessage());
            }
        });

        server.post("/join", context -> {
            JSONObject body = new JSONObject(context.body());
            body = new JSONObject((body.get("data")).toString());

            System.out.println(body);
            
            String displayName = (body.get("display_name")).toString();
            String gameId = (body.get("game_id")).toString();
            String password = (body.get("password")).toString();

            try 
            {
                String gameUrl = TicTacToeDb.joinGame(this.db, gameId, password, displayName);
                context.result((new JSONObject().accumulate("connection_url", gameUrl)).toString());
            } 
            catch (Exception e) 
            {
                throw new ConflictResponse(e.getMessage());                
            } 
        });
    }
}
