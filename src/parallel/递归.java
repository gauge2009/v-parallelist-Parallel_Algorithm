package parallel;
import java.util.*;
public class µÝ¹é {
	public static int fab(int i) {
		if (i == 1 || i == 2) {
			return 1;
		} else {
			return fab(i - 1) + fab(i - 2);
		}
	}
	public static class FibGo extends Thread {
		public int x;
		public int result;
		public FibGo(int x) {
			this.x = x;
		}
		public void run() {
			if (x <= 2) {
				this.result = 1;
			} else {
				try {
					FibGo f1 = new FibGo(x - 1);
					FibGo f2 = new FibGo(x - 2);
					f1.start();
					f2.start();
					f1.join();
					f2.join();
					this.result = f1.result + f2.result;
				} catch (Exception e) {
				}
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double begin =  System.currentTimeMillis();
		 System.out.println(fab(43));
		double end =  System.currentTimeMillis();
		 System.out.println("ºÄÊ±"+ (end-begin) + "ms" );
		try {
			double begin1 =  System.currentTimeMillis();
			FibGo f1 = new FibGo(20);
			f1.start();
			f1.join();
			System.out.println(f1.result);
			double end1 =  System.currentTimeMillis();
			System.out.println("ºÄÊ±"+ (end1-begin1) + "ms" );
		} catch (Exception e) {
		}
	}
}
