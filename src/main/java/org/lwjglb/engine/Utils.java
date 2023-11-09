package org.lwjglb.engine;

import org.joml.Vector3f;

import java.io.IOException;
import java.nio.file.*;

public class Utils {

    private Utils() {
        // Utility class
    }

    public static String readFile(String filePath) {
        String str;
        try {
            str = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException excp) {
            throw new RuntimeException("Error reading file [" + filePath + "]", excp);
        }
        return str;
    }

    public static Vector3f calcNormal(Vector3f v0, Vector3f v1, Vector3f v2) {
        var tangentA = new Vector3f(v1).sub(v0);
        var tangentB = new Vector3f(v2).sub(v0);
        var normal = tangentA.cross(tangentB);
        return normal.normalize();
    }
}
