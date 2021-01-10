package com.activecounter;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        int n = 1000000;
        Random random=new Random();

        char numberBits = 0;
        char exponentBits = 0;
        char numberBitOverFlowLimit= (char) (Math.pow(2,16)-1);
        char exponentBitOverFlowLimit= (char) (Math.pow(2,16)-1);

        for (int i = 0; i < n; i++) {
            // increment number bits with probability
            if(random.nextDouble() <  (1/Math.pow(2, exponentBits))){
                // if number bits overflows then increment exponent bits and right shift number bits
                if(numberBits==numberBitOverFlowLimit){
                    exponentBits++;
                    if(exponentBits==exponentBitOverFlowLimit){ // we will never enter this block for given n=1000000
                        System.out.println("exponent bit overflow");
                        System.exit(1);
                    }
                    numberBits= (char)((int)numberBits>>1);
                }
                else numberBits++;
            }
        }

        System.out.println((int)(numberBits*Math.pow(2,exponentBits)));
    }
}
