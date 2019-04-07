package de.melonmc.factions;
import lombok.AllArgsConstructor;

/**
 * @author Nico_ND1
 */
@AllArgsConstructor
public enum Messages {
    TOO_MANY_HOMES("Du hast bereits %NUM%{0} %TXT%Homes§8.", true, true),
    HOME_UPDATED("%TXT%Du hast den Homepunkt §e%NUM%{0} §7aktualisiert§8.", true, true),
    HOME_SET("%TXT%Du hast einen neuen Homepunkt gesetzt §8(§e%NUM%{0}%TXT%§8)", true, true),
    HOME_NOT_FOUND("Dieser Homepunkt wurde §cnicht §7gefunden§8.", true, true),
    HOME_DELETED("Der Homepunkt wurde §cgelöscht§8.", true, true),
    HOMES_EMPTY("Du hast noch §ckeine §7Homepunkte gesetzt§8!", true, true),
    HOMES_EMPTY_OTHER("Der Spieler hat noch §ckeine %TXT%Homepunkte§8.", true, true),
    HOMES_LIST("Deine Homepunkte§8: §e{0}§8.", true, true),

    PLAYER_NOT_FOUND("Dieser Spieler wurde §cnicht gefunden§8!", true, true),

    NOT_IN_A_FACTION("Du bist in §ckeiner §7Fraktion§8!", true, true),
    FACTION_LIST_PREFIX("§8§m--------------§8[ §a§l{0} §8]§8§m--------------" + "\n ", false, true),
    ALREADY_IN_FACTION("Du bist §aschon §7in einer Fraktion§8!", true, true),
    FACTION_NOT_FOUND("Diese Fraktion gibt es §cnicht§8!", true, true),
    FACTION_ALREADY_EXISTS("Diese Fraktion gibt es §aschon§8!", true, true),
    FACTION_INVALID_TAG("Der Tag muss mindestens §e{0} §7und höchstens §e{1} §7Zeichen lang sein§8.", true, true),
    FACTION_CREATED("Deine Fraktion §e{0} §7mit dem Tag §e{1} §7wurde erstellt§8!", true, true),
    FACTION_DELETED("Fraktion wurde §cgelöscht§8!", true, true),
    FACTION_PLAYER_ALREADY_INVITED("Dieser Spieler wurde §cschon §7eingeladen§8.", true, true),
    FACTION_PLAYER_INVITED("Der Spieler wurde zur Fraktion §aeingeladen§8.", true, true),
    FACTION_PLAYER_INVITED_BROADCAST("§3{0} §7wurde zur Fraktion §aeingeladen§8.", true, true),
    FACTION_PLAYER_INVITE_SELF("§7Du kannst dich selber §cnicht einladen§8!", true, true),
    FACTION_PLAYER_IN_FACTION("Dieser Spieler ist schon in einer Fraktion§8!", true, true),
    FACTION_PLAYER_INVITE_RECEIVED("Du wurdest von der Faction §e{0} §8(§7{1}§8) §aeingeladen§8.", true, true),
    FACTION_PLAYER_INVITES_NOTIFY("Du wurdest von §e{0} §7zur Faction §e{1} §7eingeladen§8.", true, true),
    FACTION_NOT_INVITED("§7Von dieser Fraktion wurdest du §cnicht §7eingeladn§8!", true, true),
    FACTION_INVITED_FACTION_NOT_FOUND("Diese Fraktion wurde §cnicht §7gefunden§8.", true, true),
    FACTION_INVITE_ACCEPTED("Du bist der Fraktion §e{0} §7beigetreten§8!", true, true),
    FACTION_INVITE_DECLINED("Du hast die Einladung §cabgelehnt§8!", true, true),
    FACTION_PLAYER_INVITE_MAX("Deine Fraktion kann nur §e{0} §7Leute einladen§8!", true, true),
    FACTION_PLAYER_JOIN("§3{0} hast die Fraktion §abetreten§8.", true, true),
    FACTION_PLAYER_QUIT("§3{0} hast die Fraktion §cverlassen§8.", true, true),
    FACTION_PLAYER_KICK_SUCCESSFUL("Du hast §3{0} aus der Fraktion gekickt§8!", true, true),
    FACTION_PLAYER_KICK_SELF("Du kannst dich §cnicht §7selbst kicken§8!", true, true),
    FACTION_PLAYER_KICK("§3{0} §7wurde aus §e{1} §7gekickt§8!", true, true),
    FACTION_PLAYER_KICKED("Du wurdest aus der Fraktion §cgekickt§8!", true, true),
    FACTION_LEAVE("Du hats die Fraktion §cverlassen§8!", true, true),
    FACTION_LEAVE_REQUEST("Willst du die Fraktion wirklich §cverlassen§8?", true, true),
    FACTION_LEAVE_REQUEST_REJECT("§7Ok§8, §7dann §cnicht§8.", true, true),
    FACTION_NOT_ENOUGH_PERMISSIONS("Dazu hast du §cnicht §7genügend Rechte§8!", true, true),
    FACTION_TARGET_PLAYER_NOT_FOUND("§3{0} §7ist §cnicht §7in der Fraktiuon§8!", true, true),
    FACTION_PLAYER_PROMOTE_SUCCESS("Du hast §3{0} §7zu §e{1} §7ernannt§8!", true, true),
    FACTION_PLAYER_PROMOTE("§3{0} §7ist nun §3{1}§8.", true, true),
    FACTION_RANK_NOT_FOUND("Der Rang wurde §cnicht §7gefunden8.", true, true),
    FACTION_PLAYER_RANK_NEEDS_CHANGE("Der Spieler hat den Rang schon§8.", true, true),
    FACTION_STATS("Die Fraktion §e{0} §7hat §e{1} §7Kill{2} §7und §e{3} §7Tod{4}§8.", true, true),
    FACTION_PLAYER_BASE_SET("Du hast Base umgesetzt§8.", true, true),
    FACTION_CHUNK_ALREADY_CLAIMED("Dieser Chunk gehört schon einer anderen Fraktion§8!", true, true),
    FACTION_CHUNK_CLAIMED("Der Chunk wurde §agesichert§8!", true, true),
    FACTION_CHUNK_NOT_CLAIMED("Der Chunk wurde §cnicht §7gesichert§8!", true, true),
    FACTION_CHUNK_NOT_OWN("Das ist §cnicht §7dein Chunk§8!", true, true),
    FACTION_CHUNK_MAX("Deine Fraktion kann nur §e{0} §7Chunks haben§8.", true, true),

    CHESTSHOP_UNKNOWN_MATERIAL("Gebe ein gültiges Material an§8.", true, true),
    CHESTSHOP_UNKNOWN_INTEGER("Gebe eine gültige Zahl an §8(§7ohne Komma§8, §7...§8)", true, true),
    CHESTSHOP_AMOUNT_TOO_HIGH("Amount darf §cnicht §7höher als §e64 §7sein§8.", true, true),
    CHESTSHOP_AMOUNT_TOO_LITTLE("§7Amount muss eins oder mehr sein§8.", true, true),
    CHESTSHOP_CREATED("§7Chestshop wurde §aerstellt§8!", true, true),
    CHESTSHOP_DELETED("§7Chestshop wurde §cgelöscht§8!", true, true),
    CHESTSHOP_BUY_OWN("%ERR%Du kannst §cnicht §7in deinem eigenen Shop einkaufen§8!", true, true),
    CHESTSHOP_EMPTY("Dieser Shop ist leer§8!", true, true),
    CHESTSHOP_INVENTORY_FULL("Dein Inventar ist zu §cvoll§8!", true, true),
    CHESTSHOP_TOO_LITTLE_MONEY("Du hast zu §cwenig §7Münzen§8!", true, true),
    CHESTSHOP_PLAYER_BOUGHT("§e{0} §7hat §e{1} §7in einem deiner Shops für §e{2} §7Münzen gekauft§8.", true, true),
    CHESTSHOP_PLAYER_BOUGHT_SUCCESS("Du hast §e{0} §7für §e{1} §7Münzen aus dem Shop von §3{2} §7gekauft§8!", true, true),

