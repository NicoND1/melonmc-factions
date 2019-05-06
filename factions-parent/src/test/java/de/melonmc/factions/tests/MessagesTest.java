package de.melonmc.factions.tests;
import de.melonmc.factions.Messages;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Nico_ND1
 */
public class MessagesTest {

    @Test
    public void testEmptyMessage() {
        final String message = Messages.EMPTY_MESSAGE.getMessage(20);
        Assert.assertEquals("Message should be equal!",
            "§7Hello, I am Nico and I am §e20 §7years old§8.",
            message);
    }

    @Test
    public void testTooManyHomesMessage() {
        final String message = Messages.TOO_MANY_HOMES.getMessage(3);
        Assert.assertEquals("Message should be equal!",
            "§8» §a§lFraktionen§8 ● §7Du hast bereits §e3 §7Homes§8.",
            message);
    }

}
