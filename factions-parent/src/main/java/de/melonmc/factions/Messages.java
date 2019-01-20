package de.melonmc.factions;
import lombok.AllArgsConstructor;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
public enum Messages {

    EMPTY_MESSAGE("%TXT%Hello, I am Nico and I am %NUM%16 %TXT%years old%PNC%.", true, true);

    private final String message;
    private final boolean prefix;
    private final boolean autoFormat;

    public String getMessage(Object... args) {
        String message = this.message;
        if (this.prefix) message = Statics.PREFIX + message;

        for (int i = 0; i < args.length; i++)
            message = message.replace("{" + i + "}", args[i].toString());
        if (this.autoFormat) {
            message = message.replaceAll("%NUM%", Statics.NUMBERS);
            message = message.replaceAll("%TXT%", Statics.DEFAULT_TEXT);
            message = message.replaceAll("%PNC%", Statics.PUNCTUATION_MARK);
        }

        return message;
    }

    private class Statics {

        static final String PREFIX = "§8» §a§lFactions§8 ● §7";
        static final String NUMBERS = "§e";
        static final String DEFAULT_TEXT = "§7";
        static final String PUNCTUATION_MARK = "§8";

    }

}
