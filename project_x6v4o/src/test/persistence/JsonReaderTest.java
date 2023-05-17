package persistence;

import exceptions.NullChampion;
import model.Match;
import model.Profile;
import model.Champion;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {


    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Profile profile = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyProfile() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyProfile.json");
        try {
            Profile profile = reader.read();
            assertEquals("Slurps", profile.getProfileName());
            assertEquals(0, profile.getMatchHistory().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralProfile() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralProfile.json");
        try {
            Profile profile = reader.read();
            assertEquals("Slurps", profile.getProfileName());
            assertEquals("Master", profile.getRank());
            List<Match> matchHistory = profile.getMatchHistory();
            assertEquals(2, profile.getMatchHistory().size());
            checkMatch("Jinx", 10, 2, 8, true, matchHistory.get(0));
            checkMatch("Jhin", 7, 4, 6, false, matchHistory.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderNullChampionException() {

        JsonReader reader = new JsonReader("./data/testReaderNullChampionException.json");
        try {
            Profile profile = reader.read();
            assertEquals("Slurps", profile.getProfileName());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}