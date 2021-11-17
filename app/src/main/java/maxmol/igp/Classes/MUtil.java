package maxmol.igp.Classes;

/**
 * My useful math methods.
 */
public class MUtil {

    /**
     * @param num: the number input
     * @param min: minimum value that number can be
     * @param max: maximum value of that number
     * @return clamped number
     */
    public static double clamp(double num, double min, double max) {
        if (num > max) {
            return max;
        }
        else if (num < min) {
            return min;
        }
        else {
            return num;
        }
    }

    public static float clamp(float num, float min, float max) {
        if (num > max) {
            return max;
        }
        else if (num < min) {
            return min;
        }
        else {
            return num;
        }
    }

    public static int clamp(int num, int min, int max) {
        if (num > max) {
            return max;
        }
        else if (num < min) {
            return min;
        }
        else {
            return num;
        }
    }

    public static double clamp(double num, double min) {
        if (num < min) {
            return min;
        }
        else {
            return num;
        }
    }

    public static int clamp(int num, int min) {
        if (num < min) {
            return min;
        }
        else {
            return num;
        }
    }

    public static float clamp(float num, float min) {
        if (num < min) {
            return min;
        }
        else {
            return num;
        }
    }


    /**
     * @param delta: approach coefficient
     * @param from: current value
     * @param to: approached value
     * @return simple lerp
     */
    public static float lerp(float delta, float from, float to) {
        if (delta > 1) return to;
        if (delta < 0) return from;

        return from + (to - from) * delta;
    }

}