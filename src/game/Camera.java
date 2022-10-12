package game;

public class Camera {
	public float x = 0;
	public float y = 0;
	public float z = 0;
	public float zaw = 0;
	public float yaw = 0;
	
	Camera(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public vec3d getVector() {
		return new vec3d(this.x,this.y,this.z);
	}
	
	public void setVector(vec3d v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	
	public vec3d getDirectionVector() {
		vec3d l_dir = new vec3d(0,0,1);
		//vec3d up = new vec3d(0,1,0);
		vec3d target = new vec3d(0,0,1);

		mat4x4 camRotY = mat4x4.MakeRotationMatrixY(this.yaw);
		mat4x4 camRotZ = mat4x4.MakeRotationMatrixX(this.zaw);

		l_dir = mat4x4.MultiplyVector(target, camRotY);
		l_dir = mat4x4.MultiplyVector(l_dir, camRotZ);

		return l_dir;
	}
}
