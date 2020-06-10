package study.algorithm.sort;

import java.util.Arrays;

public class InsertSort {
    public static void main(String[] args) {
        int[] arr={101, 34, 119, 1};
        insortSort(arr);
        System.out.println(Arrays.toString(arr));
    }
    private static void insortSort(int[] arr) {
        for(int i=1;i<arr.length;i++){
           int temp=arr[i];
           int j=i;
            while (j-1>=0 && arr[j]<arr[j-1]) {
                arr[j]=arr[j--];
            }
            if(i != j){
               arr[j]=temp;
            }
        }
    }
}
