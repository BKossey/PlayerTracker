package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Tests set for champion class
public class ChampionTest {

    private Champion champion;

    @BeforeEach
    public void setup() {
        champion = new Champion("Twitch");
    }

    @Test
    public void championConstructorTest() {
        assertEquals("Twitch", champion.getChampionName());
        assertEquals(0, champion.getTotalKills());
        assertEquals(0, champion.getTotalDeaths());
        assertEquals(0, champion.getTotalAssists());
        assertEquals(0, champion.getTotalGamesPlayed());
        assertEquals(0, champion.getTotalWins());
    }

    @Test
    public void addKillsTest() {
        champion.addKills(7);
        assertEquals(7, champion.getTotalKills());
        champion.addKills(1);
        champion.addKills(4);
        assertEquals(12, champion.getTotalKills());
    }

    @Test
    public void addDeathsTest() {
        champion.addDeaths(3);
        assertEquals(3, champion.getTotalDeaths());
        champion.addDeaths(6);
        champion.addDeaths(2);
        assertEquals(11, champion.getTotalDeaths());
    }

    @Test
    public void addAssistsTest() {
        champion.addAssists(10);
        assertEquals(10, champion.getTotalAssists());
        champion.addAssists(12);
        champion.addAssists(8);
        assertEquals(30, champion.getTotalAssists());
    }

    @Test
    public void addGamePlayedTest() {
        champion.addGamePlayed();
        assertEquals(1, champion.getTotalGamesPlayed());
        champion.addGamePlayed();
        champion.addGamePlayed();
        champion.addGamePlayed();
        assertEquals(4, champion.getTotalGamesPlayed());
    }

    @Test
    public void addWinTest() {
        champion.addWin();
        assertEquals(1, champion.getTotalWins());
        champion.addWin();
        champion.addWin();
        assertEquals(3, champion.getTotalWins());
    }

    @Test
    public void setTotalGamesPlayedTest() {
        champion.setTotalGamesPlayed(5);
        assertEquals(5, champion.getTotalGamesPlayed());
    }

    @Test
    public void setTotalWinsTest() {
        champion.setTotalWins(3);
        assertEquals(3, champion.getTotalWins());
    }


}
