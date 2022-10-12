package game;

import java.awt.Color;

public class vec3d {
	public float x=0,y=0,z=0,w=1;
	
	vec3d(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	vec3d(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	vec3d() {
		
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public float getZ() {
		return this.z;
	}
	
	public void setX(float v) {
		this.x = v;
	}
	
	public void setY(float v) {
		this.y = v;
	}
	
	public void setZ(float v) {
		this.z = v;
	}
	
	public static vec3d AddVec(vec3d v1, vec3d v2) {
		return new vec3d(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}
	
	public static vec3d SubVec(vec3d v1, vec3d v2) {
		return new vec3d(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
	}
	
	public static vec3d MulVec(vec3d v1, vec3d v2) {
		return new vec3d(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z);
	}
	
	public static vec3d MulVec(vec3d v1, float v2) {
		return new vec3d(v1.x * v2, v1.y * v2, v1.z * v2);
	}
	
	public static vec3d DivVec(vec3d v1, vec3d v2) {
		return new vec3d(v1.x / v2.x, v1.y / v2.y, v1.z / v2.z);
	}
	
	public static vec3d DivVec(vec3d v1, float v2) {
		return new vec3d(v1.x / v2, v1.y / v2, v1.z / v2);
	}
	
	public static float DotProd(vec3d v1, vec3d v2) {
		return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
	}
	
	public static float VecLength(vec3d v) {
		return (float) Math.sqrt(DotProd(v,v));
	}
	
	public static vec3d NormalizeVec(vec3d v) {
		float l = VecLength(v);
		return new vec3d(v.x / l, v.y / l, v.z / l);
	}
	
	public static vec3d CrossProd(vec3d v1, vec3d v2) {
		return new vec3d(
			v1.y * v2.z - v1.z * v2.y,
			v1.z * v2.x - v1.x * v2.z,
			v1.x * v2.y - v1.y * v2.x
		);
	}
	
	public static vec3d IntersectPlane(vec3d plane, vec3d pn, vec3d lns, vec3d lne) {
		pn = NormalizeVec(pn);
		float pd = -DotProd(pn, plane);
		float ad = DotProd(lns,pn);
		float bd = DotProd(lne,pn);
		float t = (-pd - ad) / (bd - ad);
		vec3d lste = SubVec(lne,lns);
		vec3d lti = MulVec(lste, t);
		return AddVec(lns,lti);
	}
	
	public static triangle[] Clip(vec3d pp, vec3d pn, triangle tri) {
		
		triangle[] r;
		
		pn = NormalizeVec(pn);
		
		vec3d ip[] = new vec3d[3];
		vec3d op[] = new vec3d[3];
		int ipc = 0, opc = 0;
		
		float d0 = dist(tri.p[0],pn,pp);
		float d1 = dist(tri.p[1],pn,pp);
		float d2 = dist(tri.p[2],pn,pp);
		
		if (d0 >= 0) {
			ip[ipc++] = tri.p[0];
		} else {
			op[opc++] = tri.p[0];
		}
		
		
		if (d1 >= 0) {
			ip[ipc++] = tri.p[1];
		} else {
			op[opc++] = tri.p[1];
		}
		
		if (d2 >= 0) {
			ip[ipc++] = tri.p[2];
		} else {
			op[opc++] = tri.p[2];
		}
		
		if (ipc == 0) {
			return new triangle[0];
		}
		
		if (ipc == 3) {	
			r = new triangle[1];
			
			r[0] = tri;
			
			return r;
		}
		
		if (ipc == 1 && opc == 2) {
			r = new triangle[1];
			
			r[0] = new triangle();
			
			r[0].p[0] = ip[0];
			r[0].p[1] = IntersectPlane(pp,pn,ip[0],op[0]);
			r[0].p[2] = IntersectPlane(pp,pn,ip[0],op[1]);
			
			//r[0].color = tri.color = new Color(0,255,0);		
			return r;
		}
		
		if (ipc == 2 && opc == 1) {
			r = new triangle[2];
			
			r[0] = new triangle();
			r[1] = new triangle();
			
			//r[0].color = tri.color = new Color(255,0,0);	
			
			//r[1].color = tri.color = new Color(0,0,255);	
			
			r[0].p[0] = ip[0];
			r[0].p[1] = ip[1];
			r[0].p[2] = IntersectPlane(pp,pn,ip[0],op[0]);
			
			r[1].p[0] = ip[1];
			r[1].p[1] = r[0].p[2];
			r[1].p[2] = IntersectPlane(pp,pn,ip[1],op[0]);
			
			return r;
		}
		
		return new triangle[0];
	}
	
	
	public static float dist(vec3d p, vec3d pn, vec3d pp) {
		vec3d n = NormalizeVec(p);
		
		return (pn.x * p.x + pn.y * p.y + pn.z * p.z - DotProd(pn,pp));
	} 
}
