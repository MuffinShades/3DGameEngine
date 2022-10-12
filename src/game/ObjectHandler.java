package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class ObjectHandler {
	public LinkedList<Object3d> objs = new LinkedList<>();
	public float[] dbuffer = new float[800*600];
	public Object3d[] obj_s = {};
	//public float[] buffer = new BufferCell[800*600];
	
	int i,j;
	
	public void render(Graphics g, Camera cam) {
				
		dbuffer = new float[800*600];
		
		BufferedImage img = new BufferedImage(800,600,BufferedImage.TYPE_INT_ARGB);
		
		Object3d obj;
		
		for (int i = 0; i < obj_s.length; i++) {
			obj = obj_s[i];
			for (int j = 0; j < obj.mc.tris.length; j++) {
				obj.mc.tris[j].x = obj.x;
				obj.mc.tris[j].y = obj.y;
				obj.mc.tris[j].z = obj.z;
				//obj.mc.tris[j].thetaX+=0.1f;
				//obj.mc.tris[j].thetaY+=0.1f;
				obj.mc.tris[j].thetaZ+=0.005f;
			}
			Object3d.drawTriangleList(obj.mc.tris, cam, g, dbuffer, 0, obj.mc.tris.length,img);
;			//objs.get(i).render(g);
			//p.addAll(pixs);
		}
		
		g.drawImage(img,0,0,800,600,null);
		g.drawImage(img,0,0,800,600,null);
	}
	
	public void add(Object3d t) {
		for (int j = 0; j < t.mc.tris.length; j++) {
			t.mc.tris[j].x = t.x;
			t.mc.tris[j].y = t.y;
			t.mc.tris[j].z = t.z;
		}
		
		objs.add(t);
		
		obj_s = objs.toArray(new Object3d[objs.size()]);
	}
}
