import Guo_Cam.CameraController;
import building.MeshClassifier;
import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Cube;
import eu.mihosoft.vrl.v3d.Transform;
import processing.core.PApplet;
import util.CSGOp;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.hemesh.HE_Mesh;
import wblut.processing.WB_Render3D;

import java.util.ArrayList;
import java.util.List;

public class TestJCSG extends PApplet{
    CameraController cam;
    WB_Render3D render;
    HE_Mesh mesh;
    CSG cube, cube2;
    MeshClassifier manager;
    List<CSG> objects;

    public static void main(String[] args) {
        PApplet.main(TestJCSG.class.getName());
    }

    @Override
    public void setup() {
        // TODO Auto-generated method stub
        size(1200, 900, P3D);
        cam = new CameraController(this, 20);
        render = new WB_Render3D(this);

        smooth(8);
        // we use cube and sphere as base geometries
        cube = new Cube(2).toCSG().transformed(Transform.unity().translateZ(1));
        cube2 = new Cube(3).toCSG().transformed(Transform.unity().translateX(2.5).translateZ(1.5));
        objects = getRandomCube(20, 10, 10);
        update();
    }

    public void update() {
        CSG union = cube.union(cube2);
        for (CSG c : objects)
            union = union.union(c);
        mesh = CSGOp.toHE_Mesh(union);
        manager = new MeshClassifier(mesh);
        System.out.println("    faceCount: " + mesh.getNumberOfFaces());
    }

    @Override
    public void draw() {
        // TODO Auto-generated method stub
        background(255);
        cam.drawSystem(100);
        manager.draw(render);
    }

    double step = 0.2;

    public void keyPressed() {
        switch (key) {
            case 'w':
                move(0, step, 0);
                break;
            case 's':
                move(0, -step, 0);
                break;
            case 'a':
                move(step, 0, 0);
                break;
            case 'd':
                move(-step, 0, 0);
                break;
            case 'q':
                move(0, 0, step);
                break;
            case 'e':
                move(0, 0, -step);
                break;
            case 'r':
                objects = getRandomCube(10, 10, 10);
                update();
                break;
        }
    }

    private void move(double x, double y, double z) {
        cube2 = cube2.transformed(Transform.unity().translate(x, y, z));
        update();
    }

    int minSize = 2;
    int maxSize = 5;

    List<CSG> getRandomCube(int num, int xSize, int ySize) {
        List<CSG> objects = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            double size = Math.random() * (maxSize - minSize) + minSize;
            CSG cube = new Cube(size).toCSG().transformed(Transform.unity().translate(Math.random() * xSize, Math.random() * ySize, size / 2));
            objects.add(cube);
        }
        return objects;
    }

//    List<CSG> getRandomeExtrude(int num, int xSize, int ySize){
//
//    }

    WB_Polygon getRandPoly(int x, int y, int minEdgeNum, int maxEdgeNum) {
        List<WB_Point> pts = new ArrayList<>();
        WB_Point center = new WB_Point(x, y, 0);
        int num = (int) (Math.random() * (maxEdgeNum - minEdgeNum)) + minEdgeNum;
        double angle = Math.PI * 2 / num;
        for (int i = 0; i < num; i++) {
            double length = Math.random() * (maxSize - minSize) + minSize;


        }
        return new WB_Polygon(pts);
    }
}
