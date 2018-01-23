package ecolocation.ecolocation;

import java.util.ArrayList;

/**
 * Created by Chandler on 1/23/2018.
 */

enum SORT_TYPE {
    BINOMIAL,
    THREAT_LEVEL,
    MASS,
    POPULATION
}

public class AnimalSort {
    public AnimalSort(){}

    /// /------- Sorting ArrayLists
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
            if(sortType == SORT_TYPE.BINOMIAL){
                if(order == 0){
                    stringsMergeAscend(list, i, mid, j, sortType);
                }
                else{
                    stringsMergeDescend(list, i, mid, j, sortType);
                }
            }
            else if(sortType == SORT_TYPE.MASS || sortType == SORT_TYPE.POPULATION
                    || sortType == SORT_TYPE.THREAT_LEVEL){
                if(order == 0){
                    integersMergeAscend(list, i, mid, j, sortType);
                }
                else{
                    integersMergeDescend(list, i, mid, j, sortType);
                }
            }
        }
    }

    private static void stringsMergeAscend(ArrayList<Animal> list, int leftStart, int middleStart,
                                           int rightStart, SORT_TYPE sortType){
        //initialize variables
        ArrayList<Animal> temp = new ArrayList<Animal>();
        //make temp to be the same size
        for(int i=0; i<list.size(); i++){
            temp.add(new Animal());
        }

        //initialize variables
        int leftIndex = leftStart;
        int rightIndex = middleStart + 1;
        int rightEnd = rightStart;
        int mergedIndex = leftIndex;

        //start merging left & right lists
        while (leftIndex <= middleStart && rightIndex <= rightEnd){
            String strLeft = list.get(leftIndex).getBinomial();
            String strRight = list.get(rightIndex).getBinomial();

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
        for(int i = leftStart; i <= rightStart; i++){
            list.set(i, temp.get(i));
        }
    }

    private static void stringsMergeDescend(ArrayList<Animal> list, int leftStart, int middleStart,
                                            int rightStart, SORT_TYPE sortType){
        //initialize variables
        ArrayList<Animal> temp = new ArrayList<Animal>();
        //make temp to be the same size
        for(int i=0; i<list.size(); i++){
            temp.add(new Animal());
        }

        //initialize variables
        int leftIndex = leftStart;
        int rightIndex = middleStart + 1;
        int rightEnd = rightStart;
        int mergedIndex = leftIndex;

        //start merging left & right lists
        while (leftIndex <= middleStart && rightIndex <= rightEnd){
            String strLeft = list.get(leftIndex).getBinomial();
            String strRight = list.get(rightIndex).getBinomial();

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
        for(int i = leftStart; i <= rightStart; i++){
            list.set(i, temp.get(i));
        }
    }

    private static void integersMergeAscend(ArrayList<Animal> list, int leftStart, int middleStart,
                                            int rightStart, SORT_TYPE sortType){
        //initialize variables
        ArrayList<Animal> temp = new ArrayList<Animal>();
        //make temp to be the same size
        for(int i=0; i<list.size(); i++){
            temp.add(new Animal());
        }

        //initialize variables
        int leftIndex = leftStart;
        int rightIndex = middleStart + 1;
        int rightEnd = rightStart;
        int mergedIndex = leftIndex;

        //start merging left & right lists
        while (leftIndex <= middleStart && rightIndex <= rightEnd){
            double leftInt;
            double rightInt;
            switch (sortType){
                case MASS:
                    leftInt = list.get(leftIndex).getMass();
                    rightInt = list.get(rightIndex).getMass();
                    break;
                case POPULATION:
                    leftInt = list.get(leftIndex).getPopulation();
                    rightInt = list.get(rightIndex).getPopulation();
                    break;
                case THREAT_LEVEL:
                    leftInt = list.get(leftIndex).getThreatLevel().getValue();
                    rightInt = list.get(rightIndex).getThreatLevel().getValue();
                    break;
                default:
                    return;
            }

            //see if leftIndex is "smaller"
            if(leftInt <= rightInt){
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
        for(int i = leftStart; i <= rightStart; i++){
            list.set(i, temp.get(i));
        }
    }

    private static void integersMergeDescend(ArrayList<Animal> list, int leftStart, int middleStart,
                                             int rightStart, SORT_TYPE sortType){
        //initialize variables
        ArrayList<Animal> temp = new ArrayList<Animal>();
        //make temp to be the same size
        for(int i=0; i<list.size(); i++){
            temp.add(new Animal());
        }

        //initialize variables
        int leftIndex = leftStart;
        int rightIndex = middleStart + 1;
        int rightEnd = rightStart;
        int mergedIndex = leftIndex;

        //start merging left & right lists
        while (leftIndex <= middleStart && rightIndex <= rightEnd){
            double leftInt;
            double rightInt;
            switch (sortType){
                case MASS:
                    leftInt = list.get(leftIndex).getMass();
                    rightInt = list.get(rightIndex).getMass();
                    break;
                case POPULATION:
                    leftInt = list.get(leftIndex).getPopulation();
                    rightInt = list.get(rightIndex).getPopulation();
                    break;
                case THREAT_LEVEL:
                    leftInt = list.get(leftIndex).getThreatLevel().getValue();
                    rightInt = list.get(rightIndex).getThreatLevel().getValue();
                    break;
                default:
                    return;
            }

            //see if leftIndex is "smaller"
            if(leftInt >= rightInt){
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
        for(int i = leftStart; i <= rightStart; i++){
            list.set(i, temp.get(i));
        }
    }
}
