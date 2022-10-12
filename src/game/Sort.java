package game;

import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Sort {
	/*public static double[] MergeSort(triangle[] a) {
		if (a.length <= 1) return a;
		int m = a.length/2;
		double[] l = new double[m];
		double[] r;
		if (a.length % 2 == 0) {
			r = new double[m];
		} else {
			r = new double[m+1];
		}
		
		for (int i=0;i<m;i++) {
			l[i]=a[i];
		}
		for (int i=0;i<r.length;i++) {
			r[i]=a[m+i];
		}
		double[] e=new double[a.length];
		
		l=MergeSort(l);
		r=MergeSort(r);
		
		e=merge(l,r);
		
		return e;
	}
	
	private static double[] merge(double[] l,double[] r) {
		double[] e = new double[l.length+r.length];
		
		int lp,rp,ep;
		lp=rp=ep=0;
		
		while (lp < l.length || rp < r.length) {
			if (lp<l.length&&rp<r.length) {
				if (l[lp]<r[rp]) {
					e[ep++]=l[lp++];
				} else {
					e[ep++]=r[rp++];
				}
			} else if (lp<l.length) {
				e[ep++]=l[lp];
			} else if (rp<r.length) {
				e[ep++]=r[rp];
			}
		}
		
		return e;
	}*/
	
	Sort() {}
	
	public LinkedList<triangle> mergeSort(LinkedList<triangle> e) {
		if (e.size() == 1 || e.size() == 0) return e;
		
		int mid = (int) Math.floor(e.size() / 2);
		
		LinkedList<triangle> left = (LinkedList<triangle>) new LinkedList<>(e.subList(0, mid));
		LinkedList<triangle> right = (LinkedList<triangle>) new LinkedList<>(e.subList(mid, e.size()));
		
		System.out.println(left.size());
		
		return merge(mergeSort(left), mergeSort(right));
	}
	
	public LinkedList<triangle> merge(LinkedList<triangle> l, LinkedList<triangle> r) {
		LinkedList<triangle> res = new LinkedList<triangle>();
		
		int l_i = 0;
		int r_i = 0;
		
		while (l_i < l.size() && r_i < r.size()) {
			triangle lt = l.get(l_i);
			triangle rt = r.get(r_i);
			
			if ((lt.p[0].z + lt.p[1].z + lt.p[2].z) / 3.0f > (rt.p[0].z + rt.p[1].z + rt.p[2].z) / 3.0f) {
				res.addLast(l.get(l_i));
				l_i++;
			} else {
				res.addLast(r.get(r_i));
				r_i++;
			}
		}
		
		return (LinkedList<triangle>) new LinkedList<triangle>(Stream.concat(l.subList(l_i, l.size()).stream(), r.subList(r_i, r.size()).stream()).collect(Collectors.toList()));
	}
}
