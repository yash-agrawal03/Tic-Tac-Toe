package androidsamples.java.tictactoe;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class TwoPlayerGame {
    private int turn;
    private String firstPlayer;
    private String secondPlayer;
    private String gameStatus;
    private String gameResult;

    public TwoPlayerGame() {
        this.turn = 1;
        this.firstPlayer = "";
        this.secondPlayer = "";
        this.gameStatus = "0000000000000000";
        this.gameResult = "";
    }

    public TwoPlayerGame(String firstPlayer) {
        this.turn = 1;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = "";
        this.gameStatus = "0000000000000000";
        this.gameResult = "";
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getTurn() {
        return turn;
    }
    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setFirstPlayer(String firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public void setSecondPlayer(String secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public String getSecondPlayer() {
        return secondPlayer;
    }

    public void setGameResult(String gameResult) {
        this.gameResult = gameResult;
    }

    public String getGameResult() {
        return gameResult;
    }



}
