package data.hullmods.carrier;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import data.hullmods.AutomatedHullMod;
import data.combatlog.Util;

import java.text.MessageFormat;

public class BaseRegroup extends AutomatedHullMod {
    protected static final String REGROUP = Global.getSettings().getString(Util.MOD_KEY, "REGROUP");
    protected static final String ENGAGE = Global.getSettings().getString(Util.MOD_KEY, "ENGAGE");
    protected static final MessageFormat REGROUP_INAPPLICABLE = Util.resolveSubstitutions(Util.MOD_KEY + ":REGROUP_INAPPLICABLE");

    //private static final String NO_WINGS = "NO_WINGS";

    private static final float THRESHOLD = 0.8f;
    private static final String THRESHOLD_TEXT = Util.percentToString(THRESHOLD);

    private final float limit;
    private final String limitText;

    public BaseRegroup() {
        limit = 0.2f;
        limitText = Util.percentToString(limit);
    }

    @Override
    public boolean isApplicableToShip(ShipAPI ship) {
        return ship.hasLaunchBays();
    }

    @Override
    public String getUnapplicableReason(ShipAPI ship) {
        return REGROUP_INAPPLICABLE.format(new Object[]{ship});
    }

    /*private String generateTag(ShipAPI ship) {
        return ship.getId() + "_regroup" + limitText;
    }*/

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (Global.getCurrentState() != GameState.COMBAT) return;

        float rate = calculateReplacementRate(ship);
        if (rate > 1.0f) return; //there are no fighter wings installed in the carrier

        if (rate > THRESHOLD) {
            ship.setPullBackFighters(false);
        } else if (rate < limit) {
            ship.setPullBackFighters(true);
        }
    }

    private float calculateReplacementRate(ShipAPI ship) {
        float rate = 0.0f;
        int count = 0;

        for (FighterLaunchBayAPI bay : ship.getLaunchBaysCopy()) {
            if (bay.getWing() == null) continue;
            rate += bay.getCurrRate();
            count++;
        }

        if (count == 0) {
            return 2.0f; //there are no fighter wings installed in the carrier
        }
        return rate / count;
    }

    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        switch (index) {
            case 0:
                return REGROUP;
            case 1:
                return limitText;
            case 2:
                return ENGAGE;
            case 3:
                return THRESHOLD_TEXT;
            default:
                return null;
        }
    }
}
