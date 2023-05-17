package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a single match with the champion played and stats from that game
public class Match implements Writable {

    private Champion champion;
    private int kills;
    private int deaths;
    private int assists;
    private boolean wonGame;

    // REQUIRES: kills, deaths, and assists must be greater than or equal to 0
    // EFFECTS: creates a match with champion played, number of kills/deaths/assists and if you won or lost
    //          and adds it to the profiles match history
    public Match(Champion champion, int kills, int deaths, int assists, boolean wonGame) {
        this.champion = champion;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.wonGame = wonGame;

    }

    public Champion getChampionPlayed() {
        return champion;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getAssists() {
        return assists;
    }

    public boolean getWonStatus() {
        return wonGame;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("champion", champion.getChampionName());
        json.put("kills", kills);
        json.put("deaths", deaths);
        json.put("assists", assists);
        json.put("game won?", wonGame);
        return json;
    }
}
