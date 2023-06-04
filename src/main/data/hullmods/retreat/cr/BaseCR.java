package data.hullmods.retreat.cr;

import com.fs.starfarer.api.combat.ShipAPI;
import data.hullmods.retreat.BaseRetreat;
import data.combatlog.Util;

public class BaseCR extends BaseRetreat {
    protected final float threshold;

    // TODO add a retreat before CR starts reducing
    //  ship.getHullSpec().getNoCRLossTime();
    //  stats.getPeakCRDuration().computeEffective(hullspec.getNoCRLossTime()) - ship.getTimeDeployedForCRReduction()
    protected BaseCR(float threshold) {
        this.threshold = threshold;
    }

    @Override
    protected boolean triggerCondition(ShipAPI ship) {
        return (ship.getCurrentCR() < ship.getCRAtDeployment() && ship.getCurrentCR() <= threshold);
    }

    @Override
    protected String message(ShipAPI ship) {
        return String.format("combat readiness is %d%%", (int) (ship.getCurrentCR() * 100f));
    }

    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        if (index == 0)
            return Util.percentage(threshold);
        return null;
    }
}
