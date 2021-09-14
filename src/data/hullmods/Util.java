package data.hullmods;

public class Util {
    public static String percentage(float value) {
        return Integer.toString((int)(value * 100)) + "%";
    }
    public static String percentage(double value) {
        return Integer.toString((int)(value * 100)) + "%";
    }
}
