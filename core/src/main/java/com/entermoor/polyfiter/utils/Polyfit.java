package com.entermoor.polyfiter.utils;

import com.badlogic.gdx.math.Vector2;

import java.util.Set;

public abstract class Polyfit {

    /**
     * /        sigma(i = 1 -> n)(xi*yi) - n*avgX*avgY
     * | k = -------------------------------------------- ;
     * \          sigma(i = 1 -> n)(xi^2) - n*avgX^2
     * b = avgY - k * avgX;
     * ==> f(x) = kx+b;
     *
     * @param points Given points
     * @return "k*x+b"
     */
    public static String polyfit1(Set<Vector2> points) {
        int n = points.size();
        float sigmaX = 0;
        float sigmaY = 0;
        float sigmaXY = 0;
        float sigmaX2 = 0;

        for (Vector2 point : points) {
            sigmaX += point.x;
            sigmaY += point.y;
            sigmaXY += point.x * point.y;
            sigmaX2 += point.x * point.x;
        }

        float avgX = sigmaX / n;
        float avgY = sigmaY / n;

        float k = (sigmaXY - n * avgX * avgY) / (sigmaX2 - n * avgX * avgX);
        float b = avgY - k * avgX;

        return "(" + k + ")*(x)+(" + b + ")";
    }
}
