package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class triangle {
	//pj is the projeected cordinate
	
	public vec3d[] p = new vec3d[3];
	
	public float thetaX = 0.0f;
	public float thetaY = 0.0f;
	public float thetaZ = 0.0f;
	public float x = 0.0f, y = 0.0f, z = 0.0f;
	public Color color = new Color(255,255,255);
	
	triangle() {
	}
	
	triangle(vec3d p1, vec3d p2, vec3d p3) {
		this.p[0]=p1;
		this.p[1]=p2;
		this.p[2]=p3;
	}
	
	public static void drawTriangle(triangle t, Color c, Graphics g) {
		g.setColor(c);
		
		Polygon p = new Polygon();
		
		p.addPoint((int) t.p[0].x, (int) t.p[0].y);
		p.addPoint((int) t.p[1].x, (int) t.p[1].y);
		p.addPoint((int) t.p[2].x, (int) t.p[2].y);
		
		g.fillPolygon(p);
		
		g.setColor(new Color(0,0,0));
		
		g.drawLine((int) t.p[0].x,(int) t.p[0].y,(int) t.p[1].x,(int) t.p[1].y);
		g.drawLine((int) t.p[1].x,(int) t.p[1].y,(int) t.p[2].x,(int) t.p[2].y);
		g.drawLine((int) t.p[2].x,(int) t.p[2].y,(int) t.p[0].x,(int) t.p[0].y);
	}
}
