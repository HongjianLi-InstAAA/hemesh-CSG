package util;

import eu.mihosoft.vrl.v3d.*;
import wblut.geom.WB_Coord;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.hemesh.*;

import java.util.ArrayList;
import java.util.List;

/**
 * based on JCSG 0.6.7
 * @author JianZ
 */
public class CSGOp {
    /**
     * convert from CSG Object to HE_Mesh
     * @param union
     * @return
     */
    public static HE_Mesh toHE_Mesh(CSG union){
        //get initial mesh from polygons
        List<WB_Polygon> polys = toWB_Polygons(union.getPolygons());
        HE_Mesh mesh = new HEC_FromPolygons(polys).create();

        //rebuild mesh to add point on edge
        MeshReNode reno = new MeshReNode(mesh);
        mesh = reno.getReNoded();

        //fuse coplanar faces(join triangle faces which share the same plan)
        HE_MeshOp.fuseCoplanarFaces(mesh,1e-5);

        return mesh;
    }

    /**
     * convert from HE_Mesh to CSG Object by Polyhedron of JCSG
     * @param mesh
     * @return
     */
    public static CSG toCSG(HE_Mesh mesh){
        List<HE_Vertex>vertices = mesh.getVertices();
        List<HE_Face>faces = mesh.getFaces();

        List<List<Integer>>ids = new ArrayList<>();

        for (HE_Face f:faces){
            List<Integer>faceIds = new ArrayList<Integer>();
            for (HE_Vertex v:f.getFaceVertices())
                faceIds.add(vertices.indexOf(v));
            ids.add(faceIds);
        }

        List<Vector3d> points = toVector3ds(vertices);
        Polyhedron polyhedron = new Polyhedron(points,ids);

        return polyhedron.toCSG();
    }

    private static List<WB_Polygon> toWB_Polygons(List<Polygon> polys) {
        List<WB_Polygon> wPolys = new ArrayList<>();
        for (Polygon p : polys) {
            wPolys.add(toWB_Polygon(p));
        }
        return wPolys;
    }

    public static List<Vector3d> toCSGPoints(WB_Polygon wPoly){
        List<Vector3d>vector3ds = new ArrayList<>();
        for (WB_Coord c:wPoly.getPoints().toList()){
            vector3ds.add(toVector3d(c));
        }
        return vector3ds;
    }
    public static WB_Polygon toWB_Polygon(Polygon poly) {
        List<Vertex> vers = poly.vertices;
        List<WB_Point> pts = new ArrayList<>();
        for (Vertex ver : vers) {
            WB_Point pt = new WB_Point(ver.getX(), ver.getY(), ver.getZ());
            pts.add(pt);
        }
        return new WB_Polygon(pts);
    }

    private static List<Vector3d>toVector3ds(List<HE_Vertex>coords){
        List<Vector3d>vecs = new ArrayList<>();
        for (WB_Coord c:coords){
            vecs.add(toVector3d(c));
        }
        return vecs;
    }
    private static Vector3d toVector3d(WB_Coord coord){
        return new Vector3d(coord.xd(),coord.yd(),coord.zd());
    }
}
