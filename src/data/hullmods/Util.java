package data.hullmods;

public enum Util {
    ;

    //TODO implement NumberFormat
    public static String percentage(float value) {
        return (int)(value * 100) + "%";
    }
    public static String percentage(double value) {
        return (int)(value * 100) + "%";
    }
}
