package com.dleft;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class Main {

    public static void main(String[] args) {
        int tableEntries = 1000;
        int numOfFlows = 1000;
        int numOfSegments = 4;

        //generate hashes
        int[] hashArray = new int[numOfSegments];
        RandomGen randomGen = new RandomGen(tableEntries);
        for (int i = 0; i < numOfSegments; i++) {
            int x = randomGen.generate();
            hashArray[i] = x;
        }

        // fill the hash table
        int[][][] hashTable = new int[numOfSegments][tableEntries / numOfSegments][2];
        RandomGen flowIdGen = new RandomGen(numOfFlows);
        for (int i = 0; i < numOfFlows; i++) {
            int flowId = flowIdGen.generate();
            for (int segIndex = 0; segIndex < numOfSegments; segIndex++) {
                int tableIndex = getHash(flowId ^ hashArray[segIndex]) % (tableEntries / numOfSegments);
                if (hashTable[segIndex][tableIndex][0] == 0) {
                    hashTable[segIndex][tableIndex][0] = flowId;
                    hashTable[segIndex][tableIndex][1] = 1;
                    break;
                } else if (hashTable[segIndex][tableIndex][0] == flowId) { //we do not expect to enter this branch as flowIds are going to be unique
                    //if entry belongs to same flow id then also place there
                    hashTable[segIndex][tableIndex][1] = hashTable[segIndex][tableIndex][1] + 1;
                    break;
                }
            }
        }

        //get the number of non-empty slots in hash table
        int count = 0;
        for (int[][] segment : hashTable) {
            for (int[] entry : segment) {
                if (entry[0] != 0) {
                    count++;
                }
            }
        }
        System.out.println(count);

        for (int[][] segment : hashTable)
            for (int[] entry : segment)
                System.out.println(entry[0]);
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
