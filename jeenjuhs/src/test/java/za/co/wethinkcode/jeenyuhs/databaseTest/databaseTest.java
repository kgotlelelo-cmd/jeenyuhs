package za.co.wethinkcode.jeenyuhs.databaseTest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.jeenyuhs.database.Repository;
import za.co.wethinkcode.jeenyuhs.database.Service;
import za.co.wethinkcode.jeenyuhs.database.models.Client;
import za.co.wethinkcode.jeenyuhs.database.models.Post;
import za.co.wethinkcode.jeenyuhs.database.models.Team;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class databaseTest {

    @AfterAll
    public static void tearDown(){
        Service.dropDatabase("test.db");
    }

    @Test
    public void createUser(){
        Client client = new Client("kgotlelelo@gmail.com");
        Repository.TEST.getQuery().CreateClient(client);
        assertTrue(Repository.TEST.userExist(client.email));
        assertEquals(Repository.TEST.getQuery().client(client.email),client);
    }

    @Test
    public void createPost(){
        Post post = new Post(
                "Hello",
                0,
                "timeline",
                "123@gmail.com"
        );

        Post post1 = new Post(
                "Hello",
                0,
                "timeline",
                "1234@gmail.com"
        );

        Post post2 = new Post(
                "Hello",
                0,
                "timeline",
                "12345@gmail.com"
        );

        Repository.TEST.getQuery().createPost(post);
        Repository.TEST.getQuery().createPost(post1);
        Repository.TEST.getQuery().createPost(post2);


        assertEquals(Repository.TEST.getQuery().postByPerson("123@gmail.com").get(0),post);
        assertEquals(Repository.TEST.getQuery().postByPerson("1234@gmail.com").get(0),post1);
        assertEquals(Repository.TEST.getQuery().postByPerson("12345@gmail.com").get(0),post2);

        assertEquals(Repository.TEST.getQuery().postByTeam("timeline").get(0),post);
        assertEquals(Repository.TEST.getQuery().postByTeam("timeline").get(1),post1);
        assertEquals(Repository.TEST.getQuery().postByTeam("timeline").get(2),post2);

    }

    @Test
    public void createTeam(){
        Team team = new Team("general");
        Repository.TEST.getQuery().createTeam(team);
        assertTrue(Repository.TEST.teamExist("general"));
        assertEquals(Repository.TEST.getQuery().teamByName("general"),team);
    }

    @Test
    public void JoinTeam(){

        Team team1 = new Team("team1");
        Team team2 = new Team("team2");
        Repository.TEST.getQuery().createTeam(team1);
        Repository.TEST.getQuery().createTeam(team2);

        Client client1 = new Client("1@gmail.com");
        Client client2 = new Client("2@gmail.com");
        Client client3 = new Client("3@gmail.com");
        Client client4 = new Client("4@gmail.com");
        Repository.TEST.getQuery().CreateClient(client1);
        Repository.TEST.getQuery().CreateClient(client2);
        Repository.TEST.getQuery().CreateClient(client3);
        Repository.TEST.getQuery().CreateClient(client4);

        Repository.TEST.userJoinTeam("1@gmail.com","team1");
        Repository.TEST.userJoinTeam("2@gmail.com","team1");
        Repository.TEST.userJoinTeam("3@gmail.com","team2");
        Repository.TEST.userJoinTeam("4@gmail.com","team2");

        List<Client> clientList1 = Repository.TEST.teamMembers("team1");
        List<Client> clientList2 = Repository.TEST.teamMembers("team2");

        assertEquals(clientList1.get(0),client1);
        assertEquals(clientList1.get(1),client2);
        assertEquals(clientList2.get(0),client3);
        assertEquals(clientList2.get(1),client4);
    }

    @Test
    public void updateLikes(){
        Post post = new Post(
                "Hello",
                0,
                "hello",
                "peter@gmail.com"
        );
        Repository.TEST.getQuery().createPost(post);

        Post newPost = Repository.TEST.getQuery().postByPerson("peter@gmail.com").get(0);
        newPost.likes = 1;
        Repository.TEST.getQuery().updateLikes(newPost);
        assertEquals(1,Repository.TEST.getQuery().postByPerson("peter@gmail.com").get(0).likes);
    }
}
