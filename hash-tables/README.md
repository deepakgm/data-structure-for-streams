# hash-tables
Implementation of multi-hash, cuckoo hash and d-left hash tables in java

Instructions to execute (in Linux):
Multi-Hash:
```shell script
 cd multi-hash/src/com/multihash/ && java Main.java && cd ../../../..
```
Cuckoo-Hash: 
```shell script
cd cuckoo-hash/src/com/cuckoohash/ && java Main.java && cd ../../../..
```
D-Left Hash: 
```shell script
cd dleft-hash/src/com/dleft/ && java Main.java && cd ../../../..
```

Multi-hashing table:
 * hashTable is a 2d array with for i-th entry hashTable[i][0] is flow_id and hashTable[i][1] is flow_count. In our use-case hashTable[i][1] will be at most 1.
 * creates hashArray with given number of random numbers to create multiple hash functions for a single flow_id
 * A set of Random numbers are generated as flow ids. For each flow_id and each value in the hashArray a index in the hashTable is calculated as following
     hashTable_index = Hash(random_num XOR hashArray_value) mod (hashTable_size)
 * if entry in hashTable_index is null, then we update the flow_id, set count to 1 and break hashArray loop.

Cuckoo hash table:
* hashTable is a 2d array with for i-th entry hashTable[i][0] is flow_id and hashTable[i][1] is flow_count. In our use-case hashTable[i][1] will be at most 1.
 * creates hashArray with given number of random numbers to create multiple hash functions for a single flow_id
 * A set of Random numbers are generated as flow ids. For each flow_id and each value in the hashArray a index in the hashTable is calculated as following
     hashTable_index = Hash(random_num XOR hashArray_value) mod (hashTable_size)
 * if entry in hashTable_index is null, then we update the flow_id, set count to 1 and break hashArray loop. Otherwise we call move method. If it returns true we update entry and break the loop.
 * In the previous step we have checked for a given flow id, all the indices pointed by the hash functions already have a entry in them. So here we tried to move them to different index. We loop the flowid to be swapped through the hash array and if some free entry is found we swap it. Otherwise we call move function recursively by decreasing number of steps.
 * In the starting of recursive function move we check if num of steps is valid to prevent infinite loops

 D-Left Hash Table:
 * hashTable is a 3d array with hashTable[i] is the hash table for i-th segment and hashTable[i][j][0] is flow_id and hashTable[i][j][1] is flow_count for j-th entry in the i-th hash table. In our use-case hashTable[i][j][1] will be at most 1.
 * creates hashArray with given number of random numbers (equal to number of segments) to create multiple hash functions for a single flow_id
 * A set of Random numbers are generated as flow ids. For each flow_id and each value in the hashArray a index in the hashTable is calculated as following
     hashTable_index = Hash(random_num XOR hashArray_value) mod (hashTable_size)
 * if entry in hashTable_index is null, then we update the flow_id, set count to 1 and break the loop. Otherwise we move on to next segment and calculate hashTable_index again and check.


 getHash method:
 * Takes integer input and generates a hash using bit shifts and multiplication with a prime number

 RandomGen Class:
 * Utility to generate Random Integer using Random.class.
 * Contains a Set to make sure the random numbers generated are unique.`