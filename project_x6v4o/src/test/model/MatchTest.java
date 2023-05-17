package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Tests set for match class
public class MatchTest {

    private Profile profile;
    private Match match;
    private Champion jinx;


    @BeforeEach
    public void setup() {
        profile = new Profile("soupfan4");
        jinx = new Champion("Jinx");
        match = new Match(jinx, 12, 4, 8, true);
    }

    @Test
    public void matchConstructorTest() {
        assertEquals(jinx, match.getChampionPlayed());
        assertEquals(12, match.getKills());
        assertEquals(4, match.getDeaths());
        assertEquals(8, match.getAssists());
        assertTrue(match.getWonStatus());

    }

}
