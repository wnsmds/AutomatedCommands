package data.hullmods;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipCommand;
import com.fs.starfarer.combat.CombatEngine;

import java.util.Map;

public class BaseVentingHullMod extends BaseAutomatedHullMod {
    private static final float THRESHOLD = 0.1f;

    private final float limit;
    private final String limitText;

    public BaseVentingHullMod() {
        limit = 0.8f;
        limitText = (int)(limit*100) + "%";
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (Global.getCurrentState() != GameState.COMBAT) return;
        //if (ship.getCaptain().isPlayer()) return;

        String tag = ship.getId() + "_venting" + limitText;
        CombatEngineAPI engine = Global.getCombatEngine();
        Map<String, Object> customData = engine.getCustomData();
        if (customData.containsKey(tag)) {
            if (ship.getFluxLevel() < THRESHOLD) {
                customData.remove(tag);
            }
            return;
        }
        // terminating condition put below tag removal to prevent player turning off autopilot during venting
        // from locking the player ship out of this method
        if (ship == engine.getPlayerShip() && ship.getShipAI() == null) return;
        //if (/*ship.getCaptain() != null &&*/ ship.getCaptain().isPlayer() && ship.getShipAI() == null) return;
        if (ship.getFluxLevel() > limit) {
            //formatShipMessage(ship, "venting at flux lvl " + Util.percentage(ship.getFluxLevel()));
            ship.giveCommand(ShipCommand.VENT_FLUX, null, 0);
            customData.put(tag,tag);
        }
    }
    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        switch (index) {
            case 0: return Util.percentage(limit);
            default: return null;
        }
    }
}
