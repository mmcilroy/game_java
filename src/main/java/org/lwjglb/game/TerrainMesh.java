package org.lwjglb.game;

import org.joml.Vector3f;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class TerrainMesh {

    private final FloatBuffer vertices;

    private final FloatBuffer colors;

    private final FloatBuffer normals;

    public TerrainMesh(Vector3f p, int nx, int ny) {

        /*
         * TRIANGLE
         *
         * Z
         * ^
         * | v0,4      v3
         * | # #########
         * | ### #######
         * | ##### #####
         * | ####### ###
         * | ######### #
         * | v1      v2,5
         * |____________> X
         *
         */

        int nv = 6 * 3 * nx * ny;

        this.vertices = FloatBuffer.allocate(nv);
        this.colors = FloatBuffer.allocate(nv);
        this.normals = FloatBuffer.allocate(nv);

        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {

                p.x = i;
                p.z = j;

                // First triangle
                var v0 = new Vector3f(p.x, p.y, p.z);
                var v1 = new Vector3f(p.x, p.y, p.z + 1);
                var v2 = new Vector3f(p.x + 1, p.y, p.z + 1);

                // Second triangle
                var v3 = new Vector3f(p.x + 1, p.y, p.z);
                var v4 = new Vector3f(p.x, p.y, p.z);
                var v5 = new Vector3f(p.x + 1, p.y, p.z + 1);

                // Calculate normals
                var n0 = calcNormal(v0, v1, v2);
                var n1 = calcNormal(v3, v4, v5);

                // Create data arrays
                this.vertices.put(new float[]{
                        v0.x, v0.y, v0.z,
                        v1.x, v1.y, v1.z,
                        v2.x, v2.y, v2.z,
                        v3.x, v3.y, v3.z,
                        v4.x, v4.y, v4.z,
                        v5.x, v5.y, v5.z,
                });

                this.colors.put(new float[]{
                        1.0f, 0.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 0.0f, 1.0f,
                        1.0f, 0.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                        0.0f, 0.0f, 1.0f,
                });

                this.normals.put(new float[]{
                        n0.x, n0.y, n0.z,
                        n0.x, n0.y, n0.z,
                        n0.x, n0.y, n0.z,
                        n1.x, n1.y, n1.z,
                        n1.x, n1.y, n1.z,
                        n1.x, n1.y, n1.z,
                });
            }
        }
    }

    public float[] getVertices() {
        return vertices.array();
    }

    public float[] getColors() {
        return colors.array();
    }

    public float[] getNormals() {
        return normals.array();
    }

    private Vector3f calcNormal(Vector3f v0, Vector3f v1, Vector3f v2) {
        var tangentA = new Vector3f(v1).sub(v0);
        var tangentB = new Vector3f(v2).sub(v0);
        var normal = tangentA.cross(tangentB);
        return normal.normalize();
    }
}
