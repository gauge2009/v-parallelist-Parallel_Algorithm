package parallel;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;


public class ForkJoin {
	//fork join
	public static class SortTask extends RecursiveAction {
		final int[] array;
		final int left;
		final int right;
		private int MAX = 100;//С��100������ϵͳ˳�������㷨

		public SortTask(int[] arr) { //�����ʼ��
			this.array = arr;
			this.left = 0;
			this.right = arr.length - 1;
		}
		public SortTask(int[] arr, int left, int right) { //�����ʼ��
			this.array = arr;
			this.left = left;
			this.right = right;
		}
		@Override
		protected void compute() {
			// TODO Auto-generated method stub
			if (right - left < MAX) {// ���ݹ�ģ��Сʱ������˳�������㷨
				sequentiallySort(array, left, right);
			} else {// ���ݹ�ģ�ϴ� ʱ�����÷���+�鲢�Ĳ��������㷨
				int p = partition(array, left, right);
				new SortTask(array, left, p - 1).fork();  //��������
				new SortTask(array, p + 1, right).fork();
				//fork,�ȴ�
			}

		}
		//����
		private int partition(int[] array, int left, int right) {
			int x = array[right];
			int i = left - 1;
			for (int j = left; j < right; j++) {
				if (array[j] <= x) {
					i++;
					swap(array, i, j);
				}
			}
			swap(array, i + 1, right);
			return i + 1;
		}
		//��������
		private void swap(int[] array, int i, int j) {
			if (i != j) {
				int temp = array[i];
				array[i] = array[j];
				array[j] = temp;
			}
		}
		//����
		private void sequentiallySort(int[] array, int left, int right) {
			Arrays.sort(array, left, right + 1);
		}
	}





	private static final int length = 10000000;
	int[] array = new int[length];
	Random rd = new Random();
	public void init() {
		for (int i = 0; i < array.length; i++) {
			array[i] = rd.nextInt(length);//�����ʼ��
		}
	}
	public boolean checksorted(int[] a) {
		for (int i = 0; i < a.length - 1; i++) {
			if (a[i] > a[i + 1]) {
				return false;
			}
		}
		return true;
	}
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		// ϵͳ���� [Arrays.parallelSort]
		ForkJoin fuck = new ForkJoin();
		fuck.init();
		long starttime = System.currentTimeMillis();
		Arrays.parallelSort(fuck.array, 0, fuck.array.length);
		long endtime = System.currentTimeMillis();
		System.out.println("ϵͳ���� [Arrays.parallelSort]-" + (endtime - starttime)+"ms");
		if (!fuck.checksorted(fuck.array)) {
			System.out.println("error sort1");
		}


		/// �Լ�ʵ�ֵĶ��߳����� [SortTask]
		fuck.init();
		long starttime1 = System.currentTimeMillis();
		ForkJoinTask sort = new SortTask(fuck.array);//�߳�����
		ForkJoinPool pool = new ForkJoinPool();//�̳߳�
		pool.submit(sort);//�ݽ�����
		pool.shutdown();
		pool.awaitTermination(30, TimeUnit.SECONDS);//�ȴ�

		long endtime1 = System.currentTimeMillis();
		System.out.println("�Լ�ʵ�ֵĶ��߳����� [SortTask]-" + (endtime1 - starttime1)+"ms");
		if (!fuck.checksorted(fuck.array)) {
			System.out.println("error sort2");
		}
	}
}
