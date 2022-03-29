package za.co.wethinkcode.jeenyuhs.database;

import za.co.wethinkcode.jeenyuhs.database.models.Client;
import za.co.wethinkcode.jeenyuhs.database.models.Team;

import java.util.List;

public interface Repository {

    String url = "jdbc:sqlite:test.db";
    Repository INSTANCE = new Service();
    Repository TEST = new Service(Repository.url);

    public SqliteDB getQuery();

    public boolean teamExist(String team);

    public boolean userExist(String email);

    public void userJoinTeam(String email, String teamName);

    public List<Client> teamMembers(String teamName);
}
