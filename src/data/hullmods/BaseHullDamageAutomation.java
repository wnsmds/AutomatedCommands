package data.hullmods;

import com.fs.starfarer.api.combat.ShipAPI;

public class BaseHullDamageAutomation extends BaseRetreatHullMod {
    protected final float threshold;

    protected BaseHullDamageAutomation(float threshold) {
        this.threshold = threshold;
    }
    @Override
    protected boolean triggerCondition(ShipAPI ship) {
        return  (ship.getHullLevel() < threshold);
    }
    @Override
    protected String message(ShipAPI ship) {
        return String.format("hull integrity is %d%%", (int)(ship.getHullLevel() * 100f));
    }
}
