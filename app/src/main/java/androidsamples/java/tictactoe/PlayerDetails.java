package androidsamples.java.tictactoe;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.UUID;

@IgnoreExtraProperties
public class PlayerDetails {
    private String username;
    private int wins;
    private int losses;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public PlayerDetails() {
        this.username = "";
        this.wins = 0;
        this.losses = 0;
    }
    public PlayerDetails(String username) {
        this.username = username;
        this.wins = 0;
        this.losses = 0;
    }

    public PlayerDetails(String username, int wins, int losses) {
        this.username = username;
        this.wins = wins;
        this.losses = losses;
    }
    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getWins() {
        return wins;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getLosses() {
        return losses;
    }


}
