package persistence;

import model.Match;
import model.Champion;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkMatch(String champion, int kills, int deaths, int assists, boolean wonGame, Match match) {
        assertEquals(champion, match.getChampionPlayed().getChampionName());
        assertEquals(kills, match.getKills());
        assertEquals(deaths, match.getDeaths());
        assertEquals(assists, match.getAssists());
        assertEquals(wonGame, match.getWonStatus());
    }
}
