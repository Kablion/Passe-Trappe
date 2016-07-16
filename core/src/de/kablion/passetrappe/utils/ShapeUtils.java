package de.kablion.passetrappe.utils;

import com.badlogic.gdx.math.MathUtils;

public class ShapeUtils {
    public static float[] buildCircle(float radius, int divisions) {
        float[] verts = new float[divisions * 2];
        float radiansPerDivision = (360 / divisions) * MathUtils.degreesToRadians;
        for (int division = 0; division < divisions; division++) {
            verts[division * 2] = (float) Math.cos(radiansPerDivision * division) * radius;
            verts[division * 2 + 1] = (float) Math.sin(radiansPerDivision * division) * radius;
        }
        return verts;
    }
}
