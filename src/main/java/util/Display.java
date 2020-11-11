package util;

import processing.opengl.PGraphicsOpenGL;
import wblut.hemesh.HE_Mesh;
import wblut.processing.WB_Render3D;

import java.awt.*;

/**
 * interface for objects to display
 * 
 * @author JianZ
 *
 */
public interface Display {
	int PINK = 0xFFffa69e;
	int RICE = 0xFFfaf3dd;
	int CYAN = 0xFFb8f2e6;
	int BLUE = 0xFFaed9e0;
	int GRAY = 0xFF5e6472;
	int WHITE = 0xFFFFFFFF;
	int BLACK = 0xFF000000;
	int RED = 0xFFFF0000;


	int[] ALL = new int[] { PINK, RICE, CYAN, BLUE, GRAY,WHITE,BLACK };

	default int[] randCol(int n) {
		int[] col = new int[n];
		for (int i = 0; i < n; i++) {
			int c = (int) (Math.random() * ALL.length);
			col[i] = ALL[c];
		}
		return col;
	}

	default int randCol() {
		int[] col = randCol(1);
		return col[0];
	}

	/**
	 * render mesh in white
	 * @param mesh
	 * @param render
	 */
	default void drawMesh(HE_Mesh mesh, WB_Render3D render){
		drawMesh(mesh,render,WHITE);
	}

	/**
	 *
	 * @param mesh
	 * @param render
	 * @param fillColor
	 */
	default void drawMesh(HE_Mesh mesh, WB_Render3D render, int fillColor) {
		PGraphicsOpenGL app = render.getHome();
		app.pushStyle();
		app.stroke(0);
		render.drawEdges(mesh);
		app.noStroke();
		app.fill(fillColor);
		render.drawFaces(mesh);
		app.popStyle();
	}

	default void drawMesh(HE_Mesh mesh, WB_Render3D render,int...rgba){
		drawMesh(mesh,render,RGBtoInt(rgba));
	}

	/**
	 * draw face normal and display boundary edge in RED
	 * @param mesh
	 * @param render
	 */
	default void drawMeshAnalysis(HE_Mesh mesh, WB_Render3D render){
		drawMeshAnalysis_Edge(mesh, render);
		drawMeshAnalysis_Face(mesh, render);
	}

	default void drawMeshAnalysis_Edge(HE_Mesh mesh, WB_Render3D render){
		PGraphicsOpenGL app = render.getHome();
		app.pushStyle();
		app.strokeWeight(2);
		app.stroke(RED);
		render.drawBoundaryEdges(mesh);
		app.popStyle();
	}

	default void drawMeshVertices(HE_Mesh mesh, WB_Render3D render){
		render.drawVertices(mesh);
	}
	default void drawMeshAnalysis_Face(HE_Mesh mesh, WB_Render3D render){

	}

	default int RGBtoInt(int...rgba) {
		switch (rgba.length){
			case 1:
				return new Color(rgba[0]).getRGB();
			case 2:
				return new Color(rgba[0],rgba[0],rgba[0],rgba[1]).getRGB();
			case 3:
				return new Color(rgba[0],rgba[1],rgba[2]).getRGB();
			case 4:
				return new Color(rgba[0],rgba[1],rgba[2],rgba[3]).getRGB();
			default:
				return Color.RED.getRGB();
		}
	}


	public abstract void draw(WB_Render3D render);
}
