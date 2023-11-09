package org.lwjglb.game;

import org.lwjglb.engine.graph.Mesh;

public class CubeMesh extends Mesh {

    public CubeMesh() {

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
        super(genVertices(), genColors(), null);
    }

    static private float[] genVertices() {
        return new float[]{
                // top (x, z)
                0.0f, 1.0f, 1.0f,
                0.0f, 1.0f, 0.0f,
                0.1f, 1.0f, 0.0f,

                1.0f, 1.0f, 1.0f,
                0.0f, 1.0f, 1.0f,
                0.1f, 1.0f, 0.0f,

                // bottom (x, z)
                /*
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 0.0f,
                0.1f, 0.0f, 0.0f,

                1.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.1f, 0.0f, 0.0f,
                */
        };
    }

    static private float[] genColors() {
        return new float[]{
                1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 1.0f,
        };
    }
}
