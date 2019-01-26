package de.melonmc.factions.command;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
@Data
public class TabInfo {

    private final ICommand iCommand;
    private final Tab tab;
    private final Method method;

}
