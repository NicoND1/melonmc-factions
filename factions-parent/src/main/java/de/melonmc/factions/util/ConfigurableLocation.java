package de.melonmc.factions.util;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
@Data
public class ConfigurableLocation {

    private final String worldName;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public ConfigurableLocation(Location location) {
        this.worldName = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public ConfigurableLocation(Document document) {
        this.worldName = document.getString("worldName");
        this.x = document.getDouble("x");
        this.y = document.getDouble("y");
        this.z = document.getDouble("z");
        this.yaw = Float.valueOf(Double.toString(document.getDouble("yaw")));
        this.pitch = Float.valueOf(Double.toString(document.getDouble("pitch")));
    }

    public Location toLocation() {
        return new Location(
            Bukkit.getWorld(this.worldName),
            this.x,
            this.y,
            this.z,
            this.yaw,
            this.pitch
        );
    }

    public Document createDocument() {
        return new Document("worldName", this.worldName)
            .append("x", this.x)
            .append("y", this.y)
            .append("z", this.z)
            .append("yaw", this.yaw)
            .append("pitch", this.pitch);
    }

}
