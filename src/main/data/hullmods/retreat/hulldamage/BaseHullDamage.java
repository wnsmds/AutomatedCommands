package data.hullmods.retreat.hulldamage;

import com.fs.starfarer.api.combat.ShipAPI;
import data.hullmods.retreat.BaseRetreat;
import data.combatlog.Util;

import java.text.MessageFormat;

public class BaseHullDamage extends BaseRetreat {
    protected static final MessageFormat DAMAGE_RETREAT_MESSAGE = Util.resolveSubstitutions(Util.MOD_KEY + ":DAMAGE_RETREAT_MESSAGE");
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
        return DAMAGE_RETREAT_MESSAGE.format(new Object[]{ship, ship.getHullLevel()});
    }

    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        if (index == 0)
            return thresholdText;
        return null;
    }
}
