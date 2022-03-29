package za.co.wethinkcode.jeenyuhs.database.models;

import java.util.Objects;

public class Client {
    public int id;
    public String email;

    public Client() {
    }

    public Client(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(email, client.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
