package de.melonmc.factions.defaults.invitation;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.melonmc.factions.invitation.InvitationManager;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Nico_ND1
 */
public class DefaultInvitationManager implements InvitationManager {

    private static final Cache<Key, List<UUID>> EMPTY_CACHE = CacheBuilder.newBuilder().build();
    private final Map<UUID, Cache<Key, List<UUID>>> keys = Collections.synchronizedMap(new HashMap<>());

    @Override
    public boolean cache(Key key, UUID uuid, UUID secondaryUuid) {
        if (this.isCached(key, uuid, secondaryUuid)) return false;

        final Cache<Key, List<UUID>> cache = this.keys.getOrDefault(uuid, CacheBuilder.newBuilder()
            .expireAfterWrite(key.getTimeout(), TimeUnit.SECONDS)
            .build());
        try {
            final List<UUID> uuids = cache.get(key, ArrayList::new);
            uuids.add(secondaryUuid);

            cache.put(key, uuids);
            this.keys.put(uuid, cache);
            return true;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean isCached(Key key, UUID uuid, UUID secondaryUuid) {
        final Cache<Key, List<UUID>> cache = this.keys.getOrDefault(uuid, EMPTY_CACHE);

        try {
            final List<UUID> uuids = cache.get(key, Collections::emptyList);

            return uuids.contains(secondaryUuid);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public int getCacheSize(Key key, UUID uuid) {
        final Cache<Key, List<UUID>> cache = this.keys.getOrDefault(uuid, EMPTY_CACHE);
        try {
            final List<UUID> uuids = cache.get(key, Collections::emptyList);

            return uuids.size();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void invalidate(Key key, UUID uuid) {
        if (!this.keys.containsKey(uuid)) return;

        final Cache<Key, List<UUID>> cache = this.keys.get(uuid);
        cache.invalidate(key);
    }

    @Override
    public void invalidate(Key key, UUID uuid, UUID secondaryUuid) {
        if (!this.isCached(key, uuid, secondaryUuid)) return;

        final Cache<Key, List<UUID>> cache = this.keys.get(uuid);
        final List<UUID> uuids = cache.getIfPresent(key);
        uuids.remove(secondaryUuid);

        cache.put(key, uuids);
        this.keys.put(uuid, cache);
    }
}
