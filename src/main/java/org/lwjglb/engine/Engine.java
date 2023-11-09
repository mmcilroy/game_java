package org.lwjglb.engine;

import org.lwjglb.engine.graph.Render;
import org.lwjglb.engine.scene.Camera;
import org.lwjglb.engine.scene.Scene;

public class Engine {

    public static final int TARGET_UPS = 30;
    private IAppLogic appLogic;
    private Window window;
    private Render render;
    private boolean running;
    private boolean restart;
    private Scene scene;
    private final String windowTitle;
    private final Window.WindowOptions opts;

    public Engine(String windowTitle, Window.WindowOptions opts, IAppLogic appLogic) {
        this.windowTitle = windowTitle;
        this.opts = opts;
        this.appLogic = appLogic;
    }

    private void cleanup() {
        System.out.println("Cleanup");
        appLogic.cleanup();
        render.cleanup();
        scene.cleanup();
        window.cleanup();
    }

    private void resize() {
        scene.resize(window.getWidth(), window.getHeight());
    }

    private void run() {
        window = new Window(windowTitle, opts, () -> {
            resize();
            return null;
        });

        render = new Render();
        scene = new Scene(window.getWidth(), window.getHeight());
        appLogic.init(window, scene, render);
        running = true;

        long initialTime = System.currentTimeMillis();
        float timeU = 1000.0f / opts.ups;
        float timeR = opts.fps > 0 ? 1000.0f / opts.fps : 0;
        float deltaUpdate = 0;
        float deltaFps = 0;

        long updateTime = initialTime;
        while (running && !window.windowShouldClose()) {
            window.pollEvents();

            long now = System.currentTimeMillis();
            deltaUpdate += (now - initialTime) / timeU;
            deltaFps += (now - initialTime) / timeR;

            if (opts.fps <= 0 || deltaFps >= 1) {
                window.getMouseInput().input();
                appLogic.input(window, scene, now - initialTime);
            }

            if (deltaUpdate >= 1) {
                long diffTimeMillis = now - updateTime;
                appLogic.update(window, scene, diffTimeMillis);
                updateTime = now;
                deltaUpdate--;
            }

            if (opts.fps <= 0 || deltaFps >= 1) {
                render.render(window, scene);
                deltaFps--;
                window.update();
            }
            initialTime = now;
        }

        cleanup();
    }

    public void start() {
        System.out.println("Start");
        running = true;
        restart = false;
        run();
    }

    public void stop() {
        System.out.println("Stop");
        running = false;
        restart = false;
    }

    public void restart() {
        System.out.println("Restart");
        running = false;
        restart = true;
    }

    public boolean shouldRestart() {
        return restart;
    }

    public Scene getScene() {
        return scene;
    }

    public Render getRender() {
        return render;
    }
}
