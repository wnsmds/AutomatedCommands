package data.hullmods.retreat.hulldamage;

import com.fs.starfarer.api.combat.ShipAPI;
import data.hullmods.retreat.BaseRetreat;
import data.hullmods.Util;

public class BaseHullDamage extends BaseRetreat {
    protected final float threshold;
    protected final String thresholdText;

    protected BaseHullDamage(float threshold) {
        this.threshold = threshold;
        thresholdText = Util.percentage(threshold);
    }

    @Override
    protected boolean triggerCondition(ShipAPI ship) {
        return (ship.getHullLevel() < threshold);
    }

    @Override
    protected String message(ShipAPI ship) {
        return String.format("hull integrity is %s", Util.percentage(ship.getHullLevel()));
    }

    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        if (index == 0)
            return thresholdText;
        return null;
    }
}
