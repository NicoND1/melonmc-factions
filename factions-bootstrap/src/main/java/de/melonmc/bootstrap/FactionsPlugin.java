package de.melonmc.bootstrap;
import de.melonmc.bukkit.Bootstrapable;
import de.melonmc.factions.FactionsInstanceHolder;
import de.melonmc.factions.IBootstrapable;
import de.melonmc.factions.database.DatabaseSaver;
import de.melonmc.factions.defaults.DefaultFactions;
import de.melonmc.factions.defaults.chunk.DefaultChunkManager;
import de.melonmc.factions.defaults.database.DefaultDatabaseSaver;
import de.melonmc.factions.defaults.database.MongoConfig;
import de.melonmc.factions.defaults.job.DefaultJobManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * @author Nico_ND1
 */
public class FactionsPlugin extends JavaPlugin {

    private IBootstrapable iBootstrapable;

    @Override
    public void onEnable() {
        MongoConfig mongoConfig = new MongoConfig("localhost", 27017, "username", "password", "database", "collection");
        final File file = new File(this.getDataFolder(), "config.json");
        if (!this.getDataFolder().exists()) this.getDataFolder().mkdir();
        try {
            if (file.createNewFile()) mongoConfig.save(file);
            mongoConfig = MongoConfig.GSON.fromJson(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")), MongoConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final DatabaseSaver databaseSaver = new DefaultDatabaseSaver(mongoConfig);
        FactionsInstanceHolder.setPlugin(this);
        FactionsInstanceHolder.setFactions(new DefaultFactions(databaseSaver, new DefaultChunkManager(), new DefaultJobManager()));

        this.iBootstrapable = new Bootstrapable();
        this.iBootstrapable.onStart();
    }

    @Override
    public void onDisable() {
        this.iBootstrapable.onStop();
    }
}
