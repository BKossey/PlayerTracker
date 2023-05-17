package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

import java.util.Objects;

// Represents the users profile, including rank, username, and match history
public class Profile implements Writable {

    Champion jinx = new Champion("Jinx");
    Champion twitch = new Champion("Twitch");
    Champion ezreal = new Champion("Ezreal");
    Champion zeri = new Champion("Zeri");
    Champion jhin = new Champion("Jhin");


    private String rank;
    private String profileName;
    private ArrayList<Match> matchHistory;
    private ArrayList<Match> filteredMatchHistory;
    private ArrayList<Champion> championList;

    // EFFECTS: Creates an unranked profile with a name
    public Profile(String name) {
        profileName = name;
        rank = "Unranked";
        matchHistory = new ArrayList<>();

        championList = new ArrayList<>();
        championList.add(jinx);
        championList.add(twitch);
        championList.add(ezreal);
        championList.add(zeri);
        championList.add(jhin);

    }

    // MODIFIES: this
    // EFFECTS: sets profileName to input
    public void setProfileName(String name) {
        this.profileName = name;
    }

    // MODIFIES: this, EventLog
    // EFFECTS: adds match to match history and records event
    public void record(Match match) {
        matchHistory.add(match);
        EventLog.getInstance().logEvent(new Event("Added " + match.getChampionPlayed().getChampionName()
                                                    + " game to match history. "));
    }

    // MODIFIES: this, EventLog
    // EFFECTS: removes match from match history and records event
    public void remove(Match match) {
        matchHistory.remove(match);
        EventLog.getInstance().logEvent(new Event("Removed " + match.getChampionPlayed().getChampionName()
                                                    + " game from match history. "));
    }

    // MODIFIES: this, EventLog
    // EFFECTS: returns a filtered match history of matchHistory with only the matches with the given champion
    public ArrayList<Match> filter(Champion champion) {
        filteredMatchHistory = new ArrayList<>();
        for (Match m : matchHistory) {
            if (Objects.equals(m.getChampionPlayed(), champion)) {
                filteredMatchHistory.add(m);
            }
        }
        EventLog.getInstance().logEvent(new Event("Filtered match history to " + champion.getChampionName()
                                                    + "."));
        return filteredMatchHistory;
    }

    // MODIFIES: EventLog
    // EFFECTS: records filter being turned off to EventLog
    public void filterOff() {
        EventLog.getInstance().logEvent(new Event("Disabled match history filter."));
    }

    // MODIFIES: EventLog
    // EFFECTS: Clears EventLog and records matches added by loading saved profile to EventLog
    public void loadLog() {
        EventLog.getInstance().clear();
        for (Match m : matchHistory) {
            EventLog.getInstance().logEvent(new Event("Added " + m.getChampionPlayed().getChampionName()
                    + " game to match history. "));

        }
        EventLog.getInstance().logEvent(new Event("Logged matches from saved profile"));
    }



    // MODIFIES: this
    // EFFECTS: promotes the profile one tier
    public void promote() {
        if (Objects.equals(rank, "Unranked")) {
            rank = "Iron";
        } else if (Objects.equals(rank, "Iron")) {
            rank = "Bronze";
        } else if (Objects.equals(rank, "Bronze")) {
            rank = "Silver";
        } else if (Objects.equals(rank, "Silver")) {
            rank = "Gold";
        } else if (Objects.equals(rank, "Gold")) {
            rank = "Platinum";
        } else if (Objects.equals(rank, "Platinum")) {
            rank = "Diamond";
        } else if (Objects.equals(rank, "Diamond")) {
            rank = "Master";
        } else if (Objects.equals(rank, "Master")) {
            rank = "Grandmaster";
        } else if (Objects.equals(rank, "Grandmaster")) {
            rank = "Challenger";
        }
    }

    // MODIFIES: this
    // EFFECTS: demotes the profile one tier
    public void demote() {
        if (Objects.equals(rank, "Challenger")) {
            rank = "Grandmaster";
        } else if (Objects.equals(rank, "Grandmaster")) {
            rank = "Master";
        } else if (Objects.equals(rank, "Master")) {
            rank = "Diamond";
        } else if (Objects.equals(rank, "Diamond")) {
            rank = "Platinum";
        } else if (Objects.equals(rank, "Platinum")) {
            rank = "Gold";
        } else if (Objects.equals(rank, "Gold")) {
            rank = "Silver";
        } else if (Objects.equals(rank, "Silver")) {
            rank = "Bronze";
        } else if (Objects.equals(rank, "Bronze")) {
            rank = "Iron";
        }
    }

    //REQUIRES: rank must be one of: "Iron", "Bronze", "Silver", "Gold", "Platinum", "Diamond", "Master",
    //          "Grandmaster", "Challenger"
    // MODIFIES: this
    // EFFECTS: immediately set this.rank to  rank
    public void setRank(String rank) {
        this.rank = rank;

    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("profile name", profileName);
        json.put("rank", rank);
        json.put("match history", matchHistoryToJson());
        json.put("champions", championsToJson());
        return json;
    }

    // EFFECTS: returns match history for this profile as a JSON array
    private JSONArray matchHistoryToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Match m : matchHistory) {
            jsonArray.put(m.toJson());
        }

        return jsonArray;
    }

    private JSONArray championsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Champion c : championList) {
            jsonArray.put(c.toJson());
        }
        return jsonArray;
    }


    public String getProfileName() {
        return profileName;
    }

    public String getRank() {
        return rank;
    }

    public ArrayList<Match> getMatchHistory() {
        return matchHistory;
    }

    public ArrayList<Champion> getChampions() {
        return championList;
    }
}
