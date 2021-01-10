package com.codedbfilter;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        int numOfSets = 7;
        int numOfElemInSet = 1000;
        int numOfFilters = 3;
        int numOfBits = 30000;
        int numberOfHashes = 7;

        //generate hashes
        int[] hashArray = new int[numberOfHashes];
        RandomGen randomGen = new RandomGen(numOfBits);
        for (int i = 0; i < numberOfHashes; i++) {
            int x = randomGen.generate();
            hashArray[i] = x;
        }

        boolean[][] filters = new boolean[numOfFilters][numOfBits];
        boolean[][] codes = new boolean[numOfSets][numOfFilters];

        // generate codes
        for (int i = 0; i < numOfSets; i++) {
            boolean[] code = new boolean[numOfFilters];
            for (int j = numOfFilters - 1; j >= 0; j--) {
                int setId = i + 1;
                code[j] = (setId & (1 << j)) != 0;
            }
            codes[i] = code;
        }

        int[][] referenceData = new int[numOfSets][numOfElemInSet];
        RandomGen elementGen = new RandomGen(numOfSets * numOfElemInSet);

        // generate and encode data
        for (int setIndex = 0; setIndex < numOfSets; setIndex++) {
            boolean[] code = codes[setIndex];

            for (int i = 0; i < numOfElemInSet; i++) {
                int element = elementGen.generate();
                referenceData[setIndex][i] = element;

                for (int filterIndex = 0; filterIndex < numOfFilters; filterIndex++) {

                    if (code[filterIndex]) { // for filter for which the bit in code set to true
                        for (int hashIndex = 0; hashIndex < numberOfHashes; hashIndex++) {
                            int bitIndex = getHash(element ^ hashArray[hashIndex]) % numOfBits;
                            filters[filterIndex][bitIndex] = true;
                        }
                    }
                }
            }
        }


        // lookup the elements and check the result
        int correctCount = 0;
        for (int setIndex = 0; setIndex < numOfSets; setIndex++) {
            boolean[] originalCode = codes[setIndex];
            for (int i = 0; i < numOfElemInSet; i++) {
                int element = referenceData[setIndex][i];

                boolean[] calculatedCode = new boolean[numOfFilters];
                for (int filterIndex = 0; filterIndex < numOfFilters; filterIndex++) {
                    boolean flag = true;
                    for (int hashIndex = 0; hashIndex < numberOfHashes; hashIndex++) {
                        int bitIndex = getHash(element ^ hashArray[hashIndex]) % numOfBits;
                        flag = flag && filters[filterIndex][bitIndex];
                    }
                    if (flag) calculatedCode[filterIndex] = true;
                }
                // verify the calculatedCode with the originalCode
                boolean isMatch = true;
                for (int j = 0; j < numOfFilters; j++)
                    if (originalCode[j] != calculatedCode[j]) {
                        isMatch = false;
                        break;
                    }
                if (isMatch) correctCount++;
            }
        }

        System.out.println(correctCount);
    }

    public static int getHash(int num) {
        int prime = 0x42C1E0D;
        num = (num ^ 61) ^ (num >>> 16);
        num = num + (num << 3);
        num = num ^ (num >>> 4);
        num = num * prime;
        num = num ^ (num >>> 15);
        return Math.abs(num);
    }

    static class RandomGen {
        private final Random random;
        private final Set<Integer> set;
        private final int range;

        public RandomGen(int range) {
            this.random = new Random();
            this.set = new HashSet<>();
            this.set.add(-1);
            this.range = Math.max(111111111, range);
        }

        //generates unique ids
        public int generate() {
            int id = -1;
            while (this.set.contains(id)) {
                id = this.random.nextInt(this.range);
            }
            this.set.add(id);
            return id;
        }
    }
}
