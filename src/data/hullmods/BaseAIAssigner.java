package data.hullmods;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.ShipAIConfig;
import com.fs.starfarer.api.combat.ShipAIPlugin;
import com.fs.starfarer.api.combat.ShipAPI;

public class BaseAIAssigner extends BaseAutomatedHullMod {
    private static final String PREFIX = "automated_personality_";
    enum Personality {
        TIMID,
        CAUTIOUS,
        STEADY,
        AGGRESSIVE,
        RECKLESS;
        final String id;
        final String desc;
        Personality() {
            desc = this.name().toLowerCase();
            id = PREFIX + desc;
        }
    }

    protected static final String TOKEN = "";

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
        return "has been assigned a " + personality.desc + " personality.";
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
        ShipAIPlugin ai = ship.getShipAI();
        if (ai == null) {
            ShipAIConfig config = new ShipAIConfig();
            Global.getSettings().createDefaultShipAI(ship,config);
            formatShipMessage(ship, message(ship) + " (By Default)");
            return; // Ship is not currently autopilotted
        }
        ai.getConfig().personalityOverride = personality.desc;
        formatShipMessage(ship, message(ship));

    }
}
