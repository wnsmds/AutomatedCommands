package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.DeployedFleetMemberAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.mission.FleetSide;

import java.awt.*;

public class CombatMessenger {
    private static final Color TEXT_COLOR = Global.getSettings().getColor("standardTextColor");
    private static final Color FRIEND_COLOR = Global.getSettings().getColor("textFriendColor");
    private static final Color ENEMY_COLOR = Global.getSettings().getColor("textEnemyColor");
    private static final Color HIGHLIGHT_COLOR = Color.CYAN;

    private static final CombatMessenger DEFAULT = CombatMessenger.custom(TEXT_COLOR);
    private static final CombatMessenger PRETTY = CombatMessenger.custom(HIGHLIGHT_COLOR);

    private final Color textColor;
    private final Color friendColor;
    private final Color enemyColor;
    private final Color highlightColor;

    private CombatMessenger(Color highlightColor, Color textColor,Color friendColor,Color enemyColor) {
        this.highlightColor = highlightColor;
        this.textColor = textColor;
        this.friendColor = friendColor;
        this.enemyColor = enemyColor;
    }
    // A combat messenger with the basic color scheme as defined by Starsector
    public static CombatMessenger simple() {
        return CombatMessenger.DEFAULT;
    }
    // A combat messenger with CYAN added as a highlight color added to the standard color scheme
    public static CombatMessenger pretty() {
        return CombatMessenger.PRETTY;
    }
    // A combat messenger with a custom highlight color added to the standard color scheme
    public static CombatMessenger custom(Color highlightColor) {
        return new CombatMessenger(highlightColor,TEXT_COLOR,FRIEND_COLOR,ENEMY_COLOR);
    }
    public void format(String text, Object... objects) {

    }


    private static String getShipName(FleetMemberAPI member) {
        // TODO does 'getHullNameWithDashClass()' already discriminate for fighters
        if (member.isFighterWing()) {
            return member.getHullSpec().getHullName() + " wing";
        }
        return member.getShipName() + " (" + member.getHullSpec().getHullNameWithDashClass() + ")";
    }
    protected static void formatShipMessage(ShipAPI ship, String reason) {
        //LOGGER.info(ship.getName() + " - " + reason);

        DeployedFleetMemberAPI deployedMember
                = Global.getCombatEngine().getFleetManager(FleetSide.PLAYER).getDeployedFleetMember(ship);
        if (deployedMember == null) return;
        String shipName = getShipName(deployedMember.getMember());
        Object[] message = new Object[]{
                deployedMember,
                FRIEND_COLOR, shipName,
                TEXT_COLOR, ": ",
                HIGHLIGHT_COLOR, reason };
        Global.getCombatEngine().getCombatUI().addMessage(1, message);
    }
}
