package test;

import java.util.HashMap;
import java.util.Map;

public class DictionaryManager {
    private static DictionaryManager dictionaryManager;
    private HashMap<String, Dictionary> map;

    public DictionaryManager(HashMap<String, Dictionary> map) {
        this.map = map;
    }

    public DictionaryManager() {
        this.map = new HashMap<>(); // Initialize the HashMap
    }

    public boolean query(String ... args)
    {
        int argsLength = args.length;
        String word = args[argsLength - 1];
        boolean bool= false;
        int index = 0;

        while(index < args.length - 1)
        {
            if(!map.containsKey(args[index]))
                map.put(args[index], new Dictionary(args[index]));

            if(map.get(args[index]).query(word))
                bool = true;

            index++;
        }

        return bool;

    }

    public boolean challenge(String ... args)
    {
        int argsLength = args.length;
        String word = args[argsLength - 1];
        boolean bool= false;
        int index = 0;

        while(index < args.length - 1)
        {
            if(!map.containsKey(args[index]))
                map.put(args[index], new Dictionary(args[index]));

            if(map.get(args[index]).challenge(word))
                bool = true;

            index++;
        }

        return bool;
    }


    public static DictionaryManager get()
    {
        if(dictionaryManager == null)
            dictionaryManager = new DictionaryManager();

        return dictionaryManager;
    }

    public int getSize(){ return this.map.size();}

}
