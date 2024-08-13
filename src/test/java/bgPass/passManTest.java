package bgPass;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class passManTest {
    @Test
    void addAccountTest() {
        passMan passManager = new passMan();
        try {
            passManager.addAccount("testTitle", "testUsername", "testPassword", "testNotes", "testLastUpdated", "testCategory");
            List<passValues> accounts = passManager.readDataFromFile("src/main/resources/imp_files/password.json");
            assertNotNull(accounts);
            assertFalse(accounts.isEmpty());
            // Add more assertions here to validate the account was added correctly
        } catch (Exception e) {
            fail("Exception thrown during test: " + e.getMessage());
        }
    }
}