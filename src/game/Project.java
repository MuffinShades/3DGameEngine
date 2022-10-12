package game;

public class Project {
	public static vec3d project(vec3d i) {
		float near = 0.1f;
		float far = 1000.0f;
		float fov = 90.0f;
		float aspectRatio = ((float)600 / (float)800);
		float rad = (float) (1.0f / Math.tan(fov * 0.5f / 180.0f * 3.14159f));
		
		mat4x4 mproj = new mat4x4();
		
		mproj.m[0][0] = aspectRatio * rad;
		mproj.m[1][1] = rad;
		mproj.m[2][2] = far / (far - near);
		mproj.m[3][2] = (-far * near) / (far - near);
		mproj.m[2][3] = 1.0f;
		mproj.m[3][3] = 0.0f;
		
		return mat4x4.MultiplyVector(i, mproj);
	}
	
	public static void rotateZ(vec3d i, vec3d o, double theta) {
		mat4x4 m = new mat4x4();
		
		m.m[0][0] = (float)  Math.cos(theta);
		m.m[0][1] = (float)  Math.sin(theta);
		m.m[1][0] = (float) -Math.sin(theta);
		m.m[1][1] = (float)  Math.cos(theta);
		m.m[2][2] = 1f;
		m.m[3][3] = 1f;
		
		o = mat4x4.MultiplyVector(i, m);
	}
	
	public static void rotateX(vec3d i, vec3d o, double theta) {
		mat4x4 mx = new mat4x4();
		
		mx.m[0][0] = 1f;
		mx.m[1][1] = (float)  Math.cos(theta * 0.5f);
		mx.m[1][2] = (float)  Math.sin(theta * 0.5f);
		mx.m[2][1] = (float) -Math.sin(theta * 0.5f);
		mx.m[2][2] = (float)  Math.cos(theta * 0.5f);
		mx.m[3][3] = 1f;
		
		o = mat4x4.MultiplyVector(i, mx);
	}
	
	public static void rotateY(vec3d i, vec3d o, double theta) {
		mat4x4 my = new mat4x4();
		
		my.m[0][0] = (float)  Math.cos(theta);
		my.m[0][2] = (float) -Math.sin(theta);
		my.m[1][1] = 1f;
		my.m[2][0] = (float)  Math.sin(theta);
		my.m[2][2] = (float)  Math.cos(theta);
		my.m[3][3] = 1f;
		
		o = mat4x4.MultiplyVector(i, my);
	}
	
	
}
