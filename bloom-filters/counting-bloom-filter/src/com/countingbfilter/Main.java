package com.countingbfilter;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        int numOfInitElements = 1000;
        int numOfElemToRemove = 500;
        int numOfElemToAdd = 500;
        int numOfBits = 10000;
        int numberOfHashes = 7;

        //generate hashes
        int[] hashArray = new int[numberOfHashes];
        RandomGen randomGen = new RandomGen(numOfBits);
        Random random = new Random();
        for (int i = 0; i < numberOfHashes; i++) {
            int x = randomGen.generate();
            hashArray[i] = x;
        }

        int[] filter = new int[numOfBits];
        RandomGen elementGen = new RandomGen(numOfInitElements);

        // add initial elements
        List<Integer> initialElements = new ArrayList<>(numOfInitElements);
        for (int i = 0; i < numOfInitElements; i++) {
            int element = elementGen.generate();
            initialElements.add(element);
            for (int hashIndex = 0; hashIndex < numberOfHashes; hashIndex++) {
                int bitIndex = getHash(element ^ hashArray[hashIndex]) % numOfBits;
                filter[bitIndex]++;
            }
        }

        //remove elements
        int aCount = 0;
        for (int i = 0; i < numOfElemToRemove; i++) {
            int element = initialElements.get(i);
            for (int hashIndex = 0; hashIndex < numberOfHashes; hashIndex++) {
                int bitIndex = getHash(element ^ hashArray[hashIndex]) % numOfBits;
                filter[bitIndex]--;
            }
        }

        //add elements
        for (int i = 0; i < numOfElemToAdd; i++) {
            int element = elementGen.generate();
            for (int hashIndex = 0; hashIndex < numberOfHashes; hashIndex++) {
                int bitIndex = getHash(element ^ hashArray[hashIndex]) % numOfBits;
                filter[bitIndex]++;
            }
        }

        //lookup initial elements
        int count = 0;
        for (int element : initialElements) {
            int min = Integer.MAX_VALUE;
            for (int hashIndex = 0; hashIndex < numberOfHashes; hashIndex++) {
                int bitIndex = getHash(element ^ hashArray[hashIndex]) % numOfBits;
                min = Math.min(min, filter[bitIndex]);
            }
            if (min > 0)
                count++;
        }

        System.out.println(count);
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
