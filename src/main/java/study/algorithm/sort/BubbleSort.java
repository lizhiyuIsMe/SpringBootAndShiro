package study.algorithm.sort;

import java.util.Arrays;

//冒泡
public  class BubbleSort {
    public static void main(String[] args) {
        int[] arr ={3, 9, -1, 10, -2};
        bubbleSort(arr);
        System.out.println(Arrays.toString(arr));
    }
    private static void bubbleSort(int[] arr) {
        boolean flag=true;
        //比较多少次
        for (int i = 0; i <= arr.length - 1; i++) {
            //每次比较几个
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    arr[j] = arr[j + 1] + arr[j];
                    arr[j + 1] = arr[j] - arr[j + 1];
                    arr[j] = arr[j] - arr[j + 1];
                    flag=false;
                }
            }
            if (flag) {
                break;
            }else{
                flag=true;
            }
        }
    }
}
