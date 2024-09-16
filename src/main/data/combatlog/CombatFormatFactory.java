package data.combatlog;

import com.fs.starfarer.api.Global;

import java.awt.*;

public class CombatFormatFactory {
    static final Color TEXT_COLOR = Global.getSettings().getColor("standardTextColor");
    static final Color FRIEND_COLOR = Global.getSettings().getColor("textFriendColor");
    static final Color ENEMY_COLOR = Global.getSettings().getColor("textEnemyColor");
    static final Color HIGHLIGHT_COLOR = Color.CYAN;

    private static final CombatFormatFactory DEFAULT = CombatFormatFactory.custom(TEXT_COLOR);
    private static final CombatFormatFactory PRETTY = CombatFormatFactory.custom(HIGHLIGHT_COLOR);

    private final Color textColor;
    private final Color friendColor;
    private final Color enemyColor;
    private final Color highlightColor;

    private CombatFormatFactory(Color highlightColor, Color textColor, Color friendColor, Color enemyColor) {
        this.highlightColor = highlightColor;
        this.textColor = textColor;
        this.friendColor = friendColor;
        this.enemyColor = enemyColor;
    }

    // A combat messenger with the basic color scheme as defined by Starsector
    public static CombatFormatFactory simple() {
        return CombatFormatFactory.DEFAULT;
    }
    // A combat messenger with CYAN added as a highlight color added to the standard color scheme
    public static CombatFormatFactory pretty() {
        return CombatFormatFactory.PRETTY;
    }
    // A combat messenger with a custom highlight color added to the standard color scheme
    public static CombatFormatFactory custom(Color highlightColor) {
        return new CombatFormatFactory(highlightColor,TEXT_COLOR,FRIEND_COLOR,ENEMY_COLOR);
    }
}
