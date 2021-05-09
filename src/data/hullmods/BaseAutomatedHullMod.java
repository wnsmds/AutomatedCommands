package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.mission.FleetSide;
import org.apache.log4j.Logger;

import java.awt.*;

public abstract class BaseAutomatedHullMod extends BaseHullMod {
    private static final Object TOKEN = "";
    private static final Logger LOGGER = Global.getLogger(BaseAutomatedHullMod.class);

    private static final Color TEXT_COLOR = Global.getSettings().getColor("standardTextColor");
    private static final Color FRIEND_COLOR = Global.getSettings().getColor("textFriendColor");
    //private static final Color ENEMY_COLOR = Global.getSettings().getColor("textEnemyColor");
    private static final Color MESSAGE_COLOR = Color.CYAN;

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
    /*
    private Set<String> getCustomData() {
        Object temp = Global.getCombatEngine().getCustomData().get(this.getClass().getName());
        Set<String> data;
        if (temp == null) {
            data = new HashSet<>();
            Global.getCombatEngine().getCustomData().put(this.getClass().getName(),data);
            return data;
        } else if (temp instanceof Set<?>){
            return (Set<String>)temp;
        }
        throw new IllegalStateException(temp.getClass() + " is not a member of Set<String> and cannot be cast");
    }*/
    private static void orderRetreat(ShipAPI ship, String reason) {
        CombatFleetManagerAPI fleetManager = Global.getCombatEngine().getFleetManager(FleetSide.PLAYER);
        CombatTaskManagerAPI taskManager = fleetManager.getTaskManager(false);
        CombatFleetManagerAPI.AssignmentInfo assignment = taskManager.getAssignmentFor(ship);
        if (assignment == null || assignment.getType() != CombatAssignmentType.RETREAT) {
            DeployedFleetMemberAPI member = fleetManager.getDeployedFleetMember(ship);
            //if (member == null) return;
            LOGGER.info(ship.getName() + " retreating (" + reason + ")");
            String message = reason + " - retreating ...";
            addShipMessage(member.getMember(), MESSAGE_COLOR, message);
            taskManager.orderRetreat(member, false, false);
        }
    }
    private static String getShipName(FleetMemberAPI member) {
        if (member.isFighterWing()) {
            return member.getHullSpec().getHullName() + " wing";
        }
        return member.getShipName() + " (" + member.getHullSpec().getHullName() + "-class)";
    }
    private static void addShipMessage(FleetMemberAPI member, Object... params) {
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
    /*
    @Override
    public void init(HullModSpecAPI spec) {
        LOGGER.info("Automated Commands mod initialized");
    }
     */
}
