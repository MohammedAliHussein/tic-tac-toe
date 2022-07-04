package server.database;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.util.List;
import java.util.logging.Logger;
import java.util.Arrays;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import com.mongodb.client.*;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.google.common.base.CharMatcher;

public class TicTacToeDb 
{
    private static Logger log = Logger.getLogger(TicTacToeDb.class.getName()); 

    private static Dotenv dot = Dotenv.load();

    public static MongoDatabase initDbConnection(String dbName)
    {
        String connectionString = dot.get("MONGO_CONNECTION_STRING");
        String password = dot.get("MONGO_PASSWORD");
        String formattedConnectionString = connectionString.replaceAll("<password>", password);

        MongoClient client = MongoClients.create(formattedConnectionString); //creating the connection to have access to all dbs

        return getDatabase(dbName, client);
    }

    public static String createGame(MongoDatabase db, String gameName, String password, String displayName)
    {
        String gameUrl = null;
        MongoCollection<Document> collection = db.getCollection("Active Games");
        MongoCursor<Document> cursor = collection.find().iterator();

        if(gameNameIsNotUnique(cursor, gameName))
        {
            throw new IllegalArgumentException("A game with the same name is already in play.");
        }

        List<String> players = Arrays.asList(new String[] {displayName, ""});

        gameUrl = BCrypt.hashpw(gameName, BCrypt.gensalt());

        gameUrl = CharMatcher.anyOf("$./").removeFrom(gameUrl);

        Document newGame = new Document("name", gameName)
                            .append("password", password)
                            .append("players", players)
                            .append("gameUrl", gameUrl);

        gameUrl = writeNewGame(collection, newGame) ? gameUrl : null;

        return gameUrl;
    }

    public static String joinGame(MongoDatabase db, String gameName, String password, String displayName)
    {
        String gameUrl = null;
        MongoCollection<Document> collection = db.getCollection("Active Games");
        MongoCursor<Document> cursor = collection.find().iterator();

        Document game = getActiveGame(cursor, gameName);

        if(game.equals(null)) throw new IllegalArgumentException(String.format("No game with name %s found.", gameName));

        if(!game.get("password").equals(password)) throw new IllegalArgumentException("Invalid password.");

        List<String> players = (List<String>)game.get("players");

        for(String name : players)
        {
            if(name.equals(displayName)) throw new IllegalArgumentException(String.format("%s is already being used by opponent.", displayName));
        }

        players.remove("");

        players.add(displayName);

        collection.updateOne(eq("name", gameName), set("players", players));

        return (String)game.get("gameUrl");
    }

    public static void deleteAllGames(MongoDatabase db)
    {
        db.getCollection("Active Games").deleteMany(new Document());
    }

    private static Document getActiveGame(MongoCursor<Document> cursor, String gameName)
    {
        try 
        {
            while(cursor.hasNext())
            {
                Document doc = cursor.next();

                if(doc.get("name").equals(gameName))
                {
                    cursor.close();
                    return doc;
                }
            }
        } 
        catch (Exception e) 
        {
            log.warning(e.getMessage());
        }
        finally
        {
            cursor.close();
        }

        return null;
    }

    private static boolean writeNewGame(MongoCollection<Document> collection, Document newGame)
    {
        boolean inserted = true;

        try 
        {
            collection.insertOne(newGame);
        } 
        catch (Exception e) 
        {
            log.warning(e.getMessage());
            inserted = false;
        }

        return inserted;
    } 

    private static boolean gameNameIsNotUnique(MongoCursor<Document> cursor, String gameName)
    {
        try 
        {
            while(cursor.hasNext())
            {
                Document doc = cursor.next();

                if(doc.get("name").equals(gameName))
                {
                    cursor.close();
                    return true;
                }
            }
        } 
        catch (Exception e) 
        {
            log.warning(e.getMessage());
        }
        finally
        {
            cursor.close();
        }

        return false;
    }

    private static MongoDatabase getDatabase(String dbName, MongoClient client)
    {
        MongoDatabase db = null;

        try 
        {
            db = client.getDatabase(dbName);
        } 
        catch (IllegalArgumentException e) 
        {
            log.warning(e.getMessage());
        }

        return db;
    }
}
