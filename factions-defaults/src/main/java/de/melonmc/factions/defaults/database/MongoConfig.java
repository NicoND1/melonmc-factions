package de.melonmc.factions.defaults.database;
import com.google.gson.*;
import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class MongoConfig implements JsonDeserializer<MongoConfig> {

    public static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .excludeFieldsWithoutExposeAnnotation()
        .registerTypeAdapter(MongoConfig.class, new MongoConfig())
        .create();
    @Expose private final String host;
    @Expose private final int port;
    @Expose private final String username;
    @Expose private final String password;
    @Expose private final String database;
    @Expose private final String collection;

    public MongoConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        return new MongoConfig(
            jsonObject.get("host").getAsString(),
            jsonObject.get("port").getAsInt(),
            jsonObject.get("username").getAsString(),
            jsonObject.get("password").getAsString(),
            jsonObject.get("database").getAsString(),
            jsonObject.get("collection").getAsString()
        );
    }

    public void save(File file) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(GSON.toJson(this, MongoConfig.class));
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
