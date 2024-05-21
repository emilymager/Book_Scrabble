package test;


import java.util.HashMap;
import java.util.Map;

public class LRU implements CacheReplacementPolicy {

    public HashMap<String, Integer> map;
    public int lastStrAddTime = 0;// word, time

    public LRU() {
        this.map = new HashMap<>();
    }

    public void add(String word){
        lastStrAddTime++;
        map.put(word, lastStrAddTime);
    }
    public String remove(){
        String minKey = null;
        int minValue = Integer.MAX_VALUE;

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() < minValue) {
                minKey = entry.getKey();
                minValue = entry.getValue();
            }
        }

        map.remove(minKey);

        return minKey;
    }
}
/*
A,B,C,A

hashMap, that will hold an integer called time, and the needed string.
each time I add a string, I increase its time by one.

in the example, at first it will be:
1,2,3
A,B,C

and then when we will add A once again, it will be
4,2,3

so when we'll use the method "remove", we will take the string with the min time, and remove  it, in this case,
we will remove B.
*/
