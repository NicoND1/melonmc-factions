package de.melonmc.factions.invitation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * @author Nico_ND1
 */
public interface InvitationManager {

    @AllArgsConstructor
    public enum Key {

        TPA(30),
        TRADE(45);

        @Getter private final int timeout;

    }

    boolean cache(Key key, UUID uuid, UUID secondaryUuid);

    boolean isCached(Key key, UUID uuid, UUID secondaryUuid);

    int getCacheSize(Key key, UUID uuid);

    void invalidate(Key key, UUID uuid);

    void invalidate(Key key, UUID uuid, UUID secondaryUuid);

}
