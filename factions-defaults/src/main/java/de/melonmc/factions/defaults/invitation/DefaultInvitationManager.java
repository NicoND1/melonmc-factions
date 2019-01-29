package de.melonmc.factions.defaults.invitation;
import de.melonmc.factions.invitation.InvitationManager;

import java.util.*;

/**
 * @author Nico_ND1
 */
public class DefaultInvitationManager implements InvitationManager {

    private final Map<UUID, Map<Key, List<UUID>>> keys = Collections.synchronizedMap(new HashMap<>());

    @Override
    public boolean cache(Key key, UUID uuid, UUID secondaryUuid) {
        if (this.isCached(key, uuid, secondaryUuid)) return false;

        final Map<Key, List<UUID>> map = this.keys.getOrDefault(uuid, new HashMap<>());
        final List<UUID> uuids = map.getOrDefault(key, new ArrayList<>());
        uuids.add(secondaryUuid);

        map.put(key, uuids);
        this.keys.put(uuid, map);
        return true;
    }

    @Override
    public boolean isCached(Key key, UUID uuid, UUID secondaryUuid) {
        final Map<Key, List<UUID>> map = this.keys.getOrDefault(uuid, Collections.emptyMap());
        final List<UUID> uuids = map.getOrDefault(key, Collections.emptyList());

        return uuids.contains(secondaryUuid);
    }

    @Override
    public int getCacheSize(Key key, UUID uuid) {
        final Map<Key, List<UUID>> map = this.keys.getOrDefault(uuid, Collections.emptyMap());
        final List<UUID> uuids = map.getOrDefault(key, Collections.emptyList());

        return uuids.size();
    }

    @Override
    public void invalidate(Key key, UUID uuid) {
        if (!this.keys.containsKey(uuid)) return;

        final Map<Key, List<UUID>> map = this.keys.get(uuid);
        map.remove(key);
    }

    @Override
    public void invalidate(Key key, UUID uuid, UUID secondaryUuid) {
        if (!this.isCached(key, uuid, secondaryUuid)) return;

        final Map<Key, List<UUID>> map = this.keys.get(uuid);
        final List<UUID> uuids = map.get(key);
        uuids.remove(secondaryUuid);

        map.put(key, uuids);
        this.keys.put(uuid, map);
    }
}
