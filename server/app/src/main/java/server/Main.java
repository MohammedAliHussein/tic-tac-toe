/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package server;

import server.Server;
import server.database.TicTacToeDb;

import com.mongodb.client.MongoDatabase;

public class Main 
{
    public static void main(String[] args) 
    {
        // MongoDatabase db = TicTacToeDb.initDbConnection("tic-tac-toe");

        // try
        // {
        //     TicTacToeDb.deleteAllGames(db);
        // }
        // catch (Exception e)
        // {
        //     System.out.println(String.format("\n\n%s\n", e.getMessage()));
        // }

        new Server().run();
    }
}
