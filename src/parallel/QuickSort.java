package parallel;

import java.util.Arrays;
import java.util.concurrent.*;

///  19  28        37 46
//   1289         3467
//  12346789
public class QuickSort {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] arr = {1, 3, 2, 8, 4, 6, 7, 5, 10, 9, 12, 11};
		ExecutorService exe = Executors.newFixedThreadPool(5);
		CountDownLatch count = new CountDownLatch(4);
		try {
// 12个元素的数组，多线程划江而治，每个线程对所辖4个元素 排序
			int[] allarray = new int[arr.length];//拷贝数组
			for (int i = 0; i < 4; i++) {// 数据四等分
				int[] subarray = Arrays.copyOfRange(arr, i * 3, (i + 1) * 3);/// 采用多线程操作一个线程不安全的数组，要想计算结果一致，必须限制每个线程只操作自己范围内的元素—— 各个线程划江而治
				FutureTask<int[]> ft = new
						FutureTask<int[]>(new QSort(subarray, 0, subarray.length - 1, count));
				exe.submit(ft);//线程池执行
				int[] temp = ft.get();
				for (int j = 0; j < temp.length; j++) {
					allarray[i * 3 + j] = temp[j];//数组拷贝// 四组数据各自落座，合并为一个数组
				}
			}
			count.await();//等待线程池执行完成
 // // 划江而治的各个线程排序的结果
			for (int i = 0; i < allarray.length; i++) {
				System.out.println(allarray[i]);
			}
// 用一个工作线程，聚沙成塔
			CountDownLatch count1 = new CountDownLatch(1);
			FutureTask<int[]> ft2 = new
					FutureTask<int[]>(new QSort(allarray, 0, allarray.length - 1, count1));/// 采用多线程操作一个线程不安全的数组，要想计算结果一致，必须限制每个线程只操作自己范围内的元素—— 各个线程划江而治
			exe.submit(ft2);
			count1.await();
			int[] result = ft2.get();
			for (int i = 0; i < result.length; i++) {
				System.out.println(result[i]);//多线程合并
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			exe.shutdown();//关闭线程池
		}
	}

	/// 采用多线程操作一个线程不安全的数组，要想计算结果一致，必须限制每个线程只操作自己范围内的元素—— 各个线程划江而治
	public static class QSort implements Callable<int[]> {
		int[] array;
		int left;
		int right;
		CountDownLatch count;//下载
		public QSort(int[] array, int left, int right, CountDownLatch count) {
			this.array = array;
			this.left = left;
			this.right = right;
			this.count = count;
		}
		@Override
		public int[] call() throws Exception {
			// TODO Auto-generated method stub
			this.dosort(array, 0, array.length - 1);
			count.countDown();
			return array;
		}
		private void dosort(int[] array, int left, int right) {
			if (left < right) {
				int mid = getmid(array, left, right);
				dosort(array, left, mid - 1);
				dosort(array, mid + 1, right);
			}
		}
		private int getmid(int[] array, int left, int right) {
			int base = array[left];
			while (left < right) { // 找到最左边比我小的
				while (left < right && array[right] >= base) {
					right--;
				}
				array[left] = array[right];
				while (left < right && array[left] <= base) {
					left++;
				}
				array[right] = array[left];// 左右互换
			}
			array[left] = base;
			return left;
		}
	}
}
