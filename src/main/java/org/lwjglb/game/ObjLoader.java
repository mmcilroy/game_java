package org.lwjglb.game;

import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjglb.engine.Utils;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class ObjLoader {

    public static Mesh load(String filename) {
        var v1 = new ArrayList<Vector3f>();
        var f1 = new ArrayList<Vector3i>();

        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(line -> {
                if (line.startsWith("v ")) {
                    String[] tokens = line.split(" ");
                    v1.add(
                            new Vector3f(
                                    Float.parseFloat(tokens[1]),
                                    Float.parseFloat(tokens[2]),
                                    Float.parseFloat(tokens[3])));
                } else if (line.startsWith("f ")) {
                    String[] tokens = line.split(" ");
                    f1.add(
                            new Vector3i(
                                    Integer.parseInt(tokens[1].split("/")[0]),
                                    Integer.parseInt(tokens[2].split("/")[0]),
                                    Integer.parseInt(tokens[3].split("/")[0])));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        var v2 = new float[f1.size() * 9];
        var n2 = new float[f1.size() * 9];
        int j=0;
        int k=0;

        for (Vector3i face : f1) {

            var va = new Vector3f(v1.get(face.x - 1).x, v1.get(face.x - 1).y, v1.get(face.x - 1).z);
            var vb = new Vector3f(v1.get(face.y - 1).x, v1.get(face.y - 1).y, v1.get(face.y - 1).z);
            var vc = new Vector3f(v1.get(face.z - 1).x, v1.get(face.z - 1).y, v1.get(face.z - 1).z);
            var nn = Utils.calcNormal(va, vb, vc);

            v2[j++] = va.x;
            v2[j++] = va.y;
            v2[j++] = va.z;

            v2[j++] = vb.x;
            v2[j++] = vb.y;
            v2[j++] = vb.z;

            v2[j++] = vc.x;
            v2[j++] = vc.y;
            v2[j++] = vc.z;

            n2[k++] = nn.x;
            n2[k++] = nn.y;
            n2[k++] = nn.z;

            n2[k++] = nn.x;
            n2[k++] = nn.y;
            n2[k++] = nn.z;

            n2[k++] = nn.x;
            n2[k++] = nn.y;
            n2[k++] = nn.z;
        }

        var c = new float[v2.length];
        Arrays.fill(c, 0.5f);

        return new Mesh(v2, c, n2);
    }

    public static Mesh loadWithNormals(String filename) {
        var v1 = new ArrayList<Vector3f>();
        var n1 = new ArrayList<Vector3f>();
        var f1 = new ArrayList<Vector3i>();
        var fn = new ArrayList<Vector3i>();

        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(line -> {
                if (line.startsWith("v ")) {
                    String[] tokens = line.split(" ");
                    v1.add(
                            new Vector3f(
                                    Float.parseFloat(tokens[1]),
                                    Float.parseFloat(tokens[2]),
                                    Float.parseFloat(tokens[3])));
                } else if (line.startsWith("vn ")) {
                    String[] tokens = line.split(" ");
                    n1.add(
                            new Vector3f(
                                    Float.parseFloat(tokens[1]),
                                    Float.parseFloat(tokens[2]),
                                    Float.parseFloat(tokens[3])));

                } else if (line.startsWith("f ")) {
                    String[] tokens = line.split(" ");
                    f1.add(
                            new Vector3i(
                                    Integer.parseInt(tokens[1].split("/")[0]),
                                    Integer.parseInt(tokens[2].split("/")[0]),
                                    Integer.parseInt(tokens[3].split("/")[0])));
                    fn.add(
                            new Vector3i(
                                    Integer.parseInt(tokens[1].split("/")[2]),
                                    Integer.parseInt(tokens[2].split("/")[2]),
                                    Integer.parseInt(tokens[3].split("/")[2])));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        var v2 = new float[f1.size() * 9];
        int j=0;

        for (Vector3i face : f1) {

            var va = new Vector3f(v1.get(face.x - 1).x, v1.get(face.x - 1).y, v1.get(face.x - 1).z);
            var vb = new Vector3f(v1.get(face.y - 1).x, v1.get(face.y - 1).y, v1.get(face.y - 1).z);
            var vc = new Vector3f(v1.get(face.z - 1).x, v1.get(face.z - 1).y, v1.get(face.z - 1).z);

            v2[j++] = va.x;
            v2[j++] = va.y;
            v2[j++] = va.z;

            v2[j++] = vb.x;
            v2[j++] = vb.y;
            v2[j++] = vb.z;

            v2[j++] = vc.x;
            v2[j++] = vc.y;
            v2[j++] = vc.z;
        }

        var n2 = new float[f1.size() * 9];
        int k=0;

        for (Vector3i normal : fn) {

            var na = new Vector3f(n1.get(normal.x - 1).x, n1.get(normal.x - 1).y, n1.get(normal.x - 1).z);
            var nb = new Vector3f(n1.get(normal.y - 1).x, n1.get(normal.y - 1).y, n1.get(normal.y - 1).z);
            var nc = new Vector3f(n1.get(normal.z - 1).x, n1.get(normal.z - 1).y, n1.get(normal.z - 1).z);

            n2[k++] = na.x;
            n2[k++] = na.y;
            n2[k++] = na.z;

            n2[k++] = nb.x;
            n2[k++] = nb.y;
            n2[k++] = nb.z;

            n2[k++] = nc.x;
            n2[k++] = nc.y;
            n2[k++] = nc.z;
        }

        var c = new float[v2.length];
        Arrays.fill(c, 0.5f);

        return new Mesh(v2, c, n2);
    }
}
