package com.jason.sort;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Integer[] a = new Integer[]{3,7,2,9,1,4,6,8,10,5};
        int[] b = new int[]{1,2,3,4,5,6,7,8,9,10};
        int[] c = new int[]{10,9,8,7,6,5,4,3,2,1};
        int[] d = new int[]{1,10,2,9,3,8,4,7,5,6};

//        sort(a, 0, d.length-1);
//        bubble(d);
//        choose(d);
//        inert(d);

        qq(d,0, a.length-1);
//        tttI(a, 0, a.length-1);

        System.out.println("排序后的结果：");
        for(int x : d){
           Log.e("sort",x+" ");
        }

//        int sum = sumImp(b,10);
//        Log.e("sum",sum+" ");

    }
    //求数组的和（非尾递归）
    private int sumImp(int a[], int i) {
        if (i == 1) {
            return a[0];
        }

        int temp = sumImp(a, i - 1);
        int ret = temp + a[i - 1];
        return ret;
    }



    //求数组的和（尾递归）,入口
    private long sum(int array[], int length) {
        return sumImp(array, 0, length);
    }
    //求数组的和（尾递归）,实际函数
    private long sumImp(int array[], long result, int index) {
        if (index > 0) {
            result += array[index - 1];
            return sumImp(array, result, index - 1);
        }
        return result;
    }




    public void sort(int[] a, int start, int end){
        if(start >= end){
            //如果只有一个元素，就不用再排下去了
            return;
        }
        else{
            //如果不止一个元素，继续划分两边递归排序下去
            int partition = sortImp(a, start, end);
            sort(a, start, partition-1);
            sort(a, partition+1, end);
        }

    }


    public int sortImp(int[] target, int start, int end) {
        //每次都以最右边的元素作为基准值
        int base = target[end];
        //start一旦等于end，就说明左右两个指针合并到了同一位置，可以结束此轮循环。
        while (start < end) {
            boolean returnFlag = false;
            if (target[start] <= base) {
                while (target[start] <= base) {
                    //从左边开始遍历，如果比基准值小，就继续向右走
                    start++;
                    if (start >= end) {
                        returnFlag = true;
                        break;
                    }
                }
            }

            if (target[start] > base) {
                //上面的while循环结束时，就说明当前的a[start]的值比基准值大，应与基准值进行交换
                //交换
                int temp = target[start];
                target[start] = target[end];
                target[end] = temp;
                //交换后，此时的那个被调换的值也同时调到了正确的位置(基准值右边)，因此右边也要同时向前移动一位
                end--;
                if (start >= end) {
                    returnFlag = true;
                    break;
                }
                while (target[end] > base) {
                    //从左边开始遍历，如果比基准值小，就继续向右走
                    end--;
                    if (start >= end) {
                        returnFlag = true;
                        break;
                    }
                }

                int temp1 = target[start];
                target[start] = target[end];
                target[end] = temp1;
                //交换后，此时的那个被调换的值也同时调到了正确的位置(基准值右边)，因此右边也要同时向前移动一位
                start++;
            }


            if (returnFlag) break;

        }
        //这里返回start或者end皆可，此时的start和end都为基准值所在的位置
        return end;
    }

    public void bubble(int[] a) {
//        for (int i = a.length-1; i >0; i--) {  //  length -1 趟
//            for (int j = 0; j < i; j++) {   //  最大 length -1 次
//                if (a[j] > a[j + 1]) {
//                    int t = a[j];
//                    a[j] = a[j + 1];
//                    a[j + 1] = t;
//                }
//            }
//        }

        for (int i = 0; i <= a.length - 2; i++) {
            for (int j = 0; j <= a.length - 2 - i; j++) {
                if (a[j] > a[j + 1]) {
                    swap(a, j, j + 1);
                }
            }
        }




    }

    private void swap(int[] aaa,int index1,int index2) {
        int temp = aaa[index1];
        aaa[index1]=aaa[index2];
        aaa[index2] = temp;
    }

    public void choose(int[] a) { //选择后面 序列中最小的，做交换

//        for (int i = 0; i < a.length - 1; i++) {
//            int minIndex = i;
//            for (int j = i; j < a.length; j++) {
//                if (a[j] < a[minIndex]) {
//                    minIndex = j;
//                }
//            }
//            if (i != minIndex) {
//                int t = a[i];
//                a[i] = a[minIndex];
//                a[minIndex] = t;
//            }
//        }

        for (int i = 0; i <= a.length - 2; i++) {
            for (int j = i; j <= a.length - 1; j++) {
                if (a[j] < a[i]) {
                    swap(a, i, j);
                }
            }
        }
    }

    public void inert(int[] aaa) { //从 排序区外 选择一个，插入 排序区 合适的地方
//        for (int i = 1; i < aaa.length; i++) {
//            for (int j = i - 1; j > 0; j--) {
//                if (aaa[j] < aaa[j -1]) {
//                    int temp = aaa[j -1];
//                    aaa[j-1]=aaa[j];
//                    aaa[j] = temp;
//                } else {
//                    break;
//                }
//            }
//        }

        for (int i;;){

            for (int j = 0; ; ) {

            }
        }


    }


    public void tttI(Integer[]  aaa,int start,int end) {
        //设定一个分界值，通过该分界值将数组分成大小不同的左右两部分
        if (start >= end) {
            return;
        }
        int  de = ttt(aaa, start, end);
        tttI(aaa, start, de-1);
        tttI(aaa, de+1, end);
    }
    public int ttt(Integer[]  aaa,int start,int end) {
        int standard = aaa[end];

        while (start < end) {
            if (aaa[start] <= standard) {
                while (aaa[start] <= standard) {
                    start++;
                    if (start >= end) {
                        return start;
                    }
                }
            }
            if (start >= end) {
                return start;
            }
            int temp = aaa[start];
            aaa[start] = aaa[end];
            aaa[end] = temp;
            end--;
            if (start >= end) {
                return start;
            }
            if (aaa[end] > standard) {
                while (aaa[end] > standard) {
                    end--;
                    if (start >= end) {
                        return start;
                    }
                }
            }
            if (start >= end) {
                return start;
            }
            int temp1 = aaa[start];
            aaa[start] = aaa[end];
            aaa[end] = temp1;
            start++;
        }
        return start;
    }


    public void qq(int[] aa,int start,int end) {
        if(start>=end)
            return;

        int pp = qqI(aa, start, end);
        if (start < pp - 1) {
            qq(aa, start, pp - 1);
        }
        if (end > pp + 1) {
            qq(aa, pp + 1, end);
        }
    }

    public int qqI(int[] aa,int start,int end) {
        if (start >= end) {
            return start;
        }

        int pivot = aa[end];
        while (start < end) {
            while (aa[start] < pivot && start < end){
                start++;
            }
            if (start >= end) {
                return start;
            }
            swap(aa,start,end);
            end--;
            if (start >= end) {
                return start;
            }
            while (aa[end] > pivot && start < end){
                end --;
            }
            if (start >= end) {
                return start;
            }
            swap(aa,start,end);
            start++;
        }
        return start;

    }

    public void onClick(View view) {
    }
}


































