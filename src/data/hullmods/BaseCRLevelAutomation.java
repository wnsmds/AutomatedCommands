package data.hullmods;

import com.fs.starfarer.api.combat.ShipAPI;

public class BaseCRLevelAutomation extends BaseRetreatHullMod {
    protected final float threshold;

    protected BaseCRLevelAutomation(float threshold) {
        this.threshold = threshold;
    }
    @Override
    protected boolean triggerCondition(ShipAPI ship) {
        return (ship.getCurrentCR() < ship.getCRAtDeployment() && ship.getCurrentCR() <= threshold);
    }

    @Override
    protected String message(ShipAPI ship) {
        return String.format("combat readiness is %d%%", (int)(ship.getCurrentCR() * 100f));
    }
}
