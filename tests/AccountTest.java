/*
 * Course: Software Tools & Process
 * Spring 2025
 * Group
 ** @author childressg
 ** @author OrugPeli
 ** @author Griffithjr
 ** @author kozakq
 * @version 1.0
 */

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountTest {
    @BeforeAll
    public static void setUp() {
        WordleApp.initialize();
    }

    @Test
    public void testLogin() {
        assertFalse(WordleApp.login("username", "password"));
        assertTrue(WordleApp.isValidUsername("username"));
        assertFalse(WordleApp.isLoggedIn());

        assertTrue(WordleApp.createAccount("username", "password"));
        assertTrue(WordleApp.isLoggedIn());

        WordleApp.logout();
    }

    @Test
    public void testAverageGuesses() {
        WordleApp.login("username", "password");

        assertEquals(WordleApp.getAverageGuessess(), 0);
        WordleApp.addGuessCount(2);
        WordleApp.addGuessCount(1);
        WordleApp.addGuessCount(3);
        assertEquals(WordleApp.getAverageGuessess(), 2);
        WordleApp.addGuessCount(4);
        WordleApp.addGuessCount(5);
        assertEquals(WordleApp.getAverageGuessess(), 3);

        WordleApp.logout();
    }

    @Test
    public void testLogoutAndSave() {
        WordleApp.login("username", "password");
        WordleApp.save();
        assertTrue(WordleApp.logout());
        assertFalse(WordleApp.isLoggedIn());

        WordleApp.reloadAccounts();

        assertFalse(WordleApp.isValidUsername("username"));
        assertFalse(WordleApp.login("username", "different password"));
        assertTrue(WordleApp.login("username", "password"));
        assertTrue(WordleApp.isLoggedIn());
    }
}
