package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents an individual champion with players associated kill, death, assists, games played and wins stats
public class Champion implements Writable {

    private String championName;
    private int totalKills;
    private int totalDeaths;
    private int totalAssists;
    private int totalGamesPlayed;
    private int totalWins;



    // EFFECTS: creates a champion with given name and 0 kills, deaths, assists, games played, and wins
    public Champion(String name) {
        championName = name;
        totalKills = 0;
        totalDeaths = 0;
        totalAssists = 0;
        totalGamesPlayed = 0;
        totalWins = 0;
    }

    // REQUIRES: kills must be greater than or equal to 0
    // MODIFIES: this Champion
    // EFFECTS: adds given amount of kills to total kills
    public void addKills(int kills) {
        totalKills += kills;
    }

    // REQUIRES: deaths must be greater than or equal to 0
    // MODIFIES: this Champion
    // EFFECTS: adds given amount of deaths to total deaths
    public void addDeaths(int deaths) {
        totalDeaths += deaths;
    }

    // REQUIRES: assists must be greater than or equal to 0
    // MODIFIES: this Champion
    // EFFECTS: adds given amount of assists to total assists
    public void addAssists(int assists) {
        totalAssists += assists;
    }

    // MODIFIES: this
    // EFFECTS: adds one to total games played
    public void addGamePlayed() {
        totalGamesPlayed += 1;
    }

    // MODIFIES: this
    // EFFECTS: adds one to total wins
    public void addWin() {
        totalWins += 1;
    }

    // MODIFIES: this
    // EFFECTS: sets the total wins
    public void setTotalWins(int wins) {
        totalWins = wins;
    }

    // MODIFIES: this
    // EFFECTS: sets the total games played
    public void setTotalGamesPlayed(int games) {
        totalGamesPlayed = games;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("champion overall", championName);
        json.put("total kills", totalKills);
        json.put("total deaths", totalDeaths);
        json.put("total assists", totalAssists);
        json.put("total games", totalGamesPlayed);
        json.put("won games", totalWins);
        return json;
    }

    public String getChampionName() {
        return championName;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public int getTotalAssists() {
        return totalAssists;
    }

    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public int getTotalWins() {
        return totalWins;
    }

}
