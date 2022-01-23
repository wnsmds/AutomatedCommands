package data.hullmods;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipCommand;

import java.util.Map;

public class BaseVenting extends AutomatedHullMod {
    private static final float THRESHOLD = 0.1f;

    private final float limit;
    private final String limitText;

    public BaseVenting() {
        limit = 0.8f;
        limitText = Util.percentToString(limit);
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (Global.getCurrentState() != GameState.COMBAT) return;

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
        if (ship.getFluxLevel() > limit) {
            ship.giveCommand(ShipCommand.VENT_FLUX, null, 0);
            customData.put(tag, tag);
        }
    }

    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        if (index == 0)
            return limitText;
        return null;
    }
}
