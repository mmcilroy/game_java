package org.lwjglb.game;

import org.hjson.JsonArray;
import org.hjson.JsonValue;
import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjglb.engine.*;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.Model;
import org.lwjglb.engine.graph.Render;
import org.lwjglb.engine.scene.Camera;
import org.lwjglb.engine.scene.Entity;
import org.lwjglb.engine.scene.Scene;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Main implements IAppLogic {

    private static final float MOUSE_SENSITIVITY = 0.1f;
    private static final float MOVEMENT_SPEED = 0.005f;
    private Engine engine;
    private Entity cubeEntity;

    public static void main(String[] args) {
        new Main();
    }

    private Main() {
        // Todo Make this part of config
        var windowOptions = new Window.WindowOptions();
        windowOptions.width = 800;
        windowOptions.height = 600;

        engine = new Engine("game", windowOptions, this);
        engine.start();
        while (engine.shouldRestart()) {
            engine.start();
        }

        System.out.println("Goodbye!");
    }

    @Override
    public void cleanup() {
        // Nothing to be done yet
    }

    @Override
    public void init(Window window, Scene scene, Render render) {
        loadConfig("resources/config/config.json");
        Mesh mesh = loadMesh("resources/config/cube.json");

        String cubeModelId = "cube-model";
        Model model = new Model(cubeModelId, List.of(mesh));
        scene.addModel(model);

        cubeEntity = new Entity("cube-entity", cubeModelId);
        cubeEntity.setPosition(0, 0, 0);
        cubeEntity.updateModelMatrix();

        scene.addEntity(cubeEntity);
    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis) {
        float move = diffTimeMillis * MOVEMENT_SPEED;
        Camera camera = scene.getCamera();

        if (window.isKeyPressed(GLFW_KEY_W)) {
            camera.moveForward(move);
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            camera.moveBackwards(move);
        }

        if (window.isKeyPressed(GLFW_KEY_A)) {
            camera.moveLeft(move);
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            camera.moveRight(move);
        }

        if (window.isKeyPressed(GLFW_KEY_UP)) {
            camera.moveUp(move);
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            camera.moveDown(move);
        }

        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            camera.moveUp(move);
        } else if (window.isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
            camera.moveDown(move);
        }

        if (window.isKeyPressed(GLFW_KEY_Q)) {
            var p = cubeEntity.getPosition();
            cubeEntity.setPosition(p.x, p.y, p.z - 0.1f);
            cubeEntity.updateModelMatrix();
        } else if (window.isKeyPressed(GLFW_KEY_E)) {
            var p = cubeEntity.getPosition();
            cubeEntity.setPosition(p.x, p.y, p.z + 0.1f);
            cubeEntity.updateModelMatrix();
        }

        if (window.isKeyPressed(GLFW_KEY_F5)) {
            engine.restart();
        }

        MouseInput mouseInput = window.getMouseInput();
        if (mouseInput.isRightButtonPressed()) {
            Vector2f displVec = mouseInput.getDisplVec();
            camera.addRotation((float) Math.toRadians(-displVec.x * MOUSE_SENSITIVITY),
                    (float) Math.toRadians(displVec.y * MOUSE_SENSITIVITY));
        }
    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {
        //System.out.println(scene.getCamera().getPosition() + ", " + scene.getCamera().getRotation());
    }

    private void loadConfig(String filename) {
        var json = JsonValue.readHjson(Utils.readFile(filename)).asObject();
        var cameraJson = json.get("camera").asObject();
        var cameraPosition = readJsonFloatArray(cameraJson.get("position").asArray());
        var cameraRotation = readJsonFloatArray(cameraJson.get("rotation").asArray());
        var camera = this.engine.getScene().getCamera();
        camera.setPosition(cameraPosition[0], cameraPosition[1], cameraPosition[2]);
        camera.setRotation(cameraRotation[0], cameraRotation[1]);

        var uniformsJson = json.get("uniforms").asObject();
        var lightDirection = readJsonFloatArray(uniformsJson.get("lightDirection").asArray());
        var lightColour = readJsonFloatArray(uniformsJson.get("lightColour").asArray());
        var lightBias = readJsonFloatArray(uniformsJson.get("lightBias").asArray());
        Config.lightDirection = new Vector3f(lightDirection[0], lightDirection[1], lightDirection[2]);
        Config.lightColour = new Vector3f(lightColour[0], lightColour[1], lightColour[2]);
        Config.lightBias = new Vector2f(lightBias[0], lightBias[1]);
    }

    private Mesh loadMesh(String filename) {
        var json = JsonValue.readHjson(Utils.readFile(filename)).asObject();
        var vertices = readJsonFloatArray(json.get("vertices").asArray());
        var colors = readJsonFloatArray(json.get("colors").asArray());

        var normals = new float[vertices.length];
        for (int i = 0; i < normals.length; i += 9) {
            var v0 = new Vector3f(vertices[i], vertices[i + 1], vertices[i + 2]);
            var v1 = new Vector3f(vertices[i + 3], vertices[i + 4], vertices[i + 5]);
            var v2 = new Vector3f(vertices[i + 6], vertices[i + 7], vertices[i + 8]);

            var normal = Utils.calcNormal(v0, v1, v2);
            normals[i] = normal.x;
            normals[i + 1] = normal.y;
            normals[i + 2] = normal.z;
            normals[i + 3] = normal.x;
            normals[i + 4] = normal.y;
            normals[i + 5] = normal.z;
            normals[i + 6] = normal.x;
            normals[i + 7] = normal.y;
            normals[i + 8] = normal.z;
        }

        return new Mesh(vertices, colors, normals);
    }

    private float[] readJsonFloatArray(JsonArray array) {
        var buf = FloatBuffer.allocate(array.size());
        array.forEach(e -> {
            buf.put(e.asFloat());
        });
        return buf.array();
    }
}
