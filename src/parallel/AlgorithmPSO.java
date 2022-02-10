package parallel;

import java.util.concurrent.atomic.AtomicInteger;

//����y=-x*(x-2) ��[0,2]�����ֵ������Ⱥ�㷨
public class AlgorithmPSO {
	int n = 2;//��������
	double[] y;
	double[] x;
	double[] v;
	double c1 = 2;
	double c2 = 2;
	double pbest[];
	double gbest;
	double vmax = 0.1;//�ٶ����ֵ

	//��Ӧ�Ⱥ���--  ����ִ�мӿ��ٶ�
	public void fitnessFN() {
		for (int i = 0; i < n; i++) {
			y[i] = -1 * x[i] * (x[i] - 2);
		}
	}

	//��ʼ��
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
		//��ʼ����Ѱ������
		for (int i = 0; i < n; i++) {
			pbest[i] = y[i];//��ֵ
			if (y[i] > gbest) {
				gbest = y[i];//�������
			}
		}
		System.out.println("ԭʼ���Ž�" + gbest);
	}
	private AtomicInteger count = new AtomicInteger(0);//ͳ�Ƶ�������
	//����Ⱥ�㷨
	public void PSO(int max) {
		for (int i = 0; i < max; i++) {
			new Thread(() -> {
				double w = 0.4;//ȫ������
				for (int j = 0; j < n; j++) {
					//�����ٶ�λ��
					v[j] = w * v[j] + c1 * Math.random() * (pbest[j] - x[j]) + c2 * Math.random() * (gbest - x[j]);
					if (v[j] > vmax) {
						v[j] = vmax;//�����ٶȲ��������
					}
					x[j] += v[j];//����λ��

					if (x[j] > 2) {
						x[j] = 2;
					}
					if (x[j] < 0) {
						x[j] = 0;
					}
				}
				fitnessFN();
				//���¸��弫ֵ��Ⱥ�弫ֵ
				for (int j = 0; j < n; j++) {
					pbest[j] = Math.max(y[j], pbest[j]);//ȡ�����
					if (pbest[j] > gbest) {
						gbest = pbest[j];//����Ⱥ�����ֵ
					}
					System.out.println("����n" + j + " :x:=" + x[j] + " v=" + v[j]);
				}
				int K = this.count.incrementAndGet();
				System.out.println("��" + K +"�ε�����ȫ������gbest= " + gbest);
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

