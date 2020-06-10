package study.algorithm.sort;

import java.util.Arrays;

public class SelectSort {
    public static void main(String[] args) {
        int[] arr={101, 34, 119, 1};
        selectSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    private static void selectSort(int[] arr) {
         for(int i=0;i<arr.length-1;i++){
             int minIndex=i;
             for(int j=i;j<arr.length;j++){
                 if(arr[minIndex]>arr[j]){
                     minIndex=j;
                 }
             }
             if (minIndex != i) {
                 arr[minIndex]=arr[i]+arr[minIndex];
                 arr[i]=arr[minIndex]-arr[i];
                 arr[minIndex]=arr[minIndex]-arr[i];
             }
         }
    }

}
