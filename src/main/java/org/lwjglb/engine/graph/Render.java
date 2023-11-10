package org.lwjglb.engine.graph;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.scene.Scene;
import org.lwjglb.game.Config;

import static org.lwjgl.opengl.GL11.*;

public class Render {

    private SceneRender sceneRender;

    public Render() {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        sceneRender = new SceneRender();
    }

    public void cleanup() {
        sceneRender.cleanup();
    }

    public void render(Window window, Scene scene) {
        glClearColor(Config.clearColor.x, Config.clearColor.y, Config.clearColor.z, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, window.getWidth(), window.getHeight());
        glEnable(GL13.GL_MULTISAMPLE);
        glEnable(GL11.GL_DEPTH_TEST);

        sceneRender.render(scene);
    }
}