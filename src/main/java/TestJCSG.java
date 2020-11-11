import Guo_Cam.CameraController;
import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Cube;
import eu.mihosoft.vrl.v3d.Transform;
import processing.core.PApplet;
import util.CSGOp;
import wblut.hemesh.HEC_Torus;
import wblut.hemesh.HE_Mesh;
import wblut.processing.WB_Render3D;

public class TestJCSG extends PApplet {
	CameraController cam;
	WB_Render3D render;
	HE_Mesh mesh;
	CSG cube, cube2,meshObj;

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
		cube = new Cube(2).toCSG();
		cube2 = new Cube(3).toCSG().transformed(Transform.unity().translateX(2.5).translateZ(0.5));

		HE_Mesh obj = new HEC_Torus().setRadius(1.5,3).setTorusFacets(4).setCenter(2,2,0).create();
		meshObj =CSGOp.toCSG(obj);

		update();
	}

	public void update() {
		CSG union = cube.union(cube2).union(meshObj);
		mesh = CSGOp.toHE_Mesh(union);
		System.out.println("    faceCount: " + mesh.getNumberOfFaces());
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		background(255);
		cam.drawSystem(100);
//		sim.draw(render);
		noStroke();
		fill(180, 120, 120);
//		render.drawPolygonEdges(polys);
		render.drawFaces(mesh);
		stroke(0);
		render.drawEdges(mesh);
		fill(0);
		render.drawVertices(mesh, 0.05);
		stroke(255, 0, 0);
		render.drawBoundaryHalfedges(mesh);
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
		}
	}

	private void move(double x, double y, double z) {
		cube2 = cube2.transformed(Transform.unity().translate(x, y, z));
		update();
	}
}
