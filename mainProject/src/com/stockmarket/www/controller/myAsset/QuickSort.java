package com.stockmarket.www.controller.myAsset;

public class QuickSort {
	static int serial = 0;

	public static void main(String[] args) {
		int[] array = new int[10];
		array[0] = 1;
		array[1] = 11;
		array[2] = 88;
		array[3] = 55;
		array[4] = 99;
		array[5] = 77;
		array[6] = 66;
		array[7] = 44;
		array[8] = 22;
		array[9] = 33;

		int[] arr = new int[20];
		for (int i = 0; i < 20; i++) {
			arr[i] = (int) (Math.random() * 100) + 1;
		}
		System.out.println("before)");
		displayArray(arr);
		System.out.println();
		quickSort(arr, 0, arr.length - 1);
		displayArray(arr);
	}

	static void quickSort(int[] arr, int low, int high) {
		if (low < high) {
			int pivotValue = arr[high];
			int leftPoint = low - 1;
			int rightPoint = high;

			while (true) {
				while (arr[++leftPoint] < pivotValue) { // 왼쪽포인터가 피봇보다 클때까지

				}

				while (rightPoint > low && arr[--rightPoint] > pivotValue) { // 오른쪽포인터가 피봇보다 클때까지
				}

				if (leftPoint >= rightPoint) { // 정렬이 된 경우
					break;
				} else { // 찾은 경우
					swapValue(arr, leftPoint, rightPoint);
				}
			}

			// 피봇 값을 가운데로 옮김
			swapValue(arr, leftPoint, high);
			quickSort(arr, low, leftPoint - 1);
			quickSort(arr, leftPoint + 1, high);
		}
	}

	static void swapValue(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	static void displayArray(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
	}
}
