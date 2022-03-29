package za.co.wethinkcode.jeenyuhs.database;

import net.lemnik.eodsql.BaseQuery;
import net.lemnik.eodsql.Select;
import net.lemnik.eodsql.Update;
import za.co.wethinkcode.jeenyuhs.database.models.Client;
import za.co.wethinkcode.jeenyuhs.database.models.Post;
import za.co.wethinkcode.jeenyuhs.database.models.Team;
import za.co.wethinkcode.jeenyuhs.database.models.UserJoinGroup;

import java.util.List;

public interface SqliteDB extends BaseQuery {

    @Update("INSERT INTO client (EMAIL) " +
            "VALUES (?{1.email});")
    public void CreateClient(Client client);

    @Update("INSERT INTO post (MESSAGE,LIKES,TEAM,PERSON) " +
            "VALUES (?{1.message},?{1.likes},?{1.team},?{1.person});")
    public void createPost(Post post);

    @Update("INSERT INTO team (NAME) " +
            "VALUES (?{1.name});")
    public void createTeam(Team team);

    @Update("INSERT INTO usergroup (CLIENTID,TEAMID) " +
            "VALUES (?{1.clientId},?{1.teamId});")
    public void joinTeam(UserJoinGroup userJoinGroup);

    @Update( "UPDATE post SET likes=?{1.likes} WHERE id=?{1.id}" )
    public int updateLikes( Post post );

    @Select("SELECT * FROM post WHERE person=?{1}")
    public List<Post> postByPerson(String email);

    @Select("SELECT * FROM post WHERE id=?{1}")
    public Post postById(int id);

    @Select("SELECT * FROM post WHERE team=?{1}")
    public List<Post> postByTeam(String team);

    @Select("SELECT * FROM team")
    public List<Team> teams();

    @Select("SELECT * FROM team WHERE name=?{1}")
    public Team teamByName(String name);

    @Select("SELECT * FROM client")
    public List<Client> clients();

    @Select("SELECT * FROM client WHERE email=?{1}")
    public Client client(String email);

    @Select("SELECT * FROM client WHERE id=?{1}")
    public Client clientById(int id);

    @Select("SELECT * FROM usergroup WHERE teamid=?{1}")
    public List<UserJoinGroup> combination(int id);

    @Select("SELECT * FROM usergroup")
    public List<UserJoinGroup> allCombination();
}
