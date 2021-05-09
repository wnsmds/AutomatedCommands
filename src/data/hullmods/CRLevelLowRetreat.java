package data.hullmods;

public class CRLevelLowRetreat extends BaseCRLevelAutomation {
    /** retreat when CR is 40% (possible malfunctions) */
    public CRLevelLowRetreat() {
        super(0.4f);
    }
}
