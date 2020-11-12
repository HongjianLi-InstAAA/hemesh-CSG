package main;

import wblut.hemesh.HE_Mesh;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JianZ
 * @version 1.0
 * @date 2020/11/12 15:14
 */
public class Geometry {
    public List<double[]> verts;
    public List<int[]> faces;

    public Geometry() {
        verts = new ArrayList<double[]>();
        faces = new ArrayList<int[]>();
    }

    public void addMesh(HE_Mesh mesh){

    }

    public void addVerts(WB_Coord pt) {
        verts.add(new double[]{pt.xd(), pt.yd(), pt.zd()});
    }

    public void addFaces(int f0, int f1, int f2) {
        faces.add(new int[]{f0, f1, f2});

    }
}
