package data.hullmods.carrier;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import data.hullmods.AutomatedHullMod;
import data.hullmods.Util;

import java.text.MessageFormat;
import java.util.Map;

public class BaseRegroup extends AutomatedHullMod {
    private static final float THRESHOLD = 0.8f;
    private static final String THRESHOLD_TEXT = Util.percentToString(THRESHOLD);

    private static final String REGROUP = Global.getSettings().getString(Util.MOD_KEY,"REGROUP");
    private static final String ENGAGE = Global.getSettings().getString(Util.MOD_KEY,"ENGAGE");
    private static final MessageFormat REGROUP_INAPPLICABLE = Util.resolveSubstitutions(Util.MOD_KEY +":REGROUP_INAPPLICABLE");

    private final float limit;
    private final String limitText;

    public BaseRegroup() {
        limit = 0.2f;
        limitText = Util.percentToString(limit);
    }
    @Override
    public boolean isApplicableToShip(ShipAPI ship) {
        if (!ship.hasLaunchBays()) return false;
        //return !shipHasOtherModInCategory(ship, personality.id, CATEGORY);
        return true;
    }

    @Override
    public String getUnapplicableReason(ShipAPI ship) {
        return REGROUP_INAPPLICABLE.format(new Object[]{ship});
    }

    //TODO make invalid without wings
    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (Global.getCurrentState() != GameState.COMBAT) return;

        float rate = 0.0f;
        int count = 0;

        for (FighterLaunchBayAPI bay: ship.getLaunchBaysCopy()) {
            if (bay.getWing() == null) continue;
            rate += bay.getCurrRate();
            count++;
        }
        //Assume count is not 0 because hullmod is invalid without wings
        //TODO add an ignore flag for carriers with the mod that have no installed wings
        if (count == 0) return;
        rate/=count;

        String tag = ship.getId() + "_regroup" + limitText;
        CombatEngineAPI engine = Global.getCombatEngine();
        Map<String, Object> customData = engine.getCustomData();
        if (customData.containsKey(tag)) {
            if (rate > THRESHOLD) {
                customData.remove(tag);
                ship.setPullBackFighters(false);
            }
        } else {
            if (rate < limit) {
                customData.put(tag,tag);
                ship.setPullBackFighters(true);
            }
        }
    }

    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        switch (index) {
            case 0: return REGROUP;
            case 1: return limitText;
            case 2: return ENGAGE;
            case 3: return THRESHOLD_TEXT;
            default: return null;
        }
    }
}
