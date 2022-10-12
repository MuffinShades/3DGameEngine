package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class RenderThread implements Runnable {
	public triangle[] list = {};
	public int start = 0;
	public int end = 0;
	public Graphics g;
	public float[] buffer;
	public Camera cam;
	public BufferedImage img;
	
	RenderThread(triangle[] list,Camera cam,Graphics g,float[] buffer,int start,int end,BufferedImage img) {
		this.list = list;
		this.start = start;
		this.end = end;
		this.g = g;
		this.buffer = buffer;
		this.cam=cam;
		this.img=img;
		
	}
	
	public void run() {
		Object3d.drawTriangleList(this.list,cam,g,buffer,start,end,img);
	}
}
