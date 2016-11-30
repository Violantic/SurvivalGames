package me.violantic.sg.game.util;

/**
 * Created by Ethan on 11/28/2016.
 */
public class MathUtil {

    public static void main(String[] args) {
        calculateSineLength('c', Math.sin(40), 10.0D, Math.sin(88));
    }

    public static void calculateSineLength(char x, double firstAngle, double secondLength, double secondAngle) {
        double firstFunc = firstAngle;
        double secondFunc = secondAngle/secondLength;
        System.out.println(x + " is equal to " + secondFunc*firstFunc);
    }

}
