package parallel;

//->->->->���߳�
//���߳�
//->
//->
import java.util.concurrent.*;

public class Matrix   extends Thread {
	private int[][] A;
	private int[][] B;
	private int index;//����
	private int gap;//����
	private int[][] result;//���
	private CountDownLatch cd;//����������

	public Matrix(int[][] A, int[][] B, int index,
				  int gap, int[][] result, CountDownLatch cd) {
		this.A = A;
		this.B = B;
		this.index = index;
		this.gap = gap;
		this.result = result;
		this.cd = cd;
	}
	//1 3 5
	//2 4 6

	//123  456
	//����
	public void run() {
		for (int i = index * gap; i < (index + 1) * gap; i++) {// ÿ���̸߳���ר�ŵ���������
			for (int j = 0; j < B[0].length; j++) {
				for (int k = 0; k < B.length; k++) {
					result[i][j] += A[i][k] * B[k][j];//����˷�
				}
			}
		}
		this.cd.countDown();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int row_a = 300;
		int col_a = 300;
		int col_b = 300;
		int[][] A = new int[row_a][col_a];
		int[][] B = new int[col_a][col_b];
		int[][] serial_result = new int[A.length][B[0].length];
		for (int i = 0; i < row_a; i++) {
			for (int j = 0; j < col_a; j++) {
				A[i][j] = (int) (Math.random() * 100);//��ʼ��
			}
		}

		for (int i = 0; i < col_a; i++) {
			for (int j = 0; j < col_b; j++) {
				B[i][j] = (int) (Math.random() * 100);//��ʼ��
			}
		}


		///���м���ʵ��
		///
		long starttime = System.currentTimeMillis();
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < B[0].length; j++) {
				for (int k = 0; k < A[0].length; k++) {
					serial_result[i][j] += A[i][k] * B[k][j];//����˷�
				}
			}
		}
		long endtime = System.currentTimeMillis();
		System.out.println("���м���ʵ�� " + (endtime - starttime)+" ms");

		///���м���ʵ��
		///
		int threadnum = 10;//�߳�����
		int gap = A.length / threadnum;// ÿ���̸߳���ר�ŵ���������
		CountDownLatch cd = new CountDownLatch(10);
		int[][] p_result = new int[A.length][B[0].length];
		long starttime1 = System.currentTimeMillis();
		for (int i = 0; i < threadnum; i++) {
			Matrix mx = new Matrix(A, B, i, gap, p_result, cd);
			mx.start();
		}
		long endtime1 = System.currentTimeMillis();
		System.out.println("��������ʵ�� " + (endtime1 - starttime1)+" ms");

	}

}
