# bloom-filters
java implementation of different bloom-filters

#### Instructions to execute (in Linux):
Bloom filter:

```bash
cd bloom-filter/src/com/bloomfilter/ && java Main.java && cd ../../../..
```
Counting Bool Filter: 
```bash
cd counting-bloom-filter/src/com/countingbfilter/ && java Main.java && cd ../../../..
```
Coded Bloom Filter:
```bash
cd coded-bloom-filter/src/com/codedbfilter/ && java Main.java && cd ../../../..
```

Bloom filter:
* creates a hashArray with random integers
* creates boolean array called filter
* A set of Random numbers are generated and added to setA ArrayList
* For each random number and each value in the hashArray a index in the filter array is calculated as following
    filter_bit_index = Hash(random_num XOR hashArray_value) mod (total_#_bits_in_filter)
  And the bit is updated as true
* Lookup is performed for elements in set-A by looping through arraylist setA and for each element if all of the bit_index calculated by the above formula is set to true then count is increased
* A set of ranom numbers set-B is generated in a similar way (but not encoded in the filter) and look up is performed and counter is updated accordingly

Counting Bloom filter:
* creates a hashArray with random integers
* filter is a int array here instead of boolean array
* A set of Random numbers are generated and added to initialElements ArrayList
* For each random number and each value in the hashArray a index in the filter array is calculated as following
    filter_index = Hash(random_num XOR hashArray_value) mod (total_#_bits_in_filter)
  And the value in each filter_index and increased
* For the given number of elements in initialElements ArrayList removal operation is carried by computing the filter_index as above and decreasing the value by one
* Again a given number of elements are encoded using the above mentioned steps
* Lookup is performed for original elements by looping through initialElements ArrayList and for each element, filter_index is calculated for all hashArray values and a MINIMUM of the value in filter_index is taken and if it is greater than zero we increase the count

Coded Bloom Filter:
* creates a hashArray with random integers
* filters here is a 2-d boolean array representing a filter for each 'bloom-filter'
* codes is a 2-d boolean array containing code for each set, calculated using bit-shift operators
* referenceData is a 2-d int array used to store the generated random numbers for the later lookups
* for each set a given number of random numbers are generated and if the bit in the 'code' for the particular set is set to true, then for that corresponding filter the values are updated in the similar way as in the bloom filters
* Lookup is performed by looping through 'referenceData'. We calculate the 'calculatedCode' by performing hash lookups in each filters (in the similar way of bloom filters) and if the match is found in a filter we set the corresponding bit in the calculatedCode as true. Finally for each element the 'calculatedCode' is compared with the 'orignalCode' which is the actual code for that particular set, if both are same then count is increased.

getHash method:
* Takes integer input and generates a hash using bit shifts and multiplication with a prime number

RandomGen Class:
* Utility to generate Random Integer using Random.class.
* Contains a Set to make sure the random numbers generated are unique.
