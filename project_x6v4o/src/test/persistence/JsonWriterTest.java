package persistence;

import model.Profile;
import model.Match;
import model.Champion;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {

    Champion jinxTest = new Champion("Jinx");


    @Test
    void testWriterInvalidFile() {
        try {
            Profile profile = new Profile("soupfan4");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyProfile() {
        try {
            Profile profile = new Profile("soupfan4");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyProfile.json");
            writer.open();
            writer.write(profile);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyProfile.json");
            profile = reader.read();
            assertEquals("soupfan4", profile.getProfileName());
            assertEquals(0, profile.getMatchHistory().size());
            assertEquals("Unranked", profile.getRank());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralProfile() {
        try {
            Profile profile = new Profile("Slurps");
            profile.getMatchHistory().add(new Match(jinxTest, 10, 2, 8, true));
            profile.getMatchHistory().add(new Match(jinxTest, 7, 4, 6, false));
            profile.setRank("Master");
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralProfile.json");
            writer.open();
            writer.write(profile);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralProfile.json");
            profile = reader.read();
            assertEquals("Slurps", profile.getProfileName());
            assertEquals("Master", profile.getRank());
            List<Match> matchHistory = profile.getMatchHistory();
            assertEquals(2, profile.getMatchHistory().size());
            checkMatch("Jinx", 10, 2, 8, true, matchHistory.get(0));
            checkMatch("Jinx", 7, 4, 6, false, matchHistory.get(1));



        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}