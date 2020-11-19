package building;

import processing.opengl.PGraphicsOpenGL;
import util.Display;
import wblut.geom.*;
import wblut.hemesh.HEC_FromPolygons;
import wblut.hemesh.HEM_Extrude;
import wblut.hemesh.HE_Mesh;
import wblut.hemesh.HE_MeshOp;
import wblut.math.WB_Epsilon;
import wblut.processing.WB_Render3D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * class to record base, top, walls, height of an extrude volume
 * 
 * @author JianZ
 *
 */
public class ExtrudeObject implements Display {
	WB_Polygon base, top;
	double height;
	HE_Mesh mesh;
	List<WB_Polygon> wallPolys = new ArrayList<>();

	public ExtrudeObject(WB_Polygon base, double height) {
		List<WB_Polygon> polys = new ArrayList<>();
		this.base = base;
		polys.add(reversePoly(base));
		HE_Mesh ori = new HEC_FromPolygons(polys).create();
		mesh = new HEM_Extrude().setDistance(height).apply(ori);
		wallPolys = new HEM_Extrude().setDistance(height).setPeak(false).apply(ori).getPolygonList();
		top = base.apply(new WB_Transform3D().addTranslate(base.getNormal()));
	}

	/**
	 * create ExtrudeObject from a single mesh object
	 * 
	 * @param mesh
	 * @param dir  : direction to find base & top
	 */
	public ExtrudeObject(HE_Mesh mesh, WB_Vector dir) {
		this.mesh = mesh.get();

		// join triangle faces on the same plane
		HE_MeshOp.fuseCoplanarFaces(this.mesh);

		// sort mesh polygons, find base, top & walls
		List<WB_Polygon> polys = this.mesh.getPolygonList();
		for (WB_Polygon p : polys) {
			int flag = sameDir(p.getNormal(), dir);
			switch (flag) {
			case 1:
				top = p;
				break;
			case -1:
				base = p;
				break;
			default:
				wallPolys.add(p);
			}
		}
		this.height = getPlaneDis(base, top);
	}

	/**
	 * create ExtrudeObject from a single mesh object along Z axis
	 * @param mesh
	 */
	public ExtrudeObject(HE_Mesh mesh) {
		this(mesh, new WB_Vector(0, 0, 1));
	}

	public WB_Polygon getBase() {
		return base;
	}

	public double getHeight() {
		return height;
	}

	public WB_Polygon getTop() {
		return top;
	}

	public HE_Mesh getMesh() {
		return mesh;
	}

	public List<WB_Polygon> getWallPolys() {
		return wallPolys;
	}

	/**
	 * get distance between 2 parallel polygons
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	private double getPlaneDis(WB_Polygon p1, WB_Polygon p2) {
		WB_Point c1 = p1.getCenter();
		WB_Point pro = WB_GeometryOp.getClosestPoint3D(c1, p2.getPlane());
		return c1.getDistance3D(pro);
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return 1: same -1: reverse 0: other
	 */
	private int sameDir(WB_Vector v1, WB_Vector v2) {
		double threshold = WB_Epsilon.EPSILONANGLE;
		double angle = v1.getAngle(v2);
		if (angle < threshold)
			return 1;
		if (Math.PI - angle < threshold)
			return -1;
		return 0;
	}

	/**
	 * reverse the order of points in a polygon,for mesh operations that needs to
	 * reverse face normal (e.g. {@link HEM_Extrude})
	 * 
	 * @param basePoly
	 * @return
	 */
	private WB_Polygon reversePoly(WB_Polygon basePoly) {
		List<WB_Coord> pts = basePoly.getPoints().toList();
		Collections.reverse(pts);
		return new WB_Polygon(pts);
	}

	@Override
	public void draw(WB_Render3D render) {
		PGraphicsOpenGL app = render.getHome();
		app.fill(GRAY);
		render.drawPolygonEdges(base);
		app.fill(CYAN);
		render.drawPolygonEdges(top);
		app.fill(RICE);
		render.drawPolygonEdges(wallPolys);
	}

}
