package data.hullmods.retreat;

import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import data.combatlog.Util;

import java.text.MessageFormat;

public class MissileAmmoDepleted extends BaseRetreat {
    protected static final MessageFormat MISSILE_INAPPLICABLE_REASON = Util.resolveSubstitutions(Util.MOD_KEY + ":MISSILE_INAPPLICABLE_REASON");
    protected static final MessageFormat MISSILE_RETREAT_MESSAGE = Util.resolveSubstitutions(Util.MOD_KEY + ":MISSILE_RETREAT_MESSAGE");
    protected static final MessageFormat MISSILE = Util.resolveSubstitutions(Util.MOD_KEY + ":MISSILE");

    @Override
    protected boolean triggerCondition(ShipAPI ship) {
        for (WeaponAPI weapon : ship.getAllWeapons()) {
            if (weapon.getType() == WeaponAPI.WeaponType.MISSILE && weapon.getAmmo() > 0) {
                    return false;
            }
        }
        return true;
    }

    @Override
    protected String message(ShipAPI ship) {
        return MISSILE_RETREAT_MESSAGE.format(new Object[]{ship});
    }

    @Override
    public String getUnapplicableReason(ShipAPI ship) {
        return MISSILE_INAPPLICABLE_REASON.format(new Object[]{ship});
    }

    @Override
    public boolean isApplicableToShip(ShipAPI ship) {
        for (WeaponAPI weapon : ship.getAllWeapons()) {
            if (weapon.getType() == WeaponAPI.WeaponType.MISSILE) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        if (index == 0)
            return MISSILE.format(null);
        return null;
    }
}
