package de.melonmc.factions;
import lombok.AllArgsConstructor;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
public enum Messages {

    TOO_MANY_HOMES("%ERR%Du hast bereits %NUM%{0} %ERR%Homes.", true, true),
    HOME_UPDATED("%TXT%Du hast den Homepunkt (%NUM%{0}%TXT%) aktualisiert.", true, true),
    HOME_SET("%TXT%Du hast einen neuen Homepunkt gesetzt (%NUM%{0}%TXT%)", true, true),
    HOME_NOT_FOUND("Home nicht gefunden", true, true),
    HOME_DELETED("Home gelöscht", true, true),
    HOMES_EMPTY("Du bist obdachlos", true, true),
    HOMES_EMPTY_OTHER("Er ist obdachlos", true, true),
    PLAYER_NOT_FOUND("Spieler nicht am leben oder so", true, true),
    NOT_IN_A_FACTION("Du nicht in faction", true, true),
    FACTION_LIST_PREFIX("Folgende Spieler sind in der Fraktion ({0}):", true, true),
    ALREADY_IN_FACTION("Du schon in faction", true, true),
    FACTION_ALREADY_EXISTS("Faction existiert bereits du noob", true, true),
    FACTION_CREATED("Faction created! HGW DU NOOB {0} {1}", true, true),
    FACTION_PLAYER_ALREADY_INVITED("Der isch schon invited", true, true),
    FACTION_PLAYER_INVITED("Du hast ihn invited!", true, true),
    FACTION_PLAYER_INVITE_SELF("Du = dumm", true, true),
    FACTION_PLAYER_IN_FACTION("Er in Faction", true, true),
    FACTION_PLAYER_INVITE_RECEIVED("Du wurdest von der Faction {0} ({1}) eingeladen", true, true),
    FACTION_PLAYER_INVITES_NOTIFY("Du wurdest von {0} Faction{1} eingeladen.", true, true),
    FACTION_NOT_INVITED("not invited", true, true),
    FACTION_INVITED_FACTION_NOT_FOUND("not found", true, true),
    FACTION_INVITE_ACCEPTED("du join faction {0}", true, true),
    FACTION_INVITE_DECLINED("invite declined", true, true),
    FACTION_PLAYER_JOIN("player {0} join", true, true),
    FACTION_PLAYER_QUIT("player {0} quit", true, true),
    FACTION_PLAYER_KICK_SUCCESSFUL("du hast {0} gekickt", true, true),
    FACTION_PLAYER_KICK("player {0} kick from {1}", true, true),
    FACTION_PLAYER_KICKED("du wurdest gekickt", true, true),
    FACTION_KICK_NOT_ENOUGH_PERMISSIONS("haha, du lappen (frau) hast zu wenig rechte", true, true),
    EMPTY_MESSAGE("%TXT%Hello, I am Nico and I am %NUM%{0} %TXT%years old%PNC%.", false, true);

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
            message = message.replaceAll("%ERR%", Statics.ERROR);
        }

        return message;
    }

    private class Statics {

        static final String PREFIX = "§8» §a§lFactions§8 ● §7";
        static final String NUMBERS = "§e";
        static final String DEFAULT_TEXT = "§7";
        static final String PUNCTUATION_MARK = "§8";
        static final String ERROR = "§c";

    }

}
