package building;

import processing.opengl.PGraphicsOpenGL;
import util.Display;
import wblut.geom.WB_Coord;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.hemesh.HE_Mesh;
import wblut.hemesh.HE_MeshOp;
import wblut.math.WB_Epsilon;
import wblut.processing.WB_Render3D;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JianZ
 * @version 1.0
 * @date 2020/11/13 16:51
 */
public class MeshClassifier implements Display {
    List<WB_Polygon> tops, bases, walls;
    HE_Mesh mesh;
    WB_Vector dir;

    public MeshClassifier(HE_Mesh mesh, WB_Coord dir) {
        tops = new ArrayList<>();
        bases = new ArrayList<>();
        walls = new ArrayList<>();
        this.mesh = mesh;
        this.dir = new WB_Vector(dir);
        sort();
    }

    public MeshClassifier(HE_Mesh mesh){
        this(mesh,WB_Vector.Z());
    }

    private void sort() {
        // join triangle faces on the same plane
        HE_MeshOp.fuseCoplanarFaces(this.mesh);

        // sort mesh polygons, find base, top & walls
        List<WB_Polygon> polys = this.mesh.getPolygonList();
        for (WB_Polygon p : polys) {
            int flag = sameDir(p.getNormal(), dir);
            switch (flag) {
                case 1:
                    tops.add(p);
                    break;
                case -1:
                    bases.add(p);
                    break;
                default:
                    walls.add(p);
            }
        }

    }

    /**
     *
     * @param v1
     * @param v2
     * @return 1: same -1: reverse 0: other
     */
    private int sameDir(WB_Vector v1, WB_Vector v2,double threshold) {
        double angle = v1.getAngle(v2);
        if (angle < threshold)
            return 1;
        if (Math.PI - angle < threshold)
            return -1;
        return 0;
    }

    private int sameDir(WB_Vector v1, WB_Vector v2){
       return sameDir(v1,v2,WB_Epsilon.EPSILONANGLE);
    }

    public HE_Mesh getMesh() {
        return mesh;
    }

    public List<WB_Polygon> getBases() {
        return bases;
    }

    public List<WB_Polygon> getTops() {
        return tops;
    }

    public List<WB_Polygon> getWalls() {
        return walls;
    }

    public WB_Vector getDir() {
        return dir;
    }

    @Override
    public void draw(WB_Render3D render) {
        PGraphicsOpenGL app = render.getHome();
        app.fill(GRAY);
        render.drawPolygonEdges(bases);
        app.fill(CYAN);
        render.drawPolygonEdges(tops);
        app.fill(RICE);
        render.drawPolygonEdges(walls);
    }
}
