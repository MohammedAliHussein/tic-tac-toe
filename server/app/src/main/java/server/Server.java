package server;

import java.util.logging.Logger;

import io.javalin.Javalin;
import io.javalin.http.ConflictResponse;
import server.database.TicTacToeDb;

import com.mongodb.client.MongoDatabase;

import org.json.*;

public class Server 
{
    private Logger log = Logger.getLogger(Server.class.getName()); 

    private Javalin server;
    private MongoDatabase db;

    public Server()
    {
        this.server = Javalin.create();
        this.db = TicTacToeDb.initDbConnection("tic-tac-toe");
        this.registerHandlers(this.server);
    }
    
    public void run()
    {
        this.server.start(3000);
    }

    private void registerHandlers(Javalin server)
    {
        server.post("/create", context -> {
            JSONObject body = new JSONObject(context.body());
            
            String displayName = (body.get("DisplayName")).toString();
            String gameId = (body.get("GameID")).toString();
            String password = (body.get("Password")).toString();

            try 
            {    
                String gameUrl = TicTacToeDb.createGame(this.db, gameId, password, displayName);
                //start new ws listener at game url
                context.result((new JSONObject().accumulate("connection_url", gameUrl)).toString());
            } 
            catch (Exception e) 
            {
                throw new ConflictResponse(e.getMessage());
            }
        });

        server.post("/join", context -> {
            JSONObject body = new JSONObject(context.body());
            
            String displayName = (body.get("DisplayName")).toString();
            String gameId = (body.get("GameID")).toString();
            String password = (body.get("Password")).toString();

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
