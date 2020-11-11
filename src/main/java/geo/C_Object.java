package geo;


import eu.mihosoft.vrl.v3d.CSG;
import util.Display;
import wblut.hemesh.HE_Mesh;
import wblut.processing.WB_Render3D;

public abstract class C_Object implements Display {
    CSG csg;
    HE_Mesh meshToDis;
    public abstract void initCSG();

    public CSG toCSG(){
        return csg;
    };

    @Override
    public void draw(WB_Render3D render) {
        drawMesh(meshToDis,render);
    }
}
