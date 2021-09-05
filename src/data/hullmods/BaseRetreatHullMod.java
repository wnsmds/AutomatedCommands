package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.mission.FleetSide;
import org.apache.log4j.Logger;

import java.awt.*;

public abstract class BaseRetreatHullMod extends BaseAutomatedHullMod {
    private static final Object TOKEN = "";
    //private static final Logger LOGGER = Global.getLogger(BaseRetreatHullMod.class);

    protected abstract boolean triggerCondition(ShipAPI ship);
    protected abstract String message(ShipAPI ship);

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        final String key = ship.getId() + "_" + this.getClass().getName();
        if (Global.getCombatEngine().getCustomData().containsKey(key)) return;
        if (triggerCondition(ship)) {
            Global.getCombatEngine().getCustomData().put(key, TOKEN);
            orderRetreat(ship, message(ship));
        }
    }

    private static void orderRetreat(ShipAPI ship, String reason) {
        CombatFleetManagerAPI fleetManager = Global.getCombatEngine().getFleetManager(FleetSide.PLAYER);
        CombatTaskManagerAPI taskManager = fleetManager.getTaskManager(false);
        CombatFleetManagerAPI.AssignmentInfo assignment = taskManager.getAssignmentFor(ship);
        if (assignment == null || assignment.getType() != CombatAssignmentType.RETREAT) {
            formatShipMessage(ship, reason + " - retreating ...");

            DeployedFleetMemberAPI member = fleetManager.getDeployedFleetMember(ship);
            taskManager.orderRetreat(member, false, false);
        }
    }
    /*
    @Override
    public void init(HullModSpecAPI spec) {
        LOGGER.info("Automated Commands mod initialized");
    }
     */
}
