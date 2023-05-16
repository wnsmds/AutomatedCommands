package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.DeployedFleetMemberAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.mission.FleetSide;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CombatFormat {
    // \{\d+(((?:,\w+)(?:,[\w\.#]+)?)?|,choice,.+(?:\|.+)+)\}
    private static final Pattern formatting = Pattern.compile("\\{\\d+,(color|ship),\\w+}");
    private final Color textColor;
    private final Color friendColor;
    private final Color enemyColor;
    private final Color highlightColor;

    private final Object[] arguments;

    private CombatFormat(String pattern) {
        textColor = CombatFormatFactory.TEXT_COLOR;
        friendColor = CombatFormatFactory.FRIEND_COLOR;
        enemyColor = CombatFormatFactory.ENEMY_COLOR;
        highlightColor = CombatFormatFactory.HIGHLIGHT_COLOR;

        arguments = parse(pattern);
    }
    private Object[] parse(String pattern) {
        List<Object> list = new ArrayList<>();
        int start = 0;

        Matcher matcher = formatting.matcher(pattern);
        while (matcher.find()) {
            int end = matcher.regionStart();
            list.add(pattern.substring(start, end));

            start = matcher.regionEnd();
            list.add(pattern.substring(end, start));
        }
        list.add(pattern.substring(start));

        return list.toArray();
    }
    @Override
    public String toString() {
        return Arrays.toString(arguments);
    }
    public static CombatFormat of(String pattern) {
        return new CombatFormat(pattern);
    }

    protected static String getShipName(ShipAPI ship) {
        // TODO does 'getHullNameWithDashClass()' already discriminate for fighters
        if (ship.isFighter()) {
            return ship.getHullSpec().getHullName() + " wing";
        }
        return ship.getName() + " (" + ship.getHullSpec().getHullNameWithDashClass() + ")";
    }
    protected void formatShipMessage(ShipAPI ship, String reason) {
        //LOGGER.info(ship.getName() + " - " + reason);

        DeployedFleetMemberAPI deployedMember
                = Global.getCombatEngine().getFleetManager(FleetSide.PLAYER).getDeployedFleetMember(ship);
        if (deployedMember == null) return; //ship already undeployed but still has action in queue
        String shipName = getShipName(ship);
        Object[] message = new Object[]{
                deployedMember,
                friendColor, shipName,
                textColor, ": ",
                highlightColor, reason };
        Global.getCombatEngine().getCombatUI().addMessage(1, message);
    }
}
