package org.lwjglb.engine.graph;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.*;
import java.util.*;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private final int numVertices;
    private final int vaoId;
    private final List<Integer> vboIdList;

    public Mesh(float[] vertices, float[] colors, float[] normals) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            numVertices = vertices.length;
            vboIdList = new ArrayList<>();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Positions VBO
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            FloatBuffer verticesBuffer = MemoryUtil.memCallocFloat(vertices.length); //stack.callocFloat(vertices.length);
            verticesBuffer.put(0, vertices);
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            MemoryUtil.memFree(verticesBuffer);
        }

        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Color VBO
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            FloatBuffer colorsBuffer = MemoryUtil.memCallocFloat(colors.length);
            colorsBuffer.put(0, colors);
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, colorsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
            MemoryUtil.memFree(colorsBuffer);
        }

        // Normal VBO
        if (!Objects.isNull(normals)) {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                int vboId = glGenBuffers();
                vboIdList.add(vboId);
                FloatBuffer normalsBuffer = MemoryUtil.memCallocFloat(normals.length);
                normalsBuffer.put(0, normals);
                glBindBuffer(GL_ARRAY_BUFFER, vboId);
                glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW);
                glEnableVertexAttribArray(2);
                glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
                MemoryUtil.memFree(normalsBuffer);
            }
        }

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void cleanup() {
        vboIdList.forEach(GL30::glDeleteBuffers);
        glDeleteVertexArrays(vaoId);
    }

    public int getNumVertices() {
        return numVertices;
    }

    public final int getVaoId() {
        return vaoId;
    }
}
