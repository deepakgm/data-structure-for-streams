import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static class Flow {
        public Flow(String flowId, int distinctElement) {
            this.flowId = flowId;
            this.distinctElement = distinctElement;
            this.elements = new int[distinctElement];
            this.genRandomElements();
        }

        public Flow(String flowId) {
            this.flowId = flowId;
            this.distinctElement = 0;
            this.elements = new int[1];
            this.genRandomElements();
        }

        private void genRandomElements() {
            RandomGen randomGen = new RandomGen();
            for (int i = 0; i < this.distinctElement; i++) {
                this.elements[i] = randomGen.generate();
            }
        }

        public String flowId;
        public int[] elements;
        public int distinctElement;
        public int estimatedFlowSpread;
    }

    public static void main(String[] args) {
        int n;
        int m = 500000;
        int l = 500;
        Flow[] flows = null;
        try {
            File file = new File("project4input.txt");
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
                Flow flow;
                if (splitResult.length > 1) {
                    flow = new Flow(splitResult[0], Integer.parseInt(splitResult[1]));
                } else {
                    flow = new Flow(splitResult[0]);
                }
                flows[index++] = flow;
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("An error occurred while parsing input data");
            e.printStackTrace();
            System.exit(1);
        }

        //initialize bitmap
        boolean[] B = new boolean[m];

        //initialize hash array and fill with random values
        int[] R = new int[l];
        RandomGen randomGen = new RandomGen();
        for (int i = 0; i < l; i++) {
            int x = randomGen.generate();
            R[i] = x;
        }

        //encode elements in bitmap
        for (Flow flow : flows) {
            for (int element : flow.elements) {
                int hash1 = getHash(element) % l;
                int hash2 = getHash(getHash(flow.flowId) ^ R[hash1]) % m;
                B[hash2] = true;
            }
        }

        //calculate Vb
        int zeroCount = 0;
        for (int i = 0; i < m; i++) {
            if (!B[i]) zeroCount++;
        }
        double Vb = (double) zeroCount / (double) m;

        //calculate estimated spread
        for (Flow flow : flows) {
            int zeroCount1 = 0;
            for (int i=0;i<l;i++) { 
                int hash2 = getHash(getHash(flow.flowId) ^ R[i]) % m;
                if (!B[hash2]) zeroCount1++;
            }
            double Vf = (double) zeroCount1 / (double) l;
            double estimatedSpread = l * Math.log(Vb) - l * Math.log(Vf);
            flow.estimatedFlowSpread = (int) Math.round(estimatedSpread);
        }

        //write result to file in csv format
        try {
            FileWriter writer = new FileWriter("output.csv");
            for(Flow flow:flows) {
                if(flow.estimatedFlowSpread>0){
                    writer.write(flow.distinctElement+","+flow.estimatedFlowSpread+"\n");
                }
            }
            writer.close();
            System.out.println("output written to file 'output.csv'");
        } catch (IOException e) {
            System.out.println("An error occurred while writing output to file");
            e.printStackTrace();
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

        public RandomGen() {
            this.random = new Random();
            this.set = new HashSet<>();
            this.set.add(-1);
            this.range = Integer.MAX_VALUE;
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
