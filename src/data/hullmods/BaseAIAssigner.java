package data.hullmods;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.ShipAIConfig;
import com.fs.starfarer.api.combat.ShipAPI;

import java.util.Objects;

public class BaseAIAssigner extends BaseAutomatedHullMod {
    private static final String PREFIX = "automated_personality_";
    enum Personality {
        TIMID,
        CAUTIOUS,
        STEADY,
        AGGRESSIVE,
        RECKLESS;
        final String id;
        final String value;
        Personality() {
            value = this.name().toLowerCase();
            id = PREFIX + value;
        }
        String withArticle() {
            return withArticle(this);
        }
        static String withArticle(Personality personality) {
            return ((personality == AGGRESSIVE)?"an ":"a ") + personality.value;
        }
    }

    protected static final String TOKEN = "";

    private final Personality personality;

    public BaseAIAssigner(Personality personality) {
        Objects.requireNonNull(personality);
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
    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        switch (index) {
            case 0: return personality.value;
            default: return null;
        }
    }
    protected String message(ShipAPI ship) {
        return "has been assigned " + personality.withArticle() + " personality.";
    }
    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (Global.getCurrentState() != GameState.COMBAT) return;
        String tag = ship.getId() + "_" + personality.id;
        if (Global.getCombatEngine().getCustomData().containsKey(tag)) return;
        Global.getCombatEngine().getCustomData().put(tag, TOKEN);
        overrideAI(ship);
    }
    protected void overrideAI(ShipAPI ship) {
        PersonAPI captain = ship.getCaptain();
        if (/*captain == null ||*/ !captain.isDefault()) return; //Ship has officer, do not apply customAI
        //if (captain.isPlayer()) return; // Ship has no ShipAI
        if (ship.getShipAI() == null) {
            ShipAIConfig config = new ShipAIConfig();
            Global.getSettings().createDefaultShipAI(ship,config);
            formatShipMessage(ship, message(ship) + " (By default)");
            return; // Ship is not currently autopilotted
        }
        ship.getShipAI().getConfig().personalityOverride = personality.value;
        formatShipMessage(ship, message(ship));

    }
}
