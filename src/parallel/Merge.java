package parallel;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

//1928    4756
//19  28   47  56
//1 9   2 8   4 7  5 6
//1289     4567
//12456789

public class Merge {
	public static class MergeSort extends RecursiveAction {
		private int[] arr;
		public MergeSort(int[] arr) {
			this.arr = arr;
		}
		@Override
		protected void compute() {
			// TODO Auto-generated method stub
			if (this.arr.length > 1) {
				int[] leftarr = Arrays.copyOfRange(arr, 0, arr.length / 2);
				int[] rightarr = Arrays.copyOfRange(arr, arr.length / 2, arr.length);

				invokeAll(new MergeSort(leftarr), new MergeSort(rightarr));//��ʼ��
				merge(leftarr, rightarr, arr);//�ϲ�����
			}
		}
		/**
		 * �ϲ�����
		 *
		 * @param leftArray
		 * @param rightArray
		 * @param intArr
		 */
		private void merge(int[] leftArray, int[] rightArray, int[] intArr) {
			// i��leftArray����������j��rightArray����������k��intArr��������
			int i = 0, j = 0, k = 0;
			while (i < leftArray.length && j < rightArray.length) {
				// �����������ж���ֵ��ʱ�򣬱Ƚϵ�ǰԪ�ؽ���ѡ��
				if (leftArray[i] < rightArray[j]) {
					intArr[k] = leftArray[i];
					i++;
				} else {
					intArr[k] = rightArray[j];
					j++;
				}
				k++;
			}
			// ����ʣ��Ԫ��û�б����������ֱ��׷�ӵ�intArr����
			if (i == leftArray.length) {
				for (; j < rightArray.length; j++, k++) {
					intArr[k] = rightArray[j];
				}
			} else {
				for (; i < leftArray.length; i++, k++) {
					intArr[k] = leftArray[i];
				}
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] arr = {1, 3, 2, 8, 4, 6, 7, 5, 10, 9, 12, 11};
		ForkJoinPool pool = new ForkJoinPool();
		pool.invoke(new MergeSort(arr));//fork  join�̳߳�
		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i]);
		}
	}
}
