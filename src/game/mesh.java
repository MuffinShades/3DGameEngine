package game;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class mesh {
	public triangle[] tris = {};
	
	mesh() {
		
	}
	
	public void setMesh(float[][] v) {
		this.tris = new triangle[v.length];
		
		for (int i = 0; i < v.length; i++) {
			triangle t = new triangle();
			int idx = 0;
			
			for (int j = 0; j < v[i].length; j+=3) {
				t.p[idx] = new vec3d(v[i][j],v[i][j+1],v[i][j+2]);
				
				idx++;
			}
			
			tris[i] = t;
		}
	}
	
	public void setMesh(float[][] v, vec3d o) {
		this.tris = new triangle[v.length];
		
		for (int i = 0; i < v.length; i++) {
			triangle t = new triangle();
			int idx = 0;
			
			for (int j = 0; j < v[i].length; j+=3) {
				t.p[idx] = new vec3d(v[i][j],v[i][j+1],v[i][j+2]);
				
				idx++;
			}
			
			t.x = o.x;
			t.y = o.y;
			t.z = o.z;
			
			tris[i] = t;
		}
	}
	
	public void modifyPosition(vec3d p) {
		for (int i = 0; i < this.tris.length; i++) {
			this.tris[i].x = p.x;
			this.tris[i].y = p.y;
			this.tris[i].z = p.z;
		}
	}
	
	public static mesh FromFile(String src, boolean mode) {
		File f = new File((new File("").getAbsolutePath())+"\\src\\game\\"+src);
		
		mesh m = new mesh();
		
		LinkedList<triangle> tris = new LinkedList<>();
		LinkedList<vec3d> points = new LinkedList<>();
		
		System.out.println("loading...");
		
		int r = 255, g = 0, b = 0;
		
		if (mode) {
			try {
				Scanner sc = new Scanner(f);
				
				while(sc.hasNextLine()) {
					String ln = sc.nextLine();
					
					if (ln.startsWith("v ")) {
						String[] vals = ln.substring(2).split(" ");
						
						for (int i = 0; i < vals.length; i++) {
							System.out.println(vals[i]);
							
							System.out.println(Float.parseFloat(vals[i]));
						}
						
						if (vals[0] != "" && vals[1] != "" && vals[2] != "") {
							points.add(new vec3d(Float.parseFloat(vals[0]),Float.parseFloat(vals[1]),Float.parseFloat(vals[2])));
						}
					} else if (ln.startsWith("f ")) {
						String[] sets = ln.substring(2).split(" ");
						
						for (int i = 0; i < sets.length; i++) {
							String[] pnts = sets[i].split("\\/");
							
							System.out.println(Integer.parseInt(pnts[0])-1);
							
							tris.add(new triangle(points.get(Integer.parseInt(pnts[0])-1),points.get(Integer.parseInt(pnts[1])-1),points.get(Integer.parseInt(pnts[2])-1)));
						}
					}
				}
				
				sc.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			triangle t;
			int tnum=0;
			try {
				Scanner sc = new Scanner(f);
				
				while(sc.hasNextLine()) {
					String ln = sc.nextLine();
					
					if (ln.startsWith("v ")) {
						String[] vals = ln.substring(2).split(" ");
						
						for (int i = 0; i < vals.length; i++) {
							System.out.println(vals[i]);
							
							System.out.println(Float.parseFloat(vals[i]));
						}
						
						if (vals[0] != "" && vals[1] != "" && vals[2] != "") {
							points.add(new vec3d(Float.parseFloat(vals[0]),Float.parseFloat(vals[1]),Float.parseFloat(vals[2])));
						}
					} else if (ln.startsWith("f ")) {
						String[] pnts = ln.substring(2).split(" ");
						
						t = new triangle(points.get(Integer.parseInt(pnts[0])-1),points.get(Integer.parseInt(pnts[1])-1),points.get(Integer.parseInt(pnts[2])-1));
						
						if (r >= 255 && g < 255 && b <= 0) {
							g++;
						} else if (r >= 1 && g >= 255) {
							r--;
						} else if (r <= 0 && b < 255) {
							b++;
						} else if (b >= 255 && g >= 1) {
							g--;
						} else if (g <= 0 && r < 255) {
							r++;
						} else if (r >= 255 && b >= 1) {
							b--;
						}
						
						t.color = new Color(r,g,b);
						
						tris.add(t);
						
						System.out.println("Tri: "+tnum);
						tnum++;
					}
				}
				
				sc.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("done!");
		
		m.tris = tris.toArray(new triangle[tris.size()]);
		
		return m;
	}
	
	public void scale(vec3d s) {
		for (int i = 0; i < this.tris.length; i++) {
			this.tris[i].x *= s.z;
			this.tris[i].y *= s.y;
			this.tris[i].z *= s.z;
		}
	}
}
