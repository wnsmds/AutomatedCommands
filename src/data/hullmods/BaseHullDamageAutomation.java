package data.hullmods;

import com.fs.starfarer.api.combat.ShipAPI;

public class BaseHullDamageAutomation extends BaseRetreatHullMod {
    protected final float threshold;
    protected final String thresholdText;

    protected BaseHullDamageAutomation(float threshold) {
        this.threshold = threshold;
        thresholdText = Util.percentage(threshold);
    }
    @Override
    protected boolean triggerCondition(ShipAPI ship) {
        return  (ship.getHullLevel() < threshold);
    }
    @Override
    protected String message(ShipAPI ship) {
        return String.format("hull integrity is %s", Util.percentage(ship.getHullLevel()));
    }
    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        switch (index) {
            case 0: return thresholdText;
            default: return null;
        }
    }
}
