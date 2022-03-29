package za.co.wethinkcode.jeenyuhs.database.models;

import java.util.Objects;

public class UserJoinGroup {
    public int clientId;
    public int teamId;

    public UserJoinGroup() {
    }

    public UserJoinGroup(int clientId, int teamId) {
        this.clientId = clientId;
        this.teamId = teamId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserJoinGroup that = (UserJoinGroup) o;
        return clientId == that.clientId && teamId == that.teamId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, teamId);
    }
}
