package geo;

import util.CSGOp;
import wblut.hemesh.HE_Mesh;

public class C_Mesh extends C_Object{
    HE_Mesh mesh;

    public C_Mesh(HE_Mesh mesh){
        this.mesh = mesh;
        initCSG();
    }
    @Override
    public void initCSG() {
        this.csg = CSGOp.toCSG(mesh);
    }
}
