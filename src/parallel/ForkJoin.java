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
		private int MAX = 100;//小于100个采用系统顺序排序算法

		public SortTask(int[] arr) { //构造初始化
			this.array = arr;
			this.left = 0;
			this.right = arr.length - 1;
		}
		public SortTask(int[] arr, int left, int right) { //构造初始化
			this.array = arr;
			this.left = left;
			this.right = right;
		}
		@Override
		protected void compute() {
			// TODO Auto-generated method stub
			if (right - left < MAX) {// 数据规模较小时，采用顺序排序算法
				sequentiallySort(array, left, right);
			} else {// 数据规模较大 时，采用分治+归并的并行排序算法
				int p = partition(array, left, right);
				new SortTask(array, left, p - 1).fork();  //并发计算
				new SortTask(array, p + 1, right).fork();
				//fork,等待
			}

		}
		//分区
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
		//交换数据
		private void swap(int[] array, int i, int j) {
			if (i != j) {
				int temp = array[i];
				array[i] = array[j];
				array[j] = temp;
			}
		}
		//排序
		private void sequentiallySort(int[] array, int left, int right) {
			Arrays.sort(array, left, right + 1);
		}
	}





	private static final int length = 10000000;
	int[] array = new int[length];
	Random rd = new Random();
	public void init() {
		for (int i = 0; i < array.length; i++) {
			array[i] = rd.nextInt(length);//随机初始化
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
		// 系统排序 [Arrays.parallelSort]
		ForkJoin fuck = new ForkJoin();
		fuck.init();
		long starttime = System.currentTimeMillis();
		Arrays.parallelSort(fuck.array, 0, fuck.array.length);
		long endtime = System.currentTimeMillis();
		System.out.println("系统排序 [Arrays.parallelSort]-" + (endtime - starttime)+"ms");
		if (!fuck.checksorted(fuck.array)) {
			System.out.println("error sort1");
		}


		/// 自己实现的多线程排序 [SortTask]
		fuck.init();
		long starttime1 = System.currentTimeMillis();
		ForkJoinTask sort = new SortTask(fuck.array);//线程任务
		ForkJoinPool pool = new ForkJoinPool();//线程池
		pool.submit(sort);//递交任务
		pool.shutdown();
		pool.awaitTermination(30, TimeUnit.SECONDS);//等待

		long endtime1 = System.currentTimeMillis();
		System.out.println("自己实现的多线程排序 [SortTask]-" + (endtime1 - starttime1)+"ms");
		if (!fuck.checksorted(fuck.array)) {
			System.out.println("error sort2");
		}
	}
}
