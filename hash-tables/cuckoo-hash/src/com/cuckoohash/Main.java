package com.cuckoohash;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class Main {
    public static void main(String[] args) {
        int tableEntries = 1000;
        int numOfFlows = 1000;
        int numberOfHashes = 3;
        int numbOfSteps = 2;

        //generate hashes
        int[] hashArray = new int[numberOfHashes];
        RandomGen randomGen = new RandomGen(tableEntries);
        for (int i = 0; i < numberOfHashes; i++) {
            int x = randomGen.generate();
            hashArray[i] = x;
        }

        // fill the hash table
        int[][] hashTable = new int[tableEntries][2];
        RandomGen flowIdGen = new RandomGen(numOfFlows);
        for (int i = 0; i < numOfFlows; i++) {
            boolean placed = false;
            int flowId = flowIdGen.generate();
            for (int hashIndex = 0; hashIndex < numberOfHashes; hashIndex++) {
                int tableIndex = getHash(flowId ^ hashArray[hashIndex]) % tableEntries;

                if (hashTable[tableIndex][0] == 0) {
                    hashTable[tableIndex][0] = flowId;
                    hashTable[tableIndex][1] = 1;
                    placed = true;
                    break;
                } else if (hashTable[tableIndex][0] == flowId) { //we do not expect to enter this branch as flowIds are going to be unique
                    //if entry belongs to same flow id then also place there
                    hashTable[tableIndex][1] += 1;
                    placed = true;
                    break;
                }
            }

            // the entry can't be added directly, try moving existing
            if (!placed) {
                if (move(hashTable, hashArray, flowId, numbOfSteps)) {
//                    System.out.println("moved successfully");;
                }
            }
        }

        //get the number of non-empty slots in hash table
        int count = 0;
        for (int[] entry : hashTable) {
            if (entry[0] != 0) {
                count++;
            }
        }
        System.out.println(count);

        for (int[] entry : hashTable)
            System.out.println(entry[0]);
    }


    static boolean move(int[][] hashTable, int[] hashArray, int flowId, int numOfSteps) {
        if (numOfSteps < 1)
            return false;

        int tableEntries = hashTable.length;
        int numOfHashes = hashArray.length;
        //try swap for each hash collisions
        for (int hashIndex = 0; hashIndex < numOfHashes; hashIndex++) {
            int tableIndex = getHash(flowId ^ hashArray[hashIndex]) % tableEntries;

            int flowIdToSwap = hashTable[tableIndex][0];
            // if hash is the current location check for next swappable location
            if (flowIdToSwap == flowId)
                continue;
            boolean swapped = false;
            for (int j : hashArray) {
                int tempIndex = getHash(flowIdToSwap ^ j) % tableEntries;
                if (hashTable[tempIndex][0] == 0) {
                    //swappable location found copy the content
                    hashTable[tempIndex][0] = flowIdToSwap;
                    hashTable[tempIndex][1] = 1;
                    swapped = true;
                    break;
                }
            }
            //copy the flow id to swapped location
            if (swapped) {
                hashTable[tableIndex][0] = flowId;
                hashTable[tableIndex][1] = 1;
                return true;
            } else {
                if (move(hashTable, hashArray, flowIdToSwap, numOfSteps - 1)) {
                    hashTable[tableIndex][0] = flowId;
                    hashTable[tableIndex][1] = 1;
                    return true;
                }
            }
        }
        return false;
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
