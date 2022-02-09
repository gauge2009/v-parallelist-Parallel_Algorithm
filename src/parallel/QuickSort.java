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
// 12��Ԫ�ص����飬���̻߳������Σ�ÿ���̶߳���Ͻ4��Ԫ�� ����
			int[] allarray = new int[arr.length];//��������
			for (int i = 0; i < 4; i++) {// �����ĵȷ�
				int[] subarray = Arrays.copyOfRange(arr, i * 3, (i + 1) * 3);/// ���ö��̲߳���һ���̲߳���ȫ�����飬Ҫ�������һ�£���������ÿ���߳�ֻ�����Լ���Χ�ڵ�Ԫ�ء��� �����̻߳�������
				FutureTask<int[]> ft = new
						FutureTask<int[]>(new QSort(subarray, 0, subarray.length - 1, count));
				exe.submit(ft);//�̳߳�ִ��
				int[] temp = ft.get();
				for (int j = 0; j < temp.length; j++) {
					allarray[i * 3 + j] = temp[j];//���鿽��// �������ݸ����������ϲ�Ϊһ������
				}
			}
			count.await();//�ȴ��̳߳�ִ�����
 // // �������εĸ����߳�����Ľ��
			for (int i = 0; i < allarray.length; i++) {
				System.out.println(allarray[i]);
			}
// ��һ�������̣߳���ɳ����
			CountDownLatch count1 = new CountDownLatch(1);
			FutureTask<int[]> ft2 = new
					FutureTask<int[]>(new QSort(allarray, 0, allarray.length - 1, count1));/// ���ö��̲߳���һ���̲߳���ȫ�����飬Ҫ�������һ�£���������ÿ���߳�ֻ�����Լ���Χ�ڵ�Ԫ�ء��� �����̻߳�������
			exe.submit(ft2);
			count1.await();
			int[] result = ft2.get();
			for (int i = 0; i < result.length; i++) {
				System.out.println(result[i]);//���̺߳ϲ�
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			exe.shutdown();//�ر��̳߳�
		}
	}

	/// ���ö��̲߳���һ���̲߳���ȫ�����飬Ҫ�������һ�£���������ÿ���߳�ֻ�����Լ���Χ�ڵ�Ԫ�ء��� �����̻߳�������
	public static class QSort implements Callable<int[]> {
		int[] array;
		int left;
		int right;
		CountDownLatch count;//����
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
			while (left < right) { // �ҵ�����߱���С��
				while (left < right && array[right] >= base) {
					right--;
				}
				array[left] = array[right];
				while (left < right && array[left] <= base) {
					left++;
				}
				array[right] = array[left];// ���һ���
			}
			array[left] = base;
			return left;
		}
	}
}