    MONEY_UNKNOWN_INTEGER("Gebe eine gültige Zahl an§8!", true, true),
    MONEY_PAY_OWN_TOO_LITTLE("Du hast dafür zu §cwenig §7Geld§8!", true, true),
    MONEY_PAY_TOO_LITTLE("Du musst mindestens §eeine §7Münze angeben§8!", true, true),
    MONEY_PAY_SELF("Du willst dir selbst Geld schenken§8? §7Okay§8...", true, true),
    MONEY_PAY_TIMEOUT("Warte noch kurz§8!", true, true),
    MONEY_PAY_SUCCESS("Du hast §2{0} §e{1} §7Münzen geschenkt§8.", true, true),
    MONEY_PAY_RECEIVED("§3{0} §7hat dir §e{1} §7Münzen geschenkt§8.", true, true),
    MONEY_ADD_SUCCESS("Du hast §3{0} §e{1} §7Münzen hinzugefügt§8!", true, true),
    MONEY_REMOVE_SUCCESS("Du hast §3{0} §e{1} §7Münzen genommen§8!", true, true),

    JOB_LEVEL_ACHIEVED("§7Du bist in deinem Job §e{0} §7nun Level §3{1}§8!", true, true),

    TPA_TIMEOUT("Warte noch§8!", true, true),
    TPA_REQUEST("§3{0} will zu dir§8! §7Nehme die Anfrage mit §a/tpa accept {0} §7an oder lehne sie mit §c/tpa deny {0} §7ab§8!", true, true),
    TPA_REQUEST_SENT("Du willst zu §3{0}§8!", true, true),
    TPA_REQUEST_ALREADY_SENT("Du hast diesem Spieler schon eine Anfrage geschickt§8!", true, true),
    TPA_REQUEST_ACCEPT("Du hast die Anfrage von §3{0} §aakzeptiert§8!", true, true),
    TPA_REQUEST_ACCEPTED("§3{0} §7hat deine Anfrage §aakzeptiert§8!", true, true),
    TPA_REQUEST_PLAYER_OFFLINE("Der Spieler ist §cnicht §7mehr online§8!", true, true),
    TPA_NO_REQUESTS_FROM("Der will §cnicht §7zu dir§8!", true, true),

    TRADE_TIMEOUT("Warte bitte einen Moment§8.", true, true),
    TRADE_REQUEST("§3{0} §7will mit dir handeln§8.", true, true),
    TRADE_REQUEST_SENT("§7Du willst mit §3{0} §7handeln§8.", true, true),
    TRADE_REQUEST_ALREADY_SENT("Du willst mit ihm §abereits §7handeln§8.", true, true),
    TRADE_REQUEST_ACCEPT("Du hast die §eHandels-Anfrage §7von §3{0} §7akzeptiert§8.", true, true),
    TRADE_REQUEST_ACCEPTED("§3{0} §7hat deine Handels-Anfrage §aakzeptiert§8.", true, true),
    TRADE_REQUEST_PLAYER_OFFLINE("§7Der Spieler ist §cnicht §7mehr online§8.", true, true),
    TRADE_NO_REQUESTS_FROM("Dieser Spieler will mit dir §cnicht §7handeln§8.", true, true),

    CHUNK_SETTINGS_BUILD_CANCEL("Hier kannst du §cnicht §7bauen§8, §7da dieser Chunk einer anderen Fraktion gehört§8!", true, true),

    PLAYER_JOIN("§3{0} §7hat Factions §abetreten§8!", true, true),
    PLAYER_QUIT("§3{0} §7hat Factions §cverlassen§8!", true, true),

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
