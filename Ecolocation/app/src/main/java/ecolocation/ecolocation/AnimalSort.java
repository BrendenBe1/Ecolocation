package ecolocation.ecolocation;

import java.util.ArrayList;

/**
 * Created by Chandler on 12/24/2017.
 */

public class AnimalSort {
    public AnimalSort(){}

    /// /------- Sorting Arraylists
    //int is a flag for ascending (0) or descending sort(1)
    public ArrayList<Animal> sort(ArrayList<Animal> list, int order){
        //TODO: sort based on ascending or descending


        return list;
    }

    private static void printStrings(ArrayList<Animal> arr, int i, int j){
        System.out.print("[");
        for(int k=i; k<=j-1; k++){
            System.out.print(arr.get(k).toString() + ", ");
        }
        System.out.print(arr.get(j) + "]");
    }

    public static void mergesort(ArrayList<Animal>  arr, int i, int j){
        int mid = 0;

        if(i < j) {
            mid = (i + j) / 2;

            mergesort(arr, i, mid);
            mergesort(arr, mid+1, j);
            stringsMerge(arr, i, mid, j);
        }
    }
    private static void stringsMerge(ArrayList<Animal> arr, int i, int mid, int j){

        System.out.print("Left: ");
        printStrings(arr, i, mid);
        System.out.print(" Right: ");
        printStrings(arr, mid + 1, j);
        System.out.println();

        //initialize variables
        ArrayList<Animal> temp = new ArrayList<Animal>();
        //make temp to be the same size
        for(int a=0; a<arr.size(); a++){
            temp.add(new Animal());
        }

        int l = i;
        int r = j;
        int m = mid + 1;
        int k = l;

        while (l <= mid && m <= r){
            String strL = arr.get(l).getBinomial();
            String strM = arr.get(m).getBinomial();
            if(strL.compareTo(strM) <= 0){
                temp.set(k, arr.get(l));
                l++;
            }
            else{
                temp.set(k, arr.get(m));
                m++;
            }
            k++;
        }

        //if right array is empty
        while(l <= mid){
            temp.set(k, arr.get(l));
            k++;
            l++;
        }
        //if eft array is empty
        while (m <= r){
            temp.set(k, arr.get(m));
            k++;
            m++;
        }

        for(int a = i; a <= j; a++){
            arr.set(a, temp.get(a));
        }

        System.out.print("After Merge: ");
        printStrings(arr, i, j);
        System.out.println();
    }
}
