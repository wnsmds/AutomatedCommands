package data.hullmods;

import com.fs.starfarer.api.combat.ShipAPI;

public class BaseAIAssigner extends BaseAutomatedHullMod {
    enum Personality {
        TIMID,
        CAUTIOUS,
        STEADY,
        AGGRESSIVE,
        RECKLESS;
        final String id;
        Personality() {
            id = PREFIX + this.name().toLowerCase();
        }
    }

    private static final String PREFIX = "automated_personality_";
    private static final String TAG = "personality";

    private final Personality personality;

    public BaseAIAssigner(Personality personality) {
        this.personality = personality;
    }

    @Override
    public String getUnapplicableReason(ShipAPI ship) {
        return "Ship may only have 1 personality hullmod";
    }

    @Override
    public boolean isApplicableToShip(ShipAPI ship) {
        for (String hullmod : ship.getVariant().getHullMods()) {
            if (hullmod.startsWith(PREFIX) && !hullmod.equals(personality.id)) {
                return false;
            }
        }
        return true;
    }
    protected String message(ShipAPI ship) {
        return "has been assigned a " + personality.name().toLowerCase() + " personality.";
    }
    /*@Override
    public boolean isApplicableToShip(ShipAPI ship) {
        LOGGER.info(ship.getVariant().getTags());
        return (!ship.getVariant().getTags().contains(TAG));
    }*/
}
