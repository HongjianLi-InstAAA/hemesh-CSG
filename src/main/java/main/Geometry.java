package main;

import wblut.geom.WB_Coord;
import wblut.hemesh.HE_Face;
import wblut.hemesh.HE_Mesh;
import wblut.hemesh.HE_Vertex;

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
    public List<String>colors;
    public List<Integer>col_FaceNum;

    public Geometry() {
        verts = new ArrayList<double[]>();
        faces = new ArrayList<int[]>();
        colors = new ArrayList<>();
        col_FaceNum = new ArrayList<>();
    }

    public void addMesh(HE_Mesh mesh, int color){
        //add color
        colors.add(""+color);

        //add vertices
        List<HE_Vertex>vertices = mesh.getVertices();
        for (HE_Vertex v:vertices)
            addVerts(v);

        //add faces
        for (HE_Face f:mesh.getFaces()){
            List<HE_Vertex>faceVertices = f.getFaceVertices();
            int[]versID = new int[faceVertices.size()];
            for (int i = 0; i < versID.length; i++) {
                versID[i] = vertices.indexOf(faceVertices.get(i));
            }
            faces.add(versID);
        }

        //add number of faces with this color
        col_FaceNum.add(mesh.getNumberOfFaces());
    }

    public void addVerts(WB_Coord pt) {
        verts.add(new double[]{pt.xd(), pt.yd(), pt.zd()});
    }

    public void addFace(int f0, int f1, int f2) {
        faces.add(new int[]{f0, f1, f2});

    }
}
