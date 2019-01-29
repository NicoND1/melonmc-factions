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
    FACTION_NOT_FOUND("Faction nicht am existieren", true, true),
    FACTION_ALREADY_EXISTS("Faction existiert bereits du noob", true, true),
    FACTION_INVALID_TAG("Der Tag muss mindestens {0} und höchstens {1} Zeichen lang sein.", true, true),
    FACTION_CREATED("Faction created! HGW DU NOOB {0} {1}", true, true),
    FACTION_DELETED("Faction gelöscht, lol", true, true),
    FACTION_PLAYER_ALREADY_INVITED("Der isch schon invited", true, true),
    FACTION_PLAYER_INVITED("Du hast ihn invited!", true, true),
    FACTION_PLAYER_INVITED_BROADCAST("{0} wurde invited", true, true),
    FACTION_PLAYER_INVITE_SELF("Du = dumm", true, true),
    FACTION_PLAYER_IN_FACTION("Er in Faction", true, true),
    FACTION_PLAYER_INVITE_RECEIVED("Du wurdest von der Faction {0} ({1}) eingeladen", true, true),
    FACTION_PLAYER_INVITES_NOTIFY("Du wurdest von {0} Faction{1} eingeladen.", true, true),
    FACTION_NOT_INVITED("not invited", true, true),
    FACTION_INVITED_FACTION_NOT_FOUND("not found", true, true),
    FACTION_INVITE_ACCEPTED("du join faction {0}", true, true),
    FACTION_INVITE_DECLINED("invite declined", true, true),
    FACTION_PLAYER_INVITE_MAX("Deine Fraktion kann nur {0} Leute einladen", true, true),
    FACTION_PLAYER_JOIN("player {0} join", true, true),
    FACTION_PLAYER_QUIT("player {0} quit", true, true),
    FACTION_PLAYER_KICK_SUCCESSFUL("du hast {0} gekickt", true, true),
    FACTION_PLAYER_KICK_SELF("Du kannst dich nicht selbst kicken", true, true),
    FACTION_PLAYER_KICK("player {0} kick from {1}", true, true),
    FACTION_PLAYER_KICKED("du wurdest gekickt", true, true),
    FACTION_LEAVE("Du hast verlassen", true, true),
    FACTION_LEAVE_REQUEST("Willst du wirklich leaven?", true, true),
    FACTION_LEAVE_REQUEST_REJECT("Ok, dann nicht", true, true),
    FACTION_NOT_ENOUGH_PERMISSIONS("haha, du lappen (frau) hast zu wenig rechte", true, true),
    FACTION_TARGET_PLAYER_NOT_FOUND("{0} nicht in der Faction", true, true),
    FACTION_PLAYER_PROMOTE_SUCCESS("Du hast {0} zu {1} promoted", true, true),
    FACTION_PLAYER_PROMOTE("{0} ist nun {1}", true, true),
    FACTION_RANK_NOT_FOUND("Der Rang wurde nicht gefunden.", true, true),
    FACTION_PLAYER_RANK_NEEDS_CHANGE("Der Spieler hat doch eh schon den Rang", true, true),
    FACTION_STATS("Die Fraktion {0} hat {1} Kill{2} und {3} Tod{4}", true, true),
    FACTION_PLAYER_BASE_SET("Du hast Base umgesetzt", true, true),
    FACTION_CHUNK_ALREADY_CLAIMED("Schon für coolere Menschen", true, true),
    FACTION_CHUNK_CLAIMED("Chunk geclaimt", true, true),
    FACTION_CHUNK_NOT_CLAIMED("Chunk nicht geclaimt", true, true),
    FACTION_CHUNK_NOT_OWN("Nicht deins", true, true),
    FACTION_CHUNK_MAX("Deine Fraktion kann nur {0} Chunks haben", true, true),

    CHESTSHOP_UNKNOWN_MATERIAL("Gebe ein gültiges Material an", true, true),
    CHESTSHOP_UNKNOWN_INTEGER("Gebe eine gültige Zahl an (ohne Komma, ...)", true, true),
    CHESTSHOP_AMOUNT_TOO_HIGH("Amount darf nicht höher als 64 sein", true, true),
    CHESTSHOP_AMOUNT_TOO_LITTLE("Amount muss eins oder mehr sein", true, true),
    CHESTSHOP_CREATED("Chestshop erstellt", true, true),
    CHESTSHOP_DELETED("Chestshop gelöscht", true, true),
    CHESTSHOP_BUY_OWN("%ERR%Du kannst nicht in deinem eigenen Shop einkaufen", true, true),
    CHESTSHOP_EMPTY("Der Shop ist zu leer", true, true),
    CHESTSHOP_INVENTORY_FULL("Dein Inventar ist zu voll", true, true),
    CHESTSHOP_TOO_LITTLE_MONEY("Du hast zu wenig Coins", true, true),
    CHESTSHOP_PLAYER_BOUGHT("{0} hat {1} in einem deiner Shops für {2} Coins gekauft.", true, true),
    CHESTSHOP_PLAYER_BOUGHT_SUCCESS("Du hast {0} für {1} Coins aus dem Shop von {2}", true, true),

    MONEY_UNKNOWN_INTEGER("Gebe eine gültige Zahl an", true, true),
    MONEY_PAY_OWN_TOO_LITTLE("Du hast dafür zu wenig Geld", true, true),
    MONEY_PAY_TOO_LITTLE("Du musst mindestens 1 Coin angeben", true, true),
    MONEY_PAY_SELF("Du willst dir selbst Geld schenken? Okay...", true, true),
    MONEY_PAY_TIMEOUT("Warte noch kurz", true, true),
    MONEY_PAY_SUCCESS("Du hast {0} {1} Coins geschenkt", true, true),
    MONEY_PAY_RECEIVED("{0} hat dir {1} Coins geschenkt", true, true),
    MONEY_ADD_SUCCESS("Du hast {0} {1} Coins hinzugefügt", true, true),
    MONEY_REMOVE_SUCCESS("Du hast {0} {1} Coins genommen", true, true),

    JOB_LEVEL_ACHIEVED("Du bist in deinem Job {0} nun Level {1}", true, true),

    TPA_TIMEOUT("Warte noch", true, true),
    TPA_NO_REQUESTS("Niemand will zu dir", true, true),
    TPA_REQUEST_SENT("Du willst zu {0}", true, true),
    TPA_REQUEST_ACCEPT("Du hast die Anfrage von {0} akzeptiert", true, true),
    TPA_REQUEST_ACCEPTED("{0} hat deine Anfrage akzeptiert", true, true),
    TPA_REQUEST_PLAYER_OFFLINE("Der Spieler ist nicht mehr online", true, true),
    TPA_NO_REQUESTS_FROM("Der will nicht zu dir", true, true),

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
