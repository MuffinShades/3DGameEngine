package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
/*
 * 
 * Object File
 * 
 * Contains code to create and draw 3d objects
 * 
 */

public class Object3d {
	public Camera cam = new Camera(0,0,0);
	public float thetaX = 0;
	public float thetaY = 0;
	public float thetaZ = 0;
	public float x = 0;
	public float y = 0;
	public float z = 0;
	public float scale = 1f;
	public mesh mc = new mesh();

	Object3d(float x, float y, float z, float scale) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.scale = scale;
	}

	Object3d(float x, float y, float z, float scale, Camera cam) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.cam = cam;
		this.scale = scale;
	}

	Object3d(float x, float y, float z, float scale, Camera cam, mesh m) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.cam = cam;
		this.mc = m;
		this.scale = scale;
	}

	Object3d() {

	}

	public void render(Graphics g,float[] buffer,BufferedImage img) {
		drawTriangleList(this.mc.tris,this.cam,g,buffer,0,this.mc.tris.length,img);
	}
	
	public static void drawTriangleList(triangle[] tris, Camera cam, Graphics g, float[] buffer,int startIndex,int endIndex,BufferedImage img) {	
		int i,k;
		
		triangle t;
		
		vec3d l_dir,up,target;

		for (i = endIndex; --i >= startIndex;) {
			if (i>=tris.length)break;
			t = tris[i];

			mat4x4 rzMat = mat4x4.MakeRotationMatrixZ(t.thetaX);
			mat4x4 rxMat = mat4x4.MakeRotationMatrixX(t.thetaY);
			mat4x4 ryMat = mat4x4.MakeRotationMatrixY(t.thetaZ);
			mat4x4 transMat = mat4x4.MakeTranslation(new vec3d(t.x, t.y, t.z));
			mat4x4 worldMat = mat4x4.MakeIdentity();

			l_dir = new vec3d(0,0,1);
			up = new vec3d(0,1,0);
			target = new vec3d(0,0,1);

			mat4x4 camRotY = mat4x4.MakeRotationMatrixY(cam.yaw);
			mat4x4 camRotZ = mat4x4.MakeRotationMatrixX(cam.zaw);

			l_dir = mat4x4.MultiplyVector(target, camRotY);
			l_dir = mat4x4.MultiplyVector(l_dir, camRotZ);

			target = vec3d.AddVec(cam.getVector(),l_dir);

			mat4x4 camMat = mat4x4.PointAt(cam.getVector(), target, up);

			mat4x4 viewMat = mat4x4.QuickInverse(camMat);

			worldMat = mat4x4.MultiplyMatrix(rzMat, rxMat);
			worldMat = mat4x4.MultiplyMatrix(worldMat, ryMat);
			worldMat = mat4x4.MultiplyMatrix(worldMat, transMat);

			triangle triTrans = new triangle(new vec3d(0,0,0),new vec3d(0,0,0),new vec3d(0,0,0));

			triangle triView = new triangle(new vec3d(0,0,0),new vec3d(0,0,0),new vec3d(0,0,0));

			triTrans.p[0] = mat4x4.MultiplyVector(t.p[0], worldMat);
			triTrans.p[1] = mat4x4.MultiplyVector(t.p[1], worldMat);
			triTrans.p[2] = mat4x4.MultiplyVector(t.p[2], worldMat);

			//System.out.println(triTrans.p[0].x);

			triangle triProj = new triangle(new vec3d(0,0,0),new vec3d(0,0,0),new vec3d(0,0,0));

			vec3d line = vec3d.SubVec(triTrans.p[1], triTrans.p[0]);
			vec3d line2 = vec3d.SubVec(triTrans.p[2], triTrans.p[0]);

			vec3d normal = vec3d.CrossProd(line, line2);

			normal = vec3d.NormalizeVec(normal);

			double l = Math.sqrt(normal.x*normal.x + normal.y*normal.y + normal.z*normal.z);

			normal.x /= l; normal.y /= l; normal.z /= l;

			//if (normal.z < 0) {

			vec3d vcamray = vec3d.SubVec(triTrans.p[0], cam.getVector());

			if (vec3d.DotProd(normal, vcamray) < 0.0f) {

				vec3d lightDir = new vec3d(0.0f,-2.0f,-1.0f);
				
				//vec3d lightDir=vec3d.AddVec(cam.getDirectionVector(),cam.getVector());

				float l2 = (float) Math.sqrt(lightDir.x*lightDir.x+lightDir.y*lightDir.y+lightDir.z*lightDir.z);
				lightDir.x /= l2;
				lightDir.y /= l2;
				lightDir.z /= l2;

				float dp = lightDir.x * normal.x + lightDir.y * normal.y + lightDir.z * normal.z;

				//dp = (float) (Math.floor(dp * 1000f) / 1000f);

				triView.p[0] = mat4x4.MultiplyVector(triTrans.p[0], viewMat);
				triView.p[1] = mat4x4.MultiplyVector(triTrans.p[1], viewMat);
				triView.p[2] = mat4x4.MultiplyVector(triTrans.p[2], viewMat);

				triangle[] clipped = new triangle[2];

				clipped = vec3d.Clip(new vec3d(0.0f,0.0f,0.1f), new vec3d(0.0f,0.0f,1.0f), triView);

				for (k = clipped.length; --k >=0;) {

					triProj = new triangle(new vec3d(0,0,0),new vec3d(0,0,0),new vec3d(0,0,0));

					triProj.p[0] = Project.project(clipped[k].p[0]);
					triProj.p[1] = Project.project(clipped[k].p[1]);
					triProj.p[2] = Project.project(clipped[k].p[2]);

					if (triProj.p[0].w != 0 && triProj.p[1].w != 0 && triProj.p[2].w != 0) {
						triProj.p[0] = vec3d.DivVec(triProj.p[0], triProj.p[0].w);
						triProj.p[1] = vec3d.DivVec(triProj.p[1], triProj.p[1].w);
						triProj.p[2] = vec3d.DivVec(triProj.p[2], triProj.p[2].w);
					}

					triProj.p[0].x += 1f;
					triProj.p[0].y += 1f;

					triProj.p[1].x += 1f;
					triProj.p[1].y += 1f;

					triProj.p[2].x += 1f;
					triProj.p[2].y += 1f;

					triProj.p[0].x *= 0.5f * 800;
					triProj.p[0].y *= 0.5f * 600;
					triProj.p[1].x *= 0.5f * 800;
					triProj.p[1].y *= 0.5f * 600;
					triProj.p[2].x *= 0.5f * 800;
					triProj.p[2].y *= 0.5f * 600;
					
					//triangle.drawTriangle(triProj, getColor(dp), g);

					triProj.p[0].x -= (float) (800/12);
					triProj.p[0].y -= (float) (600/8);
					triProj.p[1].x -= (float) (800/12);
					triProj.p[1].y -= (float) (600/8);
					triProj.p[2].x -= (float) (800/12);
					triProj.p[2].y -= (float) (600/8);
					
					int n;
					
					GetTrianglePixels(triProj, applyBrightness(dp,t.color), g, buffer, img);

					/*for (n = 4; --n >= 0;) {
						triangle[] temp = new triangle[0];

						switch (n) {
						case 0:
							temp = vec3d.Clip(new vec3d(0.0f,0.0f,0.0f), new vec3d(0.0f,1.0f,0.0f), triProj);
							break;
						case 1:
							temp = vec3d.Clip(new vec3d(0.0f,1f,0.0f), new vec3d(0.0f,-1.0f,0.0f), triProj);
							break;
						case 2:
							temp = vec3d.Clip(new vec3d(0.0f,0.0f,0.0f), new vec3d(1.0f,0.0f,0.0f), triProj);
							break;
						case 3:
							temp = vec3d.Clip(new vec3d(1f,0.0f,0.0f), new vec3d(-1.0f,0.0f,0.0f), triProj);
							break;
						}
						
						for (int b = 0; b < temp.length; b++) {
							//triangle.drawTriangle(temp[b], getColor(dp), g);
							
							
							GetTrianglePixels(temp[b], applyBrightness(dp,temp[b].color), g, buffer, img);
						}
					}*/
				}
			}
		}
	}
	
	public static void GetTrianglePixels(triangle t,Color c, Graphics g, float[] buffer, BufferedImage img) {

		//g.setColor(c);
		float x1 = t.p[0].x;
		float y1 = t.p[0].y;
		float x2 = t.p[1].x;
		float y2 = t.p[1].y;
		float x3 = t.p[2].x;
		float y3 = t.p[2].y;
		
		
		
		/*float x1 = Math.max(Math.min(t.p[0].x,800f),0f);
		float y1 = Math.max(Math.min(t.p[0].y,600f),0f);
		float x2 = Math.max(Math.min(t.p[1].x,800f),0f);
		float y2 = Math.max(Math.min(t.p[1].y,600f),0f);
		float x3 = Math.max(Math.min(t.p[2].x,800f),0f);
		float y3 = Math.max(Math.min(t.p[2].y,600f),0f);*/
		float d1 = t.p[0].z;
		float d2 = t.p[1].z;
		float d3 = t.p[2].z;
		
		if (y2 < y1) {
			float temp = y1;
			y1 = y2;
			y2 = temp;
			
			float temp2 = x1;
			x1 = x2;
			x2 = temp2;
			
			float temp3 = d1;
			d1 = d2;
			d2 = temp3;
		}
		
		if (y3 < y1) {
			float temp = y1;
			y1 = y3;
			y3 = temp;
			
			float temp2 = x1;
			x1 = x3;
			x3 = temp2;
			
			float temp3 = d1;
			d1 = d3;
			d3 = temp3;
		}
		
		if (y3 < y2) {
			float temp = y2;
			y2 = y3;
			y3 = temp;
			
			float temp2 = x2;
			x2 = x3;
			x3 = temp2;
			
			float temp3 = d2;
			d2 = d3;
			d3 = temp3;
		}
		
		if ((x1<0&&x2<0&&x3<0) || (x1>800&&x2>800&&x3>800) || (y3<0) || (y1>600))return;
		
		Pixel[] p1 = drawGradientLine((int) x1, (int) y1, d1, (int) x2, (int) y2, d2, c, c, g,false, img, buffer);
		Pixel[] p2 = drawGradientLine((int) x2, (int) y2, d2, (int) x3, (int) y3, d3, c, c, g,false, img, buffer);
		Pixel[] p3 = drawGradientLine((int) x1, (int) y1, d1, (int) x3, (int) y3, d3, c, c, g,false, img, buffer);
		
		Pixel i1;
		Pixel i2;
		
		//rasterize top half of triangle by drawing a line between left hand side and right hand side of triangle
		//System.out.println(p1.length);
		
		for (int i = 0; i < p1.length; i++) {
			i1 = p1[i];
			i2 = p3[i];
			if (i1 != null && i2 != null) {
				//if (i1.x > 800 || i1.y > 600 || i2.x > 800 || i2.y > 600) break;
				getGradientPixels(i1.x,i1.y,i1.depth,i2.x,i2.y,i2.depth,c,c,g,buffer,img);
			}
		}
		
		
		//rasterize bottom half by going from point 2 to bottom half of point 3
		for (int i = 0; i < p2.length; i++) {
			i1 = p2[i];
			i2 = p3[i+(p1.length-1)];
			if (i1 != null && i2 != null) {
				//if (i1.x > 800 || i1.y > 600 || i2.x > 800 || i2.y > 600) break;
				getGradientPixels(i1.x,i1.y,i1.depth,i2.x,i2.y,i2.depth,c,c,g,buffer,img);
			}
		}
	}
	
	public static Pixel[] drawGradientLine(int x1, int y1, float d1, int x2, int y2, float d2, Color c1, Color c2, Graphics g, boolean keepIn, BufferedImage img, float[] buffer) {
		/*
		 * Used to draw gradient line
		 * 
		 */
		/*y2 = Math.max(y2, 0);
		y1 = Math.max(y1, 0);
		x2 = Math.max(x2, 0);
		x1 = Math.max(x1, 0);
		y2 = Math.min(y2, 800);
		y1 = Math.min(y1, 800);
		x2 = Math.min(x2, 800);
		x1 = Math.min(x1, 800);*/
		
		int dx = Math.abs(x2 - x1);
		int dy = Math.abs(y2 - y1);
		
		int sx = x1 < x2 ? 1 : -1;
		int sy = y1 < y2 ? 1 : -1;
		
		int er = dx - dy;
		int er2;
		
		/*float slope = dx != 0 ? dy/dx : 1;
		
		if (x1 < 0) {
			y1 = (int) (y1 - (slope * x1));
			x1 = 0;
		}
		
		dx = Math.abs(x2 - x1);
		dy = Math.abs(y2 - y1);
		
		if (x2 < 0) {
			y2 = (int) (y1 - (slope * x2));
			x2 = 0;
		}
		
		dx = Math.abs(x2 - x1);
		dy = Math.abs(y2 - y1);
		
		if (x1 > 800) {
			y1 = (int) ((slope * 800) + (y1 - (slope * x1)));
			x1 = 800;
		}
		
		dx = Math.abs(x2 - x1);
		dy = Math.abs(y2 - y1);
		
		if (x2 > 800) {
			y2 = (int) ((slope * 800) + (y1 - (slope * x2)));
			x2 = 800;
		}
		
		dx = Math.abs(x2 - x1);
		dy = Math.abs(y2 - y1);
		
		if (y1 < 0) {
			x1 = (int) ((-(y1 - (slope * x1)) + y1) / slope);
			y1 = 0;
		}
		
		dx = Math.abs(x2 - x1);
		dy = Math.abs(y2 - y1);
		
		if (y2 < 0) {
			x2 = (int) ((-(y2 - (slope * x2)) + y2) / slope);
			y2 = 0;
		}
		
		dx = Math.abs(x2 - x1);
		dy = Math.abs(y2 - y1);
		
		if (y1 > 800) {
			x1 = (int) ((-(y1 - (slope * x1)) + y1) / slope);
			y1 = 800;
		}
		
		dx = Math.abs(x2 - x1);
		dy = Math.abs(y2 - y1);
		
		if (y2 > 800) {
			x2 = (int) ((-(y2 - (slope * x2)) + y2) / slope);
			y2 = 800;
		}
		
		dx = Math.abs(x2 - x1);
		dy = Math.abs(y2 - y1);*/
		
		//save the x y positions in a list
		LinkedList<Integer> xpos = new LinkedList<Integer>();
		LinkedList<Integer> ypos = new LinkedList<Integer>();
		
		int lastY = y1;
		
		int pixIndex = 0;
		
		while (true) {
			
			lastY = y1;
			
			//add x, y positon to array
			xpos.add(x1);
			ypos.add(y1);
			
			if (x1 == x2 && y1 == y2) {
				break;
			}
			
			er2 = 2 * er;
			
			if (er2 > -dy) {
				er = er - dy;
				
				x1 = x1 + sx;
			}
			
			if (er2 < dx) {
				er = er + dx;
				
				y1 = y1 + sy;
			}
		}
		
		/*int r1 = c1.getRed();
		int g1 = c1.getGreen();
		int b1 = c1.getBlue();
		
		int r2 = c2.getRed();
		int g2 = c2.getGreen();
		int b2 = c2.getBlue();*/
		
		int p = 0;
		
		int d = Math.max(xpos.size(),ypos.size());
		

		LinkedList<Pixel> pixs = new LinkedList<Pixel>();
		
		boolean ft = true;
		
		pixs.add(null);
		
		Color gradColor;
		
		int x;
		int y;
		
		Pixel[] cast = new Pixel[pixs.size()];
		
		for (int k = 0; k < xpos.size(); k++) {
			p++;
			
			x=xpos.get(k);
			y=ypos.get(k);
			
			//calculate gradient
			
			float Gradd = (((float) d1 / (float) d) * (d - p) + ((float) d2 / (float) d) * p);
			
			Gradd = (((float) d1 / d) * (d - k) + ((float) d2 / d) * k);
			
			//gradColor = new Color(0,0,0);
			gradColor = c1;
			
			if (x+(y * 800) <= buffer.length && x > 0 && y > 0 && x < 800 && y < 600) {
				if (buffer[x+(y*800)] > 0.0f) {
					if (buffer[x+(y * 800)] > Gradd) {
						buffer[x+(y * 800)] = Gradd;
					
						img.setRGB(x, y, gradColor.getRGB());
					}
				} else {
					buffer[x+(y * 800)] = Gradd;
				
					img.setRGB(x, y, gradColor.getRGB());
				}
			}
			
			
			if (k > 0) lastY = ypos.get(k-1);
			
			if (!keepIn) {
				if (!ft) {
					if (pixs.get(pixIndex).x < x) {
						pixs.set(pixIndex, new Pixel(x,y,Gradd,gradColor));
					}
					ft = false;
				} else {
					pixs.set(pixIndex, new Pixel(x,y,Gradd,gradColor));
				}
			} else {
				if (!ft) {
					if (pixs.get(pixIndex).x > x) {
						pixs.set(pixIndex, new Pixel(x,y,Gradd,gradColor));
					}
					ft = false;
				} else {
					pixs.set(pixIndex, new Pixel(x,y,Gradd,gradColor));
				}
			}
			
			if (lastY < ypos.get(k)) {
				pixIndex++;
				pixs.add(null);
				ft = true;
			}
		}
		
		return pixs.toArray(cast);
	}
	
	public static void getGradientPixels(int x1, int y1, float d1, int x2, int y2, float d2, Color c1, Color c2, Graphics g,float[] buffer,BufferedImage img) {
		/*
		 * Used to draw gradient line
		 * 
		 */
		
		if ((x1 < 0 && x2 < 0) || (x1 > 800 && x2 > 800) || (y1 < 0 && y2 < 0) || (y1 > 600 && y2 > 600)) return;
		
		int dx = Math.abs(x2 - x1);
		int dy = Math.abs(y2 - y1);
		
		float slope = dx != 0 ? dy/dx : 1;
		
		//save the x y positions in a list
		//LinkedList<Integer> xpos = new LinkedList<Integer>();
		//LinkedList<Integer> ypos = new LinkedList<Integer>();
		
		int[] xpos = new int[dx+dy+1];
		int[] ypos = new int[dx+dy+1];
		
		int dex = 0;
		
		if (x1 < 0) {
			y1 = (int) (y1 - (slope * x1));
			x1 = 0;
		}
		
		dx = Math.abs(x2 - x1);
		dy = Math.abs(y2 - y1);
		
		if (x2 < 0) {
			y2 = (int) (y1 - (slope * x2));
			x2 = 0;
		}
		
		dx = Math.abs(x2 - x1);
		dy = Math.abs(y2 - y1);
		
		if (x1 > 800) {
			y1 = (int) ((slope * 800) + (y1 - (slope * x1)));
			x1 = 800;
		}
		
		dx = Math.abs(x2 - x1);
		dy = Math.abs(y2 - y1);
		
		if (x2 > 800) {
			y2 = (int) ((slope * 800) + (y1 - (slope * x2)));
			x2 = 800;
		}
		
		dx = Math.abs(x2 - x1);
		dy = Math.abs(y2 - y1);
		
		if (y1 < 0) {
			x1 = (int) ((-(y1 - (slope * x1)) + y1) / slope);
			y1 = 0;
		}
		
		dx = Math.abs(x2 - x1);
		dy = Math.abs(y2 - y1);
		
		if (y2 < 0) {
			x2 = (int) ((-(y2 - (slope * x2)) + y2) / slope);
			y2 = 0;
		}
		
		dx = Math.abs(x2 - x1);
		dy = Math.abs(y2 - y1);
		
		if (y1 > 800) {
			x1 = (int) ((-(y1 - (slope * x1)) + y1) / slope);
			y1 = 800;
		}
		
		dx = Math.abs(x2 - x1);
		dy = Math.abs(y2 - y1);
		
		if (y2 > 800) {
			x2 = (int) ((-(y2 - (slope * x2)) + y2) / slope);
			y2 = 800;
		}
		
		dx = Math.abs(x2 - x1);
		dy = Math.abs(y2 - y1);
		
		int sx = x1 < x2 ? 1 : -1;
		int sy = y1 < y2 ? 1 : -1;
		
		int er = dx - dy;
		int er2;
		
		while (true) {
			
			//add x, y positon to array
			xpos[dex] = x1;
			ypos[dex] = y1;
			
			if (x1 == x2 && y1 == y2) {
				break;
			}
			
			er2 = er << 1;
			
			if (er2 > -dy) {
				er = er - dy;
				
				x1 = x1 + sx;
			}
			
			if (er2 < dx) {
				er = er + dx;
				
				y1 = y1 + sy;
			}
			
			dex++;
		}
		
		//int r1 = c1.getRed();
		//int g1 = c1.getGreen();
		//int b1 = c1.getBlue();
		
		float d = xpos.length;
		
		int gradColor;
		
		float Gradd;
		
		int x;
		int y;
		
		for (int k = 0; k < dex; k++) {
			x = xpos[k];
			y = ypos[k];
			if (x < 800 && y < 600 && x > 0  && y > 0) {
				
				Gradd = (((float) d1 / d) * (d - k) + ((float) d2 / d) * k);
				
				gradColor = c1.getRGB();
				
				
				if (buffer[x+(y*800)] > 0.0f) {
					if (buffer[x+(y * 800)] > Gradd) {
						buffer[x+(y * 800)] = Gradd;
						
						img.setRGB(x, y, gradColor);
					}
				} else {
					buffer[x+(y * 800)] = Gradd;
					
					img.setRGB(x, y, gradColor);
				}
			}
		}
	}

	public static Color getColor(float v) {
		float n = (v+1)/2;

		int a = (int) (n * 128);

		return new Color(128+a, 128+a, 128+a);
	}
	
	public static Color applyBrightness(float v, Color c) {
		float n = (v+1)/2;
		
		int a = (int) (n * 128);
		
		return new Color((int) ((c.getRed() + a) / 2f),(int) ((c.getGreen() + a) / 2f),(int) ((c.getBlue() + a) / 2f));
	}

	public void SetMesh(mesh m) {
		m.modifyPosition(new vec3d(this.x,this.y,this.z));
		m.scale(new vec3d(this.scale,this.scale,this.scale));
		this.mc = m;
	}
	
	public void setCamera(Camera c) {
		this.cam = c;
	}

	public void SetGlDir(vec3d d) {};
}
