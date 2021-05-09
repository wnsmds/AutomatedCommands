package data.hullmods;

public class CRLevelMedRetreat extends BaseCRLevelAutomation {
    /** retreat when CR is 60% (degraded performance) */
    public CRLevelMedRetreat() {
        super(0.6f);
    }
}