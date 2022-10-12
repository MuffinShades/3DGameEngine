package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.muffinshades.Session;

public class Main implements Runnable {
	public  JFrame frame;
	public  JPanel win = new JPanel();
	public boolean running = false;
	public Thread thread;
	public static Main m;
	
	long tmr = System.currentTimeMillis();
	double fps = 0;
	int FPS_VALUE = 120;
	float diff = 0;
	
	Camera renderCam = new Camera(0.0f,0.0f,0.0f);

	public JPanel pan = new JPanel() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5914925757840723819L;


		@Override
		public void paintComponent(Graphics g) {
			if (running) {
				g.clearRect(0, 0, WIDTH, HEIGHT);
				g.setColor(new Color(100,200,255));
				g.fillRect(0, 0, 800,600);
				//m.tick(FPS_VALUE);
				m.render(g);
				fps = 1000 / (System.currentTimeMillis() - tmr);
				tmr = System.currentTimeMillis();
				frame.setTitle("3d Game Engine | FPS: "+fps);
			}

			repaint();
		}
	};

	public  mesh mc;
	public Robot r;
	
	public int lastX = 0;
	public int lastY = 0;

	MouseMotionListener mml = new MouseMotionListener() {

		@Override
		public void mouseMoved(MouseEvent e) {
			//float xDiff = lastX - e.getX();
			//float yDiff = lastY - e.getY();
			
			//lastX = e.getX();
			//lastY = e.getY();

			//cam.zaw -= yDiff * sensitivity;
			//cam.yaw += xDiff * sensitivity;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			float xDiff = lastX - e.getX();
			float yDiff = lastY - e.getY();
			
			lastX = e.getX();
			lastY = e.getY();

			//cam.zaw -= yDiff * sensitivity;
			//cam.yaw += xDiff * sensitivity;
		}
	};
	
	
	public vec3d gl_dir = new vec3d();
	public Object3d cub = new Object3d(0.0f,0.0f,3.0f,1.0f);
	public Object3d cub2 = new Object3d(1.0f,1.0f,3.0f,1.0f);
	public ObjectHandler hand = new ObjectHandler();

	Main() {
		System.out.println(Session.fromFile("data.session", 0xff72a01d));
		
		frame = new Window(800,600,"3d Game Engine").win;

		pan.setPreferredSize(new Dimension(800,600));
		pan.setVisible(true);
		pan.setSize(800,600);

		win.add(pan);

		frame.add(win);
		frame.pack();
		frame.setVisible(true);
		frame.addMouseMotionListener(mml);

		mc = new mesh();

		try {
			r = new Robot();
		} catch (Exception e) {
			e.printStackTrace();
		}

		float[][] p = {

				// SOUTH
				{ 0.0f, 0.0f, 0.0f,    0.0f, 1.0f, 0.0f,    1.0f, 1.0f, 0.0f },
				{ 0.0f, 0.0f, 0.0f,    1.0f, 1.0f, 0.0f,    1.0f, 0.0f, 0.0f },

				// EAST                                                      
				{ 1.0f, 0.0f, 0.0f,    1.0f, 1.0f, 0.0f,    1.0f, 1.0f, 1.0f },
				{ 1.0f, 0.0f, 0.0f,    1.0f, 1.0f, 1.0f,    1.0f, 0.0f, 1.0f },

				// NORTH                                                     
				{ 1.0f, 0.0f, 1.0f,    1.0f, 1.0f, 1.0f,    0.0f, 1.0f, 1.0f },
				{ 1.0f, 0.0f, 1.0f,    0.0f, 1.0f, 1.0f,    0.0f, 0.0f, 1.0f },

				// WEST                                                      
				{ 0.0f, 0.0f, 1.0f,    0.0f, 1.0f, 1.0f,    0.0f, 1.0f, 0.0f },
				{ 0.0f, 0.0f, 1.0f,    0.0f, 1.0f, 0.0f,    0.0f, 0.0f, 0.0f },

				// TOP                                                       
				{ 0.0f, 1.0f, 0.0f,    0.0f, 1.0f, 1.0f,    1.0f, 1.0f, 1.0f },
				{ 0.0f, 1.0f, 0.0f,    1.0f, 1.0f, 1.0f,    1.0f, 1.0f, 0.0f },

				// BOTTOM                                                    
				{ 1.0f, 0.0f, 1.0f,    0.0f, 0.0f, 1.0f,    0.0f, 0.0f, 0.0f },
				{ 1.0f, 0.0f, 1.0f,    0.0f, 0.0f, 0.0f,    1.0f, 0.0f, 0.0f },

		};
		
		//contradicting places
		float[][] p2 = {
				{ 1.0f, 0.5f, 1.0f,    0.0f, 0.5f, 1.0f,    0.0f, 0.5f, 0.0f },
				{ 1.0f, 0.5f, 1.0f,    0.0f, 0.5f, 0.0f,    1.0f, 0.5f, 0.0f },
				
				{ 0.0f, 0.0f, 1.5f,    0.0f, 1.0f, 1.5f,    0.0f, 1.0f, 0.5f },
				{ 0.0f, 0.0f, 1.5f,    0.0f, 1.0f, 0.5f,    0.0f, 0.0f, 0.5f },
		};

		mc.setMesh(p);
		
		//cub.SetMesh(mc);
		//cub2.SetMesh(mc);
		
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 1; j++) {
				for (int k = 0; k < 1; k++) {
					Object3d c = new Object3d(j,i,k+1,1f,cam);
					
					c.SetMesh(mc);
					//c.SetMesh(mesh.FromFile("newthing.obj",false));
					
					hand.add(c);
				}
			}
		}
		
		System.out.println(hand.objs.size());
	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		float tps_count  = 0;
		//create game loop
		long lastTime = System.nanoTime();
		double amountOfTicks = 120.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		float TPS = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				//pan.repaint();
				tps_count+=1;
				delta--;
			}

			//old
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				TPS = tps_count;
				//System.out.println("TPS: "+TPS);
				tps_count = 0;
			}
		}

		stop();
	}

	public float theta = 0;
	public float sensitivity = 0.01f;

	public Camera cam = new Camera(0,0,0);
	public float yaw = 0.0f;
	public float zaw = 0.0f;

	public boolean tup = false, tdown = false, tleft = false, tright = false, mup = false, mdown = false, mleft = false, mright = false, mfor = false, mback = false;
	public float speed = 0.5f;
	public float rotSpeed = 0.02f;
	public vec3d speedDiff = new vec3d(0,0,0);
	vec3d n;
	public void tick() {
		float m = 1;
		
		if (tup) {
			cam.setVector(vec3d.AddVec(cam.getVector(), vec3d.MulVec(gl_dir, speed*m)));
		}

		if (tdown) {
			cam.setVector(vec3d.SubVec(cam.getVector(), vec3d.MulVec(gl_dir, speed*m)));
		}

		if (tleft) {
			cam.yaw -= rotSpeed*m;
		}

		if (tright) {
			cam.yaw += rotSpeed*m;
		}

		if (mup) {
			cam.y -= speed*m;
		}

		if (mdown) {
			cam.y += speed*m;
		}
		

		if (mfor) {
			cam.setVector(vec3d.AddVec(cam.getVector(), vec3d.MulVec(gl_dir, speed*m)));
		}

		if (mback) {
			cam.setVector(vec3d.SubVec(cam.getVector(), vec3d.MulVec(gl_dir, speed*m)));
		}
		
		gl_dir = cam.getDirectionVector();
	}
	
	float v = 0;
	float tx = 210.0f;
	float ty = 210.0f;

	public  void render(Graphics g) {
		renderCam.setVector(cam.getVector());
		renderCam.zaw=cam.zaw;
		renderCam.yaw=cam.yaw;
		/*triangle t = new triangle();
		t.p[0] = new vec3d(tx,ty,0);
		t.p[1] = new vec3d(210.0f,410.0f,0);
		t.p[2] = new vec3d(410.0f,210f,0);*/
		//System.out.println(t.p[0].x+"    "+t.p[0].y+"     "+t.p[1].x+"    "+t.p[1].y+"    "+t.p[2].x+"    "+t.p[2].y);
		//Object3d.FillTriangle(t, Color.WHITE, g);
		//Object3d.GetTrianglePixels(t, Color.WHITE, g);
		//triangle.drawTriangle(new Triangle(), null, g);
		//Object3d.drawGradientLine(0, 0, 100, (int) v, new Color(255,0,0), new Color(0,255,0), g);
		
		//ty += 1.0f;
		
		v += 1f;
		
		/*try {
			thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		hand.render(g, renderCam);
	}


	//keyboard
	public KeyboardHead kb = new KeyboardHead(new Keyboard() {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_W) {
				tup=true;
			}
			if (e.getKeyCode() == KeyEvent.VK_S) {
				tdown=true;
			}
			if (e.getKeyCode() == KeyEvent.VK_A) {
				tleft=true;
			}
			if (e.getKeyCode() == KeyEvent.VK_D) {
				tright=true;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				mup=true;
			}
			if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				mdown=true;
			}
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				mfor=true;
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				mback=true;
			}
		}

		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_W) {
				tup=false;
			}
			if (e.getKeyCode() == KeyEvent.VK_S) {
				tdown=false;
			}
			if (e.getKeyCode() == KeyEvent.VK_A) {
				tleft=false;
			}
			if (e.getKeyCode() == KeyEvent.VK_D) {
				tright=false;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				mup=false;
			}
			if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				mdown=false;
			}
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				mfor=false;
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				mback=false;
			}
		}
	});

	public static void main(String[] args) {
		m = new Main();
		m.kb.Start();
		m.start();
	}
}
