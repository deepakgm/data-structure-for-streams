# counters
countmin, counter sketch and active counter implementation in Java

#### Instructions to execute (in Linux):
To use a different a input file please replace the existing 'project3input.txt' in the directories 'countmin/src/com/countmin/' and 'counter-sketch/src/com/countersketch/' with the same name OR change the file path inside the code

CountMin:
```shell script
cd countmin/src/com/countmin/ && java Main.java && cd ../../../..
```

Counter Sketch: 

```shell script
cd counter-sketch/src/com/countersketch/ && java Main.java && cd ../../../..
```

Active counter: 
```shell script
cd active-counter/src/com/activecounter/ && java Main.java && cd ../../../..
```

Flow is class consisting of 3 attributes, i) flowId, ii) flowSize iii) estimatedFlowSize
flowId and flowSize are obtained from the input file and estimatedFlowSize is calculated later
CountMin:
* Input file is parsed and the data is loaded into a array of 'Flow'
* 'counter' is a int 2D array
* hashArray is an array with k random integers
* For each element in the inputData, integer hash value of string flowID is obtained and for each value in the hashArray a hash is calculated and index in counter is also calculated using modulus operator as
    index = Hash2(Hash1(flowId) XOR hashArray_value) mod w
* Value in the counter is incremented by the size from the input data in each iteration
* estimatedFlowSize is updated in each iteration whose value are calculated by choosing the corresponding minimum value in counter
* top 100 elements with largest estimated size is calculated using a priority queue

Counter Sketch:
* Input file is parsed and the data is loaded into a array of 'Flow'
* 'counter' is a int 2D array
* hashArray is an array with k random integers
* For each element in the inputData, integer hash value of string flowID is obtained and for each value in the hashArray a hash is calculated and index in counter is also calculated using modulus operator as
    index = Hash2(Hash1(flowId) XOR hashArray_value) mod w
* Depending on a specific bit of the calculated hash value, the counter is incremented or decremented by 'flowSize' from the input data in each iteration
* To calculate estimatedFlowSize for each flow the 'k' counter values from from different indices is placed into an array and median is calculated using getMedian method
* top 100 elements with largest estimated size is calculated using a priority queue

Active counter:
* Program uses char (16 bit) variables to numberBits and exponentBits represent bits. And their overflow is tracked using threshold for 16 bits
* In each iteration numberBits is increased with a probability of (1/(2^exponentBits)
* When numberBits overflows exponentBits is increased by one and bit right shift is performed on numberBits
* Final output is calculated as (numberBits * (2^exponentBits))