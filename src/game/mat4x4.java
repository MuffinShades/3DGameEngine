package game;

public class mat4x4 {
	float m[][] = new float[4][4];
	
	mat4x4() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				m[i][j] = 0;
			}
		}
	}
	
	public static vec3d MultiplyVector(vec3d i, mat4x4 m) {
		
		vec3d o = new vec3d();
		
		o.x = i.x * m.m[0][0] + i.y * m.m[1][0] + i.z * m.m[2][0] + i.w * m.m[3][0];
		o.y = i.x * m.m[0][1] + i.y * m.m[1][1] + i.z * m.m[2][1] + i.w * m.m[3][1];
		o.z = i.x * m.m[0][2] + i.y * m.m[1][2] + i.z * m.m[2][2] + i.w * m.m[3][2];
		o.w = i.x * m.m[0][3] + i.y * m.m[1][3] + i.z * m.m[2][3] + i.w * m.m[3][3];
		
		return o;
	}
	
	public static mat4x4 MakeIdentity() {
		mat4x4 mat = new mat4x4();
		mat.m[0][0] = 1.0f;
		mat.m[1][1] = 1.0f;
		mat.m[2][2] = 1.0f;
		mat.m[3][3] = 1.0f;
		return mat;
	}
	
	public static mat4x4 MakeRotationMatrixX(float theta) {
		mat4x4 mx = new mat4x4();
		mx.m[0][0] = 1f;
		mx.m[1][1] = (float)  Math.cos(theta * 0.5f);
		mx.m[1][2] = (float)  Math.sin(theta * 0.5f);
		mx.m[2][1] = (float) -Math.sin(theta * 0.5f);
		mx.m[2][2] = (float)  Math.cos(theta * 0.5f);
		mx.m[3][3] = 1f;
		return mx;
	}
	
	public static mat4x4 MakeRotationMatrixY(float theta) {
		mat4x4 my = new mat4x4();
		my.m[0][0] = (float)  Math.cos(theta);
		my.m[0][2] = (float) -Math.sin(theta);
		my.m[1][1] = 1f;
		my.m[2][0] = (float)  Math.sin(theta);
		my.m[2][2] = (float)  Math.cos(theta);
		my.m[3][3] = 1f;
		return my;
	}
	
	public static mat4x4 MakeRotationMatrixZ(float theta) {
		mat4x4 m = new mat4x4();
		m.m[0][0] = (float)  Math.cos(theta);
		m.m[0][1] = (float)  Math.sin(theta);
		m.m[1][0] = (float) -Math.sin(theta);
		m.m[1][1] = (float)  Math.cos(theta);
		m.m[2][2] = 1f;
		m.m[3][3] = 1f;
		return m;
	}
	
	public static mat4x4 MakeTranslation(vec3d t) {
		mat4x4 m = new mat4x4();
		
		m.m[0][0] = 1.0f;
		m.m[1][1] = 1.0f;
		m.m[2][2] = 1.0f;
		m.m[3][3] = 1.0f;
		m.m[3][0] = t.x;
		m.m[3][1] = t.y;
		m.m[3][2] = t.z;
		
		return m;
	}
	
	public static mat4x4 PointAt(vec3d pos, vec3d target, vec3d up) {
		vec3d nf = vec3d.SubVec(target, pos);
		nf = vec3d.NormalizeVec(nf);
		
		vec3d a = vec3d.MulVec(nf, vec3d.DotProd(up, nf));
		vec3d nu = vec3d.SubVec(up, a);
		nu = vec3d.NormalizeVec(nu);
		
		vec3d nr = vec3d.CrossProd(nu, nf);
		
		mat4x4 m = new mat4x4();
		
		m.m[0][0] = nr.x;
		m.m[1][0] = nu.x;
		m.m[2][0] = nf.x;
		m.m[3][0] = pos.x;
		
		m.m[0][1] = nr.y;
		m.m[1][1] = nu.y;
		m.m[2][1] = nf.y;
		m.m[3][1] = pos.y;
		
		m.m[0][2] = nr.z;
		m.m[1][2] = nu.z;
		m.m[2][2] = nf.z;
		m.m[3][2] = pos.z;
		
		m.m[0][3] = 0.0f;
		m.m[1][3] = 0.0f;
		m.m[2][3] = 0.0f;
		m.m[3][3] = 1.0f;
		
		return m;
	}
	
	public static mat4x4 QuickInverse(mat4x4 m) {
		mat4x4 r = new mat4x4();
		
		r.m[0][0] = m.m[0][0];
		r.m[1][0] = m.m[0][1];
		r.m[2][0] = m.m[0][2];
		r.m[0][1] = m.m[1][0];
		r.m[1][1] = m.m[1][1];
		r.m[2][1] = m.m[1][2];
		r.m[0][2] = m.m[2][0];
		r.m[1][2] = m.m[2][1];
		r.m[2][2] = m.m[2][2];
		r.m[0][3] = 0.0f;
		r.m[1][3] = 0.0f;
		r.m[2][3] = 0.0f;
		r.m[3][3] = 1.0f;
		r.m[3][0] = -(m.m[3][0] * r.m[0][0] + m.m[3][1] * r.m[1][0] + m.m[3][2] * r.m[2][0]);
		r.m[3][1] = -(m.m[3][0] * r.m[0][1] + m.m[3][1] * r.m[1][1] + m.m[3][2] * r.m[2][1]);
		r.m[3][2] = -(m.m[3][0] * r.m[0][2] + m.m[3][1] * r.m[1][2] + m.m[3][2] * r.m[2][2]);
		
		return r;
	}
	
	public static mat4x4 MakeProjection(float fov, float aspectRatio, float near, float far) {
		mat4x4 mproj = new mat4x4();
		float rad = (float) (1.0f / Math.tan(fov * 0.5f / 180.0f * 3.14159f));
		mproj.m[0][0] = aspectRatio * rad;
		mproj.m[1][1] = rad;
		mproj.m[2][2] = far / (far - near);
		mproj.m[3][2] = (-far * near) / (far - near);
		mproj.m[2][3] = 1.0f;
		mproj.m[3][3] = 0.0f;
		return mproj;
	}
	
	public static mat4x4 MultiplyMatrix(mat4x4 m1, mat4x4 m2) {
		mat4x4 o = new mat4x4();
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				o.m[j][i] = m1.m[j][0] * m2.m[0][i] + m1.m[j][1] * m2.m[1][i] + m1.m[j][2] * m2.m[2][i] + m1.m[j][3] * m2.m[3][i];
			}
		}
		
		return o;
	}
}
