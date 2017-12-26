package ecolocation.ecolocation;

import java.util.ArrayList;

/**
 * Created by Chandler on 12/24/2017.
 */
enum SORT_TYPE {
    BINOMIAL,
    ENDANGERED,
    MASS,
    POPULATION
}

public class AnimalSort {
    public AnimalSort(){}

    /// /------- Sorting Arraylists
    //int is a flag for ascending (0) or descending sort(1)
    public ArrayList<Animal> sort(ArrayList<Animal> list, SORT_TYPE sortType, int order){
        mergesort(list, 0, list.size()-1, sortType, order);

        return list;
    }


    public static void mergesort(ArrayList<Animal>  list, int i, int j, SORT_TYPE sortType,
                                 int order){
        int mid = 0;

        if(i < j) {
            mid = (i + j) / 2;

            mergesort(list, i, mid, sortType, order);
            mergesort(list, mid+1, j, sortType, order);
            if(sortType == SORT_TYPE.BINOMIAL || sortType == SORT_TYPE.ENDANGERED){
                if(order == 0){
                    stringsMergeAscend(list, i, mid, j, sortType);
                }
                else{
                    stringsMergeDescend(list, i, mid, j, sortType);
                }
            }
            else if(sortType == SORT_TYPE.MASS || sortType == SORT_TYPE.POPULATION){
                integersMerge(list, i, mid, j, sortType);
            }
        }
    }

    private static void stringsMergeAscend(ArrayList<Animal> list, int leftStart, int middleStart,
                                           int righStart, SORT_TYPE sortType){
        //initialize variables
        ArrayList<Animal> temp = new ArrayList<Animal>();
        //make temp to be the same size
        for(int i=0; i<list.size(); i++){
            temp.add(new Animal());
        }

        //initialize variables
        int leftIndex = leftStart;
        int rightIndex = middleStart + 1;
        int rightEnd = righStart;
        int mergedIndex = leftIndex;

        //start merging left & right lists
        while (leftIndex <= middleStart && rightIndex <= rightEnd){
            String strLeft;
            String strRight;
            //TODO get BINOMIAL or population based on SORT_TYPE
            switch (sortType){
                case BINOMIAL:
                    strLeft = list.get(leftIndex).getBinomial();
                    strRight = list.get(rightIndex).getBinomial();
                    break;
                case ENDANGERED:
                    strLeft = list.get(leftIndex).getEndangeredLevel();
                    strRight = list.get(rightIndex).getEndangeredLevel();
                    break;
                default:
                    return;
            }

            //see if leftIndex is "smaller"
            if(strLeft.compareTo(strRight) <= 0){
                temp.set(mergedIndex, list.get(leftIndex));
                leftIndex++;
            }
            else{
                temp.set(mergedIndex, list.get(rightIndex));
                rightIndex++;
            }
            mergedIndex++;
        }

        //if right list is empty
        while(leftIndex <= middleStart){
            temp.set(mergedIndex, list.get(leftIndex));
            mergedIndex++;
            leftIndex++;
        }
        //if eft list is empty
        while (rightIndex <= rightEnd){
            temp.set(mergedIndex, list.get(rightIndex));
            mergedIndex++;
            rightIndex++;
        }

        //copy temp list into given list
        for(int i = leftStart; i <= righStart; i++){
            list.set(i, temp.get(i));
        }
    }

    private static void stringsMergeDescend(ArrayList<Animal> list, int leftStart, int middleStart,
                                           int righStart, SORT_TYPE sortType){
        //initialize variables
        ArrayList<Animal> temp = new ArrayList<Animal>();
        //make temp to be the same size
        for(int i=0; i<list.size(); i++){
            temp.add(new Animal());
        }

        //initialize variables
        int leftIndex = leftStart;
        int rightIndex = middleStart + 1;
        int rightEnd = righStart;
        int mergedIndex = leftIndex;

        //start merging left & right lists
        while (leftIndex <= middleStart && rightIndex <= rightEnd){
            String strLeft;
            String strRight;
            //TODO get BINOMIAL or population based on SORT_TYPE
            switch (sortType){
                case BINOMIAL:
                    strLeft = list.get(leftIndex).getBinomial();
                    strRight = list.get(rightIndex).getBinomial();
                    break;
                case ENDANGERED:
                    strLeft = list.get(leftIndex).getEndangeredLevel();
                    strRight = list.get(rightIndex).getEndangeredLevel();
                    break;
                default:
                    return;
            }

            //see if leftIndex is "smaller"
            if(strLeft.compareTo(strRight) >= 0){
                temp.set(mergedIndex, list.get(leftIndex));
                leftIndex++;
            }
            else{
                temp.set(mergedIndex, list.get(rightIndex));
                rightIndex++;
            }
            mergedIndex++;
        }

        //if right list is empty
        while(leftIndex <= middleStart){
            temp.set(mergedIndex, list.get(leftIndex));
            mergedIndex++;
            leftIndex++;
        }
        //if eft list is empty
        while (rightIndex <= rightEnd){
            temp.set(mergedIndex, list.get(rightIndex));
            mergedIndex++;
            rightIndex++;
        }

        //copy temp list into given list
        for(int i = leftStart; i <= righStart; i++){
            list.set(i, temp.get(i));
        }
    }

    private static void integersMerge(ArrayList<Animal> list, int leftStart, int middleStart,
                                      int righStart, SORT_TYPE sortType){

    }
}
