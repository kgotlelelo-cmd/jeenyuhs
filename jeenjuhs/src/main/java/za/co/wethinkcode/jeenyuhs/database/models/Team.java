package za.co.wethinkcode.jeenyuhs.database.models;

import java.util.Objects;

public class Team {

    public int id;
    public String name;

    public Team() {
    }

    public Team(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(name, team.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
