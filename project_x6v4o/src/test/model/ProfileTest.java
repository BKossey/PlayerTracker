package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Tests set for profile class
class ProfileTest {

    private Profile profile;

    @BeforeEach
    public void setup() {
        profile = new Profile("Slurps");
    }

    @Test
    public void profileConstructorTest() {
        assertEquals("Slurps", profile.getProfileName());
        assertEquals("Unranked", profile.getRank());
        assertEquals(0, profile.getMatchHistory().size());
        assertEquals(5, profile.getChampions().size());
    }

    @Test
    public void setRankTest() {
        profile.setRank("Diamond");
        assertEquals("Diamond", profile.getRank());
        profile.setRank("Challenger");
        assertEquals("Challenger", profile.getRank());
        profile.setRank("Iron");
        assertEquals("Iron", profile.getRank());
    }

    @Test
    public void promoteTest() {
        profile.promote();
        assertEquals("Iron", profile.getRank());
        profile.promote();
        assertEquals("Bronze", profile.getRank());
        profile.promote();
        assertEquals("Silver", profile.getRank());
        profile.promote();
        assertEquals("Gold", profile.getRank());
        profile.promote();
        assertEquals("Platinum", profile.getRank());
        profile.promote();
        assertEquals("Diamond", profile.getRank());
        profile.promote();
        assertEquals("Master", profile.getRank());
        profile.promote();
        assertEquals("Grandmaster", profile.getRank());
        profile.promote();
        assertEquals("Challenger", profile.getRank());
        profile.promote();
        assertEquals("Challenger", profile.getRank());
    }

    @Test
    public void demoteTest() {
        profile.setRank("Challenger");
        profile.demote();
        assertEquals("Grandmaster", profile.getRank());
        profile.demote();
        assertEquals("Master", profile.getRank());
        profile.demote();
        assertEquals("Diamond", profile.getRank());
        profile.demote();
        assertEquals("Platinum", profile.getRank());
        profile.demote();
        assertEquals("Gold", profile.getRank());
        profile.demote();
        assertEquals("Silver", profile.getRank());
        profile.demote();
        assertEquals("Bronze", profile.getRank());
        profile.demote();
        assertEquals("Iron", profile.getRank());
        profile.demote();
        assertEquals("Iron", profile.getRank());

    }
}