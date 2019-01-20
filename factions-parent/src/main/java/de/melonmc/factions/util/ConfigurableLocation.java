package de.melonmc.factions.util;
import lombok.AllArgsConstructor;
import lombok.Data;
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

}
