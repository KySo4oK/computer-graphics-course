package com.example.demo;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class ObjLoader {

    public static Vector3 parseVertex(String[] data) {
        return new Vector3(Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]));
    }

    public static Vector3 parseNormal(String[] data) {
        return new Vector3(Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]));
    }

    public static Vector3 parseTextureCoordinate(String[] data) {
        return new Vector3(Double.parseDouble(data[1]), Double.parseDouble(data[2]), 0);
    }

    public static Vertex parseTriangleVertex(String[] data, ArrayList<Vector3> vertices, ArrayList<Vector3> textureCoordinates, ArrayList<Vector3> normals) {
        Vector3 vertex = null;
        if (!data[0].isEmpty()) {
            int vertexIndex = Integer.parseInt(data[0]) - 1;
            vertex = vertices.get(vertexIndex);
        }

        Vector3 textureCoordinate = null;
        if (!data[1].isEmpty()) {
            int textureCoordinateIndex = Integer.parseInt(data[1]) - 1;
            textureCoordinate = textureCoordinates.get(textureCoordinateIndex);
        }

        Vector3 normal = null;
        if (!data[2].isEmpty()) {
            int normalIndex = Integer.parseInt(data[2]) - 1;
            normal = normals.get(normalIndex);
        }

        return new Vertex(vertex, textureCoordinate, normal);
    }

    public static Triangle parseFace(String[] data, ArrayList<Vector3> vertices, ArrayList<Vector3> textureCoordinates, ArrayList<Vector3> normals, Material material) {
        return new Triangle(
                parseTriangleVertex(data[1].split("/"), vertices, textureCoordinates, normals),
                parseTriangleVertex(data[2].split("/"), vertices, textureCoordinates, normals),
                parseTriangleVertex(data[3].split("/"), vertices, textureCoordinates, normals),
                material
        );
    }

    public static Triangle[] parseFile(File file) throws Exception {
        BufferedReader input = new BufferedReader(new FileReader(file));

        ArrayList<Vector3> vertices = new ArrayList<>();
        ArrayList<Vector3> textureCoordinates = new ArrayList<>();
        ArrayList<Vector3> normals = new ArrayList<>();
        ArrayList<Triangle> faces = new ArrayList<>();

        HashMap<String, Material> materials = new HashMap<>();
        Material currentMaterial = new Material();

        String line;
        while ((line = input.readLine()) != null) {
            String[] data = line.split(" ");
            switch (data[0]) {
                case "mtllib":
                    // Read material file from disk and parse it
                    File parentDirectory = file.getParentFile();
                    File[] files = parentDirectory.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.equals(data[1]);
                        }
                    });
                    if (files.length >= 1) {
                        File materialFile = files[0];
                        HashMap<String, Material> newMaterials = parseMaterialFile(materialFile);
                        materials.putAll(newMaterials);
                    }
                    break;
                case "usemtl":
                    currentMaterial = materials.get(data[1]);
                    break;
                case "v":
                    vertices.add(parseVertex(data));
                    break;
                case "vn":
                    normals.add(parseNormal(data));
                    break;
                case "vt":
                    textureCoordinates.add(parseTextureCoordinate(data));
                    break;
                case "f":
                    faces.add(parseFace(data, vertices, textureCoordinates, normals, currentMaterial));
                    break;
            }
        }
        input.close();

        Triangle[] facesArray = new Triangle[faces.size()];
        facesArray = faces.toArray(facesArray);
        return facesArray;
    }

    public static Vector3 parseColor(String[] data) {
        return new Vector3(Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]));
    }

    public static HashMap<String, Material> parseMaterialFile(File file) throws Exception {
        HashMap<String, Material> materials = new HashMap<>();

        Material currentMaterial = new Material();

        BufferedReader input = new BufferedReader(new FileReader(file));
        String line;
        while ((line = input.readLine()) != null) {
            String[] data = line.split(" ");
            switch (data[0]) {
                case "newmtl":
                    currentMaterial = new Material();
                    materials.put(data[1], currentMaterial);
                    break;
                case "Ka":
                    currentMaterial.ambientColor = parseColor(data);
                    break;
                case "Kd":
                    currentMaterial.diffuseColor = parseColor(data);
                    break;
                case "Ks":
                    currentMaterial.specularColor = parseColor(data);
                    break;
                case "Ns":
                    currentMaterial.specularExponent = Double.parseDouble(data[1]);
                    break;
                case "d":
                    currentMaterial.transparency = Double.parseDouble(data[1]);
                    break;
                case "illum":
                    currentMaterial.illuminationModel = Integer.parseInt(data[1]);
                    break;
            }
        }
        input.close();

        return materials;
    }

}
