package parallel;

import java.util.concurrent.atomic.AtomicInteger;

//函数y=-x*(x-2) 在[0,2]上最大值的粒子群算法
public class AlgorithmPSO {
	int n = 2;//粒子数量
	double[] y;
	double[] x;
	double[] v;
	double c1 = 2;
	double c2 = 2;
	double pbest[];
	double gbest;
	double vmax = 0.1;//速度最大值

	//适应度函数--  并发执行加快速度
	public void fitnessFN() {
		for (int i = 0; i < n; i++) {
			y[i] = -1 * x[i] * (x[i] - 2);
		}
	}

	//初始化
	public void init() {
		this.x = new double[n];
		this.y = new double[n];
		this.v = new double[n];
		this.pbest = new double[n];

		x[0] = 0.0;
		x[1] = 2.0;
		v[0] = 0.01;
		v[1] = 0.02;
		fitnessFN();
		//初始化，寻找最优
		for (int i = 0; i < n; i++) {
			pbest[i] = y[i];//赋值
			if (y[i] > gbest) {
				gbest = y[i];//保存最大
			}
		}
		System.out.println("原始最优解" + gbest);
	}
	private AtomicInteger count = new AtomicInteger(0);//统计迭代次数
	//粒子群算法
	public void PSO(int max) {
		for (int i = 0; i < max; i++) {
			new Thread(() -> {
				double w = 0.4;//全局搜索
				for (int j = 0; j < n; j++) {
					//更新速度位置
					v[j] = w * v[j] + c1 * Math.random() * (pbest[j] - x[j]) + c2 * Math.random() * (gbest - x[j]);
					if (v[j] > vmax) {
						v[j] = vmax;//控制速度不超过最大
					}
					x[j] += v[j];//更新位置

					if (x[j] > 2) {
						x[j] = 2;
					}
					if (x[j] < 0) {
						x[j] = 0;
					}
				}
				fitnessFN();
				//更新个体极值与群体极值
				for (int j = 0; j < n; j++) {
					pbest[j] = Math.max(y[j], pbest[j]);//取得最大
					if (pbest[j] > gbest) {
						gbest = pbest[j];//保存群体最大值
					}
					System.out.println("粒子n" + j + " :x:=" + x[j] + " v=" + v[j]);
				}
				int K = this.count.incrementAndGet();
				System.out.println("第" + K +"次迭代，全局最优gbest= " + gbest);
				System.out.println();
			}).start();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AlgorithmPSO ap = new AlgorithmPSO();
		ap.init();
		ap.PSO(100);
	}
}

