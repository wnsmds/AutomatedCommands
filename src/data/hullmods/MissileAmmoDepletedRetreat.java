package data.hullmods;

import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;

import java.util.Scanner;

public class MissileAmmoDepletedRetreat extends BaseRetreatHullMod {
    @Override
    protected boolean triggerCondition(ShipAPI ship) {
        for (WeaponAPI weapon : ship.getAllWeapons()) {
            if (weapon.getType() == WeaponAPI.WeaponType.MISSILE) {
                if (weapon.getAmmo() > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected String message(ShipAPI ship) {
        return "has no missile left";
    }

    @Override
    public String getUnapplicableReason(ShipAPI ship) {
        return "Ship has no missiles installed";
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
        switch (index) {
            case 0: return "no missile";
            default: return null;
        }
    }
}
