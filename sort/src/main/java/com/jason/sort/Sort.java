package com.jason.sort;

public class Sort {




    public void aaa() {
        int[]  candidate  = {5, 2, 4, 2, 4};
        //冒泡
        for (int i = 0; i <  candidate .length; i++) {
            for (int j = 0; j <  candidate .length-i-1; j++) {
                if ( candidate [j + 1] <  candidate [j]) {
                    int temp =  candidate [j + 1];
                    candidate [j + 1] =  candidate [j];
                    candidate [j] = temp;
                }
            }
        }
        //优化冒泡
        for (int i = 0; i <  candidate .length; i++) {
            boolean flag = false;
            for (int j = 0; j <  candidate .length-i-1; j++) {
                if ( candidate [j + 1] <  candidate [j]) {
                    int temp =  candidate [j + 1];
                    candidate [j + 1] =  candidate [j];
                    candidate [j] = temp;
                    flag = true;
                }
            }
            if(!flag) break;
        }

        //选择排序
        for (int i = 0; i <  candidate .length; i++) {
            int minIndex = i;
            for (int j = i; j <  candidate .length; j++) {
                if ( candidate [j] <  candidate [minIndex]) //找到最小的数
                    minIndex = j; //将最小数的索引保存
            }
            int temp =  candidate [minIndex];
            candidate [minIndex] =  candidate [i];
            candidate [i] = temp;
        }
        //优化选择排序
        for (int i = 0; i < candidate.length; i++) {
            int minIndex = i;
            for (int j = i; j < candidate.length; j++) {
                if(candidate[j]<candidate[minIndex])
                    minIndex = j;
            }
            if(minIndex==i) continue;
            int temp = candidate[i];
            candidate[i] = candidate[minIndex];
            candidate[minIndex] = temp;
        }


        //插入排序
        int current;
        for (int i = 0; i < candidate.length - 1; i++) {
            current = candidate[i + 1];
            int preIndex = i;
            while (preIndex >= 0 && current < candidate[preIndex]) {
                candidate[preIndex + 1] = candidate[preIndex];
                preIndex--;
            }
            candidate[preIndex + 1] = current;
        }
        //插入排序
        for (int i = 0; i < candidate.length-1; i++) {
            int target = candidate[i + 1];
            for (int j = i; j >=0; j--) {
                if (target < candidate[j]) {
                    candidate[j + 1] = candidate[j];
                }else {
                    candidate[j+1] = target;
                    break;
                }
            }
        }
        //插入排序
        for (int i = 0; i < candidate.length-1; i++) {
            int target = candidate[i + 1];
            int j;
            for (j = i; j >=0 && target < candidate[j]; j--) {
                candidate[j + 1] = candidate[j];
            }
            candidate[j+1] = target;
        }

        quickSort(candidate,0,candidate.length-1);
    }


//  private int aaaaa(int[] candidate, int low, int high) {
//    int pivotIndex = high;
//
//    for (int i=low;i<high;i++){
//      if (candidate[i] > candidate[pivotIndex]) {
//        swap(candidate, i, pivotIndex);
//        pivotIndex = i;
//        for(int j=candidate.)
//      }
//    }
//  }


    /* The main function that implements QuickSort()
      arr[] --> Array to be sorted,
      low  --> Starting index,
      high  --> Ending index */
    private void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            /* pi is partitioning index, arr[pi] is
              now at right place */
            int pi = partition(arr, low, high);

            // Recursively sort elements before
            // partition and after partition
            quickSort(arr, low, pi-1);
            quickSort(arr, pi+1, high);
        }
    }


    private int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = (low-1); // index of smaller element
        for (int j=low; j<high; j++) {
            // If current element is smaller than the pivot
            if (arr[j] < pivot) {
                i++;

                // swap arr[i] and arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        // swap arr[i+1] and arr[high] (or pivot)
        int temp = arr[i+1];
        arr[i+1] = arr[high];
        arr[high] = temp;

        return i+1;
    }



}
