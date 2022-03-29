package za.co.wethinkcode.jeenyuhs.database;

import net.lemnik.eodsql.QueryTool;
import za.co.wethinkcode.jeenyuhs.database.models.Client;
import za.co.wethinkcode.jeenyuhs.database.models.Team;
import za.co.wethinkcode.jeenyuhs.database.models.UserJoinGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Service implements Repository{

    private String DB_URL="jdbc:sqlite:jeenjuhs.db";
    Connection connection;
    Statement statement;
    boolean isConnected;
    SqliteDB query;

    public Service() {
        try {
            DbConnect(DB_URL);
            createTables();
            connectQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //for testing
    public Service(String DB_URL) {
        try {
            DbConnect(DB_URL);
            createTables();
            connectQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void DbConnect(String url){
        try {
            this.connection = DriverManager.getConnection(url);
            this.statement =  this.connection.createStatement();
            isConnected = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTables() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS CLIENT " +
                "(ID INTEGER PRIMARY KEY     AUTOINCREMENT," +
                " EMAIL           TEXT    NOT NULL UNIQUE );";

        this.statement.addBatch(sql);

        sql = "CREATE TABLE IF NOT EXISTS POST " +
                "(ID INTEGER PRIMARY KEY     AUTOINCREMENT, " +
                "MESSAGE     TEXT     NOT NULL, " +
                "LIKES        INTEGER    NOT NULL, " +
                "TEAM TEXT, " +
                "PERSON TEXT ); ";

        this.statement.addBatch(sql);

        sql = "CREATE TABLE IF NOT EXISTS TEAM " +
                "(ID INTEGER PRIMARY KEY     AUTOINCREMENT," +
                " NAME           TEXT     NOT NULL );";

        this.statement.addBatch(sql);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                  CREATING JOINING TABLES                                              //
        //////////////////////////////////////////////////////////////////////////////////////////////////////////

        sql = "CREATE TABLE IF NOT EXISTS USERGROUP " +
                "(CLIENTID       INT     NOT NULL references CLIENT(ID) ON DELETE CASCADE, " +
                " TEAMID         INT     NOT NULL references TEAM(ID) ON DELETE CASCADE, " +
                "PRIMARY KEY(CLIENTID,TEAMID));" ;

        this.statement.addBatch(sql);

        this.statement.executeBatch();
    }

    public void connectQuery(){
        this.query = QueryTool.getQuery(this.connection,SqliteDB.class);
    }

    @Override
    public SqliteDB getQuery() {
        return query;
    }

    @Override
    public boolean teamExist(String team) {
        return Optional.ofNullable(this.query.teamByName(team)).isPresent();
    }

    @Override
    public boolean userExist(String email) {
        return Optional.ofNullable(this.query.client(email)).isPresent();
    }

    @Override
    public void userJoinTeam(String email, String teamName) {
        Client client = this.query.client(email);
        Team team = this.query.teamByName(teamName);
        UserJoinGroup userJoinGroup = new UserJoinGroup(client.id,team.id);
        this.query.joinTeam(userJoinGroup);
    }

    @Override
    public List<Client> teamMembers(String teamName) {
        Team team = this.query.teamByName(teamName);
        List<UserJoinGroup> userJoinGroupList = this.query.combination(team.id);
        return userJoinGroupList.stream().map(value-> this.query.clientById(value.clientId)).collect(Collectors.toList());
    }

    public static void dropDatabase(String database)  {

        boolean result;

        try {
            File file = new File(database);
            file.delete();
            System.out.println("Database Dropped");
        }catch (Exception e){
            System.out.println("ERROR dropping the database");

        }
    }
}
