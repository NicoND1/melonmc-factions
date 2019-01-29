package de.melonmc.factions.invitation;
import de.melonmc.factions.defaults.invitation.DefaultInvitationManager;
import de.melonmc.factions.invitation.InvitationManager.Key;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

/**
 * @author Nico_ND1
 */
public class DefaultInvitationManagerTest {

    @Test
    public void onInvitationManagerTest() {
        final DefaultInvitationManager invitationManager = new DefaultInvitationManager();
        final UUID keyUuid = UUID.randomUUID();
        final UUID valueUuid = UUID.randomUUID();

        Assert.assertTrue(invitationManager.cache(Key.TPA, keyUuid, valueUuid));
        Assert.assertTrue(invitationManager.isCached(Key.TPA, keyUuid, valueUuid));
        Assert.assertFalse(invitationManager.isCached(Key.TRADE, keyUuid, valueUuid));
        Assert.assertFalse(invitationManager.isCached(Key.TPA, UUID.randomUUID(), valueUuid));
        Assert.assertFalse(invitationManager.isCached(Key.TPA, keyUuid, UUID.randomUUID()));
        Assert.assertFalse(invitationManager.isCached(Key.TPA, valueUuid, keyUuid));
        Assert.assertEquals(1, invitationManager.getCacheSize(Key.TPA, keyUuid));
        Assert.assertEquals(0, invitationManager.getCacheSize(Key.TPA, valueUuid));
    }
}
