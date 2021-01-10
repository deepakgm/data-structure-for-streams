package com.bloomfilter;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        int numOfElements = 1000;
        int numOfBits = 10000;
        int numberOfHashes = 7;

        //generate hashes
        int[] hashArray = new int[numberOfHashes];
        RandomGen randomGen = new RandomGen(numOfBits);
        for (int i = 0; i < numberOfHashes; i++) {
            int x = randomGen.generate();
            hashArray[i] = x;
        }

        boolean[] filter = new boolean[numOfBits];
        RandomGen elementGen = new RandomGen(numOfElements);

        // encode Set-A
        List<Integer> setA = new ArrayList<>(numOfElements);
        for (int i = 0; i < numOfElements; i++) {
            int element = elementGen.generate();
            setA.add(element);
            for (int hashIndex = 0; hashIndex < numberOfHashes; hashIndex++) {
                int bitIndex = getHash(element ^ hashArray[hashIndex]) % numOfBits;
                filter[bitIndex] = true;
            }
        }

        //look up set A
        int aCount = 0;
        for (int element : setA) {
            boolean flag = true;
            for (int hashIndex = 0; hashIndex < numberOfHashes; hashIndex++) {
                int bitIndex = getHash(element ^ hashArray[hashIndex]) % numOfBits;
                flag = flag && filter[bitIndex];
            }
            if (flag)
                aCount++;
        }

        // encode Set-B
        List<Integer> setB = new ArrayList<>(numOfElements);
        for (int i = 0; i < numOfElements; i++) {
            int element = elementGen.generate();
            setB.add(element);
            //not encoded in the filter
//            for (int hashIndex = 0; hashIndex < numberOfHashes; hashIndex++) {
//                int bitIndex = getHash(element ^ hashArray[hashIndex]) % numOfBits;
//                filter[bitIndex] = true;
//            }
        }

        //look up set B
        int bCount = 0;
        for (int element : setB) {
            boolean flag = true;
            for (int hashIndex = 0; hashIndex < numberOfHashes; hashIndex++) {
                int bitIndex = getHash(element ^ hashArray[hashIndex]) % numOfBits;
                flag = flag && filter[bitIndex];
            }
            if (flag)
                bCount++;
        }

        System.out.println(aCount);
        System.out.println(bCount);
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
