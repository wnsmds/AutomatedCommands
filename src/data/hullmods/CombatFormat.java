package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.DeployedFleetMemberAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.mission.FleetSide;

import java.awt.*;

public class CombatFormat {
    private final Color textColor;
    private final Color friendColor;
    private final Color enemyColor;
    private final Color highlightColor;

    private final Object[] arguments;

    private CombatFormat(String pattern) {
        textColor = CombatFormatFactory.TEXT_COLOR;
        friendColor = CombatFormatFactory.FRIEND_COLOR;
        enemyColor = CombatFormatFactory.ENEMY_COLOR;
        highlightColor = CombatFormatFactory.HIGHLIGHT_COLOR;

        arguments = parse(pattern);
    }
    private Object[] parse(String pattern) {
        return null;
    }

    protected static String getShipName(ShipAPI ship) {
        // TODO does 'getHullNameWithDashClass()' already discriminate for fighters
        if (ship.isFighter()) {
            return ship.getHullSpec().getHullName() + " wing";
        }
        return ship.getName() + " (" + ship.getHullSpec().getHullNameWithDashClass() + ")";
    }
    protected void formatShipMessage(ShipAPI ship, String reason) {
        //LOGGER.info(ship.getName() + " - " + reason);

        DeployedFleetMemberAPI deployedMember
                = Global.getCombatEngine().getFleetManager(FleetSide.PLAYER).getDeployedFleetMember(ship);
        if (deployedMember == null) return;
        String shipName = getShipName(ship);
        Object[] message = new Object[]{
                deployedMember,
                friendColor, shipName,
                textColor, ": ",
                highlightColor, reason };
        Global.getCombatEngine().getCombatUI().addMessage(1, message);
    }
}
