package data.hullmods.retreat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.mission.FleetSide;
import data.hullmods.AutomatedHullMod;

public abstract class BaseRetreat extends AutomatedHullMod {
    protected static final Object TOKEN = "";

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
            formatShipMessage(ship, reason);

            DeployedFleetMemberAPI member = fleetManager.getDeployedFleetMember(ship);
            taskManager.orderRetreat(member, false, false);
        }
    }
}