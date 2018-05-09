package animap.animap;

import java.util.ArrayList;

/**
 *  Created by Chandler on 1/23/2018.
 *
 *  This class sorts ArrayList's holding Animal objects. It can sort based off of the scientific
 *  name (binomial), common name, mass, and threat level. It can sort each of these attributes
 *  ascending or descending.
 */

/**
 *  This determines what attribute of an animal is being sorted. Based on this enumeration, the
 *  AnimalSort class will sort by the binomial if the enumeration is BINOMIAL and so on.
 */
enum SORT_TYPE {
    BINOMIAL,
    COMMON_NAME,
    THREAT_LEVEL,
    MASS
}

public class AnimalSort {
    public AnimalSort(){}

    //------- Sorting ArrayLists

    /**
     *  This is the sorting method that will be used in other classes. It can sort in ascending or
     *  descending order based off of common name, scientific name (binomial), mass, or threat
     *  level.
     *
     * @param list      the list to be sorted
     * @param sortType  the attribute it's sorting off of
     * @param order     0 means ascending order and 1 means descending order
     * @return          the sorted list
     */
    public ArrayList<Animal> sort(ArrayList<Animal> list, SORT_TYPE sortType, int order){
        mergesort(list, 0, list.size()-1, sortType, order);

        return list;
    }

    /**
     *  This is the overlying method of mergesort where it divides and uses the merge method to
     *  recombine method.
     *
     * @param list      the list to be sorted
     * @param i         the start of the "left" sub-list
     * @param j         the end of the "right" sub-list
     * @param sortType  the attribute it's sorting by
     * @param order     indicates if it's sorting in ascending or descending order: 0=ascending
     *                  1 = descending
     */
    public static void mergesort(ArrayList<Animal>  list, int i, int j, SORT_TYPE sortType,
                                 int order){
        int mid = 0;

        // i must always be smaller than j
        if(i < j) {
            // determine the midpoint of the list, so it can be split in half
            mid = (i + j) / 2;

            // sor the sub lists
            mergesort(list, i, mid, sortType, order);
            mergesort(list, mid+1, j, sortType, order);

            //-------- Combining the Two Lists in Sorted Order
            // check if the values are being sorted by strings (binomial or common name) or by
            // numbers (mass or threat level)
            if(sortType == SORT_TYPE.BINOMIAL || sortType == SORT_TYPE.COMMON_NAME){
                // determine if the list needs to be sorted in ascending or descending order
                if(order == 0){
                    stringsMergeAscend(list, i, mid, j, sortType);
                }
                else{
                    stringsMergeDescend(list, i, mid, j, sortType);
                }
            }
            else if(sortType == SORT_TYPE.MASS || sortType == SORT_TYPE.THREAT_LEVEL){
                // determine if the list needs to be sorted in ascending or descending order
                if(order == 0){
                    integersMergeAscend(list, i, mid, j, sortType);
                }
                else{
                    integersMergeDescend(list, i, mid, j, sortType);
                }
            }
        }
    }

    /**
     *  Merges the two sub-lists that mergesort created into sorted order. This method sorts strings
     *  and can only sort by scientific name (binomial) and common name. It sorts them in
     *  ascending order.
     *
     * @param list          the list to be sorted
     * @param leftStart     the start of the left list
     * @param middleStart   the start of the right list
     * @param rightEnd      the end of the right list
     * @param sortType      the attribute that it's being sorted on
     */
    private static void stringsMergeAscend(ArrayList<Animal> list, int leftStart, int middleStart,
                                           int rightEnd, SORT_TYPE sortType){
        //initialize variables
        ArrayList<Animal> temp = new ArrayList<Animal>();
        //make temp to be the same size
        for(int i=0; i<list.size(); i++){
            temp.add(new Animal());
        }

        //initialize variables
        int leftIndex = leftStart;
        int rightIndex = middleStart + 1;
        int mergedIndex = leftIndex;

        //start merging left & right lists
        while (leftIndex <= middleStart && rightIndex <= rightEnd){
            String strLeft = "";
            String strRight = "";

            switch(sortType){
                case COMMON_NAME:
                    strLeft = list.get(leftIndex).getName();
                    strRight = list.get(rightIndex).getName();
                    break;

                case BINOMIAL:
                    strLeft = list.get(leftIndex).getBinomial();
                    strRight = list.get(rightIndex).getBinomial();
                    break;
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
        for(int i = leftStart; i <= rightEnd; i++){
            list.set(i, temp.get(i));
        }
    }

    /**
     *  Merges the two sub lists that mergesort created into sorted order in descending order. It
     *  can only sort based off of common name and scientific name (binomial)
     *
     * @param list          the list to be sorted
     * @param leftStart     the starting index of the left sub-list
     * @param middleStart   the starting index of the right sub-list
     * @param rightEnd      the end index of the right sub-list
     * @param sortType      the attribute it should be sorting by. Can only sort by common name and
     *                      scientific name (binomial)
     */
    private static void stringsMergeDescend(ArrayList<Animal> list, int leftStart, int middleStart,
                                            int rightEnd, SORT_TYPE sortType){
        //initialize variables
        ArrayList<Animal> temp = new ArrayList<Animal>();
        //make temp to be the same size
        for(int i=0; i<list.size(); i++){
            temp.add(new Animal());
        }

        //initialize variables
        int leftIndex = leftStart;
        int rightIndex = middleStart + 1;
        int mergedIndex = leftIndex;

        //start merging left & right lists
        while (leftIndex <= middleStart && rightIndex <= rightEnd){
            String strLeft = "";
            String strRight = "";

            switch(sortType){
                case COMMON_NAME:
                    strLeft = list.get(leftIndex).getName();
                    strRight = list.get(rightIndex).getName();
                    break;

                case BINOMIAL:
                    strLeft = list.get(leftIndex).getBinomial();
                    strRight = list.get(rightIndex).getBinomial();
                    break;
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
        for(int i = leftStart; i <= rightEnd; i++){
            list.set(i, temp.get(i));
        }
    }

    /**
     *  Merges the two sub-lists created by mergesort in sorted order in ascending order. It can
     *  only sort lists with numbers. This means that it can only sort by mass and threat level.
     *
     * @param list          the list to be sorted
     * @param leftStart     the beginning index of the left sub-list
     * @param middleStart   the beginning index of the right sub-list
     * @param rightStart    the end index of the right sub-list
     * @param sortType      the attribute it should be sorting by. Can only sort by threat level
     *                      and mass.
     */
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

    /**
     *   Merges the two sub-lists created by mergesort in sorted order in descending order. It can
     *   only sort lists with numbers. This means that it can only sort by mass and threat level.
     *
     * @param list          the list to be sorted
     * @param leftStart     the beginning index of the left sub-list
     * @param middleStart   the beginning index of the right sub-list
     * @param rightStart    the end index of the right sub-list
     * @param sortType      the attribute it should be sorting by. Can only sort by threat level
     *                      and mass
     */
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
