package maxmol.igp.classes;

// @ Useful math methods.
public class MUtil {
    public static double Clamp(double num, double min, double max) {
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

    public static float Clamp(float num, float min, float max) {
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

    public static int Clamp(int num, int min, int max) {
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

    public static double Clamp(double num, double min) {
        if (num < min) {
            return min;
        }
        else {
            return num;
        }
    }

    public static int Clamp(int num, int min) {
        if (num < min) {
            return min;
        }
        else {
            return num;
        }
    }

    public static float Clamp(float num, float min) {
        if (num < min) {
            return min;
        }
        else {
            return num;
        }
    }
}