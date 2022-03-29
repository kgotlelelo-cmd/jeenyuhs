package za.co.wethinkcode.jeenyuhs.database.models;

import java.util.Objects;

public class Post {
    public int id;
    public String message;
    public int likes;
    public String team;
    public String person;

    public Post() {
    }

    public Post(String message, int likes, String team, String person) {
        this.message = message;
        this.likes = likes;
        this.team = team;
        this.person = person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(message, post.message) && Objects.equals(team, post.team) && Objects.equals(person, post.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, team, person);
    }
}
