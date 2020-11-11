package geo;


import eu.mihosoft.vrl.v3d.CSG;

public abstract class C_Object {
    CSG csg;
    public abstract void initCSG();

    public CSG toCSG(){
        return csg;
    };
}
