package com.countersketch;

import java.io.File;
import java.util.*;


public class Main {
    public static class Flow {
        public Flow(String flowId, int flowSize) {
            this.flowId = flowId;
            this.flowSize = flowSize;
        }

        public String flowId;
        public int flowSize;
        public int estimatedFlowSize;
    }

    public static void main(String[] args) {
        int k = 3;
        int w = 3000;
        int n = 0;

        Flow[] flows = null;
        try {
            File file = new File("project3input.txt");
            Scanner scanner = new Scanner(file);
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                n = Integer.parseInt(input);
                flows = new Flow[n];
            } else {
                System.out.println("empty input file");
                System.exit(1);
            }

            int index = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] splitResult = line.split("\\s+");
                Flow flow = new Flow(splitResult[0], Integer.parseInt(splitResult[1]));
                flows[index++] = flow;
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("An error occurred while parsing input data");
            e.printStackTrace();
            System.exit(1);
        }

        //initialize counter
        int[][] counter = new int[k][w];

        //initialize hash array and fill with random values
        int[] hashArray = new int[k];
        RandomGen randomGen = new RandomGen(n);
        for (int i = 0; i < k; i++) {
            int x = randomGen.generate();
            hashArray[i] = x;
        }


        // update the counter for input data
        for (Flow flow : flows) {
            //find the min counter value among corresponding indices
            for (int hashIndex = 0; hashIndex < k; hashIndex++) {
                int hash = getHash(getHash(flow.flowId) ^ hashArray[hashIndex]);
                int bitIndex = hash % w;
                if (checkFirstBitIsSet(hash)) {
//                    if (bitIndex == 5) System.out.println("hash is " + hash);
                    counter[hashIndex][bitIndex] += flow.flowSize;
                } else {
                    counter[hashIndex][bitIndex] -= flow.flowSize;
//                    if (bitIndex == 5) System.out.println("hash is -" + hash);
                }
            }
        }

        //calculate estimated data
        for (int i = 0; i < n; i++) {
            //create an array with positive/negative value from array accordingly
            int[] arr = new int[k];
            for (int hashIndex = 0; hashIndex < k; hashIndex++) {
                int hash = getHash(getHash(flows[i].flowId) ^ hashArray[hashIndex]);
                int bitIndex = hash % w;
                if (checkFirstBitIsSet(hash))
                    arr[hashIndex] = counter[hashIndex][bitIndex];
                else
                    arr[hashIndex] = -1 * counter[hashIndex][bitIndex];
            }
            flows[i].estimatedFlowSize = getMedian(arr);
        }

        /*
            Evaluate and display the result
         */

        //find flows with 100 largest largest true size and put them in a map
        PriorityQueue<Flow> priorityQueue = new PriorityQueue<>(new Comparator<Flow>() {
            @Override
            public int compare(Flow a, Flow b) {
                return b.estimatedFlowSize - a.estimatedFlowSize;
            }
        });

        for (Flow flow : flows) {
            priorityQueue.add(flow);
        }

        int sumOfErrors = 0;
        for (Flow flow : flows) {
            sumOfErrors += Math.abs(flow.estimatedFlowSize - flow.flowSize);
        }

        System.out.println((float) sumOfErrors / (float) n);

        for (int i = 0; i < 100; i++) {
            Flow flow = priorityQueue.poll();
            System.out.println(flow.flowId + " " + flow.estimatedFlowSize + " " + flow.flowSize);
        }
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

    public static int getHash(String str) {
        int h = 7;
        for (int i = 0; i < str.length(); i++) {
            h = h * 31 + str.charAt(i);
        }
        return h;
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

    //finds median in int array
    public static int getMedian(int[] arr) {
        int median;
        int len = arr.length;
        Arrays.sort(arr);
        if (len % 2 == 0)
            median = (arr[len / 2] + arr[len / 2 - 1]) / 2;
        else
            median = arr[len / 2];
        return median;
    }

    public static boolean checkFirstBitIsSet(int num) {
//        int sum=0;
//        while(num>0){
//            sum+=num%10;
//            num/=10;
//        }
//        return  sum%2==0;
//       return  (num & (1<<0)) != 0;
        return (num & (1 << 30)) != 0;
    }
}
