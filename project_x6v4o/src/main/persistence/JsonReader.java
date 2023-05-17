package persistence;


import exceptions.NullChampion;
import model.Profile;
import model.Match;
import model.Champion;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads profile from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads profile from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Profile read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseProfile(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses profile from JSON object and returns it
    private Profile parseProfile(JSONObject jsonObject) {
        String profileName = jsonObject.getString("profile name");
        String rank = jsonObject.getString("rank");
        Profile profile = new Profile(profileName);
        profile.setRank(rank);
        addMatchHistory(profile, jsonObject);
        reloadStats(profile, jsonObject);
        return profile;
    }

    // MODIFIES: profile
    // EFFECTS: parses match history from JSON object and adds them to profile
    private void addMatchHistory(Profile profile, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("match history");
        for (Object json : jsonArray) {
            JSONObject nextMatch = (JSONObject) json;
            addMatch(profile, nextMatch);
        }
    }

    // MODIFIES: profile
    // EFFECTS: parses match from JSON object and adds it to profile
    private void addMatch(Profile profile, JSONObject jsonObject) {
        String jsonChampion = jsonObject.getString("champion");
        Champion champion = selectChampionReader(jsonChampion, profile);
        int kills = jsonObject.getInt("kills");
        int deaths = jsonObject.getInt("deaths");
        int assists = jsonObject.getInt("assists");
        boolean wonGame = jsonObject.getBoolean("game won?");


        Match match = new Match(champion, kills, deaths, assists, wonGame);
        profile.getMatchHistory().add(match);
    }

    // MODIFIES: profile
    // EFFECTS: parses champion stats list from JSON object and adds them to profile
    private void reloadStats(Profile profile, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("champions");
        for (Object json : jsonArray) {
            JSONObject nextChampion = (JSONObject) json;
            try {
                reloadChampionStats(profile, nextChampion);
            } catch (NullChampion e) {
                System.out.println("Error loading champion data");
            }
        }
    }

    // MODIFIES: profile
    // EFFECTS: parses champion stats from JSON object and updates them all
    private void reloadChampionStats(Profile profile, JSONObject jsonObject) throws NullChampion {
        if (null == selectChampionReader(jsonObject.getString("champion overall"), profile)) {
            throw new NullChampion();
        } else {
            Champion champion = selectChampionReader(jsonObject.getString("champion overall"), profile);
            champion.addKills(jsonObject.getInt("total kills"));
            champion.addDeaths(jsonObject.getInt("total deaths"));
            champion.addAssists(jsonObject.getInt("total assists"));
            champion.setTotalGamesPlayed(jsonObject.getInt("total games"));
            champion.setTotalWins(jsonObject.getInt("won games"));
        }
    }


    // REQUIRES: champion is one of possible champions
    // EFFECTS: selects and returns a champion based off champion name string
    private Champion selectChampionReader(String champion, Profile profile) {
        for (Champion c : profile.getChampions()) {
            if (Objects.equals(c.getChampionName(), champion)) {
                return c;
            }
        }
        return null;
    }


}
