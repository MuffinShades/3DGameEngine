package game;

import java.awt.Color;

public class Pixel {
	public int x,y;
	public float depth;
	Color color;
	
	Pixel (int x, int y, Color c) {
		this.x=x;
		this.y=y;
		this.color=c;
	}
	
	Pixel (int x, int y, float depth, Color c) {
		this.x=x;
		this.y=y;
		this.depth=depth;
		this.color=c;
	}
}
