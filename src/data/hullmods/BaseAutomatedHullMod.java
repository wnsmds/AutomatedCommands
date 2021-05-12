package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatFleetManagerAPI;
import com.fs.starfarer.api.combat.DeployedFleetMemberAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.mission.FleetSide;
import org.apache.log4j.Logger;

import java.awt.*;

public class BaseAutomatedHullMod extends BaseHullMod {
    private static final Logger LOGGER = Global.getLogger(BaseAutomatedHullMod.class);

    private static final Color TEXT_COLOR = Global.getSettings().getColor("standardTextColor");
    private static final Color FRIEND_COLOR = Global.getSettings().getColor("textFriendColor");
    private static final Color MESSAGE_COLOR = Color.CYAN;
    //private static final Color ENEMY_COLOR = Global.getSettings().getColor("textEnemyColor");

    protected static String getShipName(FleetMemberAPI member) {
        if (member.isFighterWing()) {
            return member.getHullSpec().getHullName() + " wing";
        }
        return member.getShipName() + " (" + member.getHullSpec().getHullName() + "-class)";
    }
    protected static void addShipMessage(FleetMemberAPI member, Object... params) {
        /*Global.getCombatEngine().getCombatUI().addMessage(1,
                member,
                FRIEND_COLOR, getShipName(member),
                TEXT_COLOR, ": ",
                params);*/
        Object[] prefix = new Object[]{
                member,
                FRIEND_COLOR, getShipName(member),
                TEXT_COLOR, ": "};
        Object[] all = new Object[prefix.length + params.length];
        System.arraycopy(prefix, 0, all, 0, prefix.length);
        System.arraycopy(params, 0, all, prefix.length, params.length);
        Global.getCombatEngine().getCombatUI().addMessage(1, all);
    }
    protected static void formatShipMessage(ShipAPI ship, String reason) {
        LOGGER.info(ship.getName() + " - " + reason);

        DeployedFleetMemberAPI deployedMember
                = Global.getCombatEngine().getFleetManager(FleetSide.PLAYER).getDeployedFleetMember(ship);
        String shipName = getShipName(deployedMember.getMember());
        Object[] message = new Object[]{
                deployedMember,
                FRIEND_COLOR, shipName,
                TEXT_COLOR, ": ",
                MESSAGE_COLOR, reason,
                " ..."};
        Global.getCombatEngine().getCombatUI().addMessage(1, message);
    }
}
