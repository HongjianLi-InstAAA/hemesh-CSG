package geo;

import eu.mihosoft.vrl.v3d.Extrude;
import eu.mihosoft.vrl.v3d.Vector3d;
import util.CSGOp;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;

public class C_Extrude extends C_Object {

    private final WB_Polygon basePoly;
    private final WB_Vector dir;
    private final double length;

    public C_Extrude(WB_Polygon basePoly, WB_Vector dir, double length) {
        this.basePoly = basePoly;
        this.dir = dir;
        this.length = length;
    }

    @Override
    public void initCSG() {
        WB_Vector _dir = dir.copy();
        _dir.normalizeSelf();
        _dir.mulSelf(length);
        Vector3d vector3d = new Vector3d(_dir.xd(),_dir.yd(),_dir.zd());
        this.csg = Extrude.points(vector3d,CSGOp.toCSGPoints(basePoly));
    }
}
