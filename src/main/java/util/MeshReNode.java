package util;

import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.hemesh.*;
import wblut.math.WB_Epsilon;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *class to add point on edge and rebuild mesh, in order to get a closed mesh
 * @author JianZ
 */
public class MeshReNode {
	HE_Mesh ori, reNoded;

	public MeshReNode(HE_Mesh ori) {
		this.ori = ori;
		reNode();
	}

	public HE_Mesh getReNoded() {
		return reNoded;
	}

	private void reNode() {
		List<HE_Vertex> vers = ori.getAllBoundaryVertices();
		List<HE_Halfedge> edges = ori.getAllBoundaryHalfedges();

		List<WB_Polygon> polys = new ArrayList<>();

		// process all boundary edges
		for (HE_Halfedge edge : edges) {
			
			edge = edge.getPair();
			
			if (edge.getFace().isVisited())
				continue;
			List<WB_Point> pts = reNodePts(edge, vers);
			if (pts == null)
				continue;

			pts.add(edge.getEndPosition());

			// add all points that need to be add to edge, which is on the edges of the same
			// face
			int num = edge.getFace().getFaceHalfedges().size();
			for (int i = 1; i < num; i++) {
				HE_Halfedge next = edge.getNextInFace(i);
				List<WB_Point> ptsBuffer = reNodePts(next, vers);
				if (ptsBuffer != null)
					pts.addAll(ptsBuffer);
				pts.add(next.getEndPosition());
			}

			// add new polygon
			polys.add(new WB_Polygon(pts));

			// label original face
			edge.getFace().setVisited();
		}

		// add other face polygon that don't need to reNode
		for (HE_Face f : ori.getFaces()) {
			if (f.isVisited())
				continue;
			polys.add(f.getPolygon());
		}

		reNoded = new HEC_FromPolygons(polys).create();
	}

	/**
	 * add vertices on the edge segment to the face
	 * @param edge
	 * @param vers
	 * @return
	 */
	private List<WB_Point> reNodePts(HE_Halfedge edge, List<HE_Vertex> vers) {
		List<WB_Point> ptsToAdd = new ArrayList<>();
		for (HE_Vertex ver : vers) {
			if (onEdge(edge, ver))
				ptsToAdd.add(ver.getPosition());
		}
		if (ptsToAdd.size() == 0) {
			return null;
		}

		ptsToAdd.sort(new Comparator<WB_Point>() {
			@Override
			public int compare(WB_Point o1, WB_Point o2) {
				// TODO Auto-generated method stub
				double dis1 = o1.getDistance3D(edge.getStartPosition());
				double dis2 = o2.getDistance3D(edge.getStartPosition());
				return Double.compare(dis1, dis2);
			}
		});

		return ptsToAdd;
	}

	private boolean onEdge(HE_Halfedge edge, HE_Vertex ver) {
		WB_Vector vEdge = edge.getEndPosition().subToVector3D(edge.getStartPosition());
		WB_Vector vVer = ver.getPosition().subToVector3D(edge.getStartPosition());
		if (vEdge.getAngle(vVer) < WB_Epsilon.EPSILONANGLE) {
			return vVer.getLength3D() < vEdge.getLength3D();
		}
		return false;
	}

}
