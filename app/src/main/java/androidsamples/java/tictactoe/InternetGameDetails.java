package androidsamples.java.tictactoe;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class InternetGameDetails {
    private String playername;
    private String playeruid;

    public InternetGameDetails() {
    }
    public InternetGameDetails(String playername, String playeruid) {
        this.playername = playername;
        this.playeruid = playeruid;
    }
    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }
    public String getPlayeruid() {
        return playeruid;
    }

    public void setPlayeruid(String playeruid) {
        this.playeruid = playeruid;
    }


}
