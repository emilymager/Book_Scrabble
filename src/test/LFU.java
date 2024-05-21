package test;


import java.util.HashMap;
import java.util.Map;

public class LFU implements CacheReplacementPolicy {

    public HashMap<String, Value> map;
    public int lastStrAddTime = 0;// word, time

    public LFU() {
        this.map = new HashMap<>();
    }

    public void add(String word){
        if(this.map.containsKey(word))
            this.map.put(word, new Value(map.get(word).time, map.get(word).frequency + 1));

        else {
            this.map.put(word, new Value(lastStrAddTime, 1));
            lastStrAddTime++;
        }
    }
    public String remove(){

        String minKey = null;
        int minFrequency = Integer.MAX_VALUE;
        int minTime = Integer.MAX_VALUE;

        for (Map.Entry<String, Value> entry : map.entrySet()) {
            if(entry.getValue().frequency < minFrequency) {
                minFrequency = entry.getValue().frequency;
                minKey = entry.getKey();
                minTime = entry.getValue().time;
            }

            if(minFrequency == entry.getValue().frequency)
            {
                if(minTime > entry.getValue().time)
                {
                    minFrequency = entry.getValue().frequency;
                    minKey = entry.getKey();
                    minTime = entry.getValue().time;
                }
            }
        }

        return minKey;
    }

    public class Value{
        public int time;
        public int frequency;

        public Value(int time, int frequency) {
            this.time = time;
            this.frequency = frequency;
        }
    }
}
