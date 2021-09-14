package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.DeployedFleetMemberAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.mission.FleetSide;
import org.apache.log4j.Logger;

import java.awt.Color;

public abstract class BaseAutomatedHullMod extends BaseHullMod {
    private static final Logger LOGGER = Global.getLogger(BaseAutomatedHullMod.class);

    private static final Color TEXT_COLOR = Global.getSettings().getColor("standardTextColor");
    private static final Color FRIEND_COLOR = Global.getSettings().getColor("textFriendColor");
    private static final Color MESSAGE_COLOR = Color.CYAN;
    //private static final Color ENEMY_COLOR = Global.getSettings().getColor("textEnemyColor");

    protected static String getShipName(FleetMemberAPI member) {
        // TODO does 'getHullNameWithDashClass()' already discriminate for fighters
        if (member.isFighterWing()) {
            return member.getHullSpec().getHullName() + " wing";
        }
        return member.getShipName() + " (" + member.getHullSpec().getHullNameWithDashClass() + ")";
    }
    protected static void formatShipMessage(ShipAPI ship, String reason) {
        LOGGER.info(ship.getName() + " - " + reason);

        DeployedFleetMemberAPI deployedMember
                = Global.getCombatEngine().getFleetManager(FleetSide.PLAYER).getDeployedFleetMember(ship);
        if (deployedMember == null) return;
        String shipName = getShipName(deployedMember.getMember());
        Object[] message = new Object[]{
                deployedMember,
                FRIEND_COLOR, shipName,
                TEXT_COLOR, ": ",
                MESSAGE_COLOR, reason };
        Global.getCombatEngine().getCombatUI().addMessage(1, message);
    }
}
