package de.melonmc.factions.defaults.invitation;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.melonmc.factions.invitation.InvitationManager;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.ObjectUtils.Null;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Nico_ND1
 */
public class DefaultInvitationManager implements InvitationManager {

    private static final Cache<UUID, Null> EMPTY_CACHE = CacheBuilder.newBuilder().build();
    private final Map<UUID, Cache<Key, List<UUID>>> keys2 = Collections.synchronizedMap(new HashMap<>());
    private final Map<Key, Map<UUID, Cache<UUID, Null>>> keys = Collections.synchronizedMap(new HashMap<>());

    @Override
    public boolean cache(Key key, UUID uuid, UUID secondaryUuid) {
        if (this.isCached(key, uuid, secondaryUuid)) return false;

        final Map<UUID, Cache<UUID, Null>> map = this.keys.getOrDefault(key, new HashMap<>());
        final Cache<UUID, Null> cache = map.getOrDefault(uuid, CacheBuilder.newBuilder()
            .expireAfterWrite(key.getTimeout(), TimeUnit.SECONDS)
            .build());
        cache.put(secondaryUuid, ObjectUtils.NULL);
        map.put(uuid, cache);
        this.keys.put(key, map);

        return true;
    }

    @Override
    public boolean isCached(Key key, UUID uuid, UUID secondaryUuid) {
        final Map<UUID, Cache<UUID, Null>> map = this.keys.getOrDefault(key, Collections.emptyMap());
        final Cache<UUID, Null> cache = map.getOrDefault(uuid, EMPTY_CACHE);

        return cache.asMap().containsKey(secondaryUuid);
    }

    @Override
    public int getCacheSize(Key key, UUID uuid) {
        final Map<UUID, Cache<UUID, Null>> map = this.keys.getOrDefault(key, Collections.emptyMap());
        final Cache<UUID, Null> cache = map.getOrDefault(uuid, EMPTY_CACHE);

        return (int) cache.size();
    }

    @Override
    public void invalidate(Key key, UUID uuid) {
        final Map<UUID, Cache<UUID, Null>> map = this.keys.getOrDefault(key, Collections.emptyMap());
        if (!map.containsKey(uuid)) return;

        map.remove(uuid);
    }

    @Override
    public void invalidate(Key key, UUID uuid, UUID secondaryUuid) {
        if (!this.isCached(key, uuid, secondaryUuid)) return;

        final Map<UUID, Cache<UUID, Null>> map = this.keys.getOrDefault(key, Collections.emptyMap());
        final Cache<UUID, Null> cache = map.getOrDefault(uuid, EMPTY_CACHE);
        cache.invalidate(secondaryUuid);
    }
}
