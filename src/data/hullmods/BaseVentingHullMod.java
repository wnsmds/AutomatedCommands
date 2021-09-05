package data.hullmods;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipCommand;

public class BaseVentingHullMod extends BaseAutomatedHullMod {
    private static final float THRESHOLD = 0.1f;

    private final float LIMIT;
    private boolean executing;

    public BaseVentingHullMod() {
        LIMIT = 0.8f;
        executing = false;
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (Global.getCurrentState() != GameState.COMBAT) return;
        if (executing) {
            if (ship.getFluxLevel() < THRESHOLD) {
                executing = false;
            }
            return;
        }
        if (ship.getFluxLevel() > LIMIT) {
            //formatShipMessage(ship, "venting at flux lvl " + (int)(ship.getFluxLevel()*100) + "%");
            ship.giveCommand(ShipCommand.VENT_FLUX, null, 0);
            executing = true;
        }
    }
}
