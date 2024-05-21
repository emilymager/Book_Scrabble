package test;


import java.util.HashSet;

public class CacheManager {
    public static int maxCacheSize;
    public HashSet<String> cacheWords;
    CacheReplacementPolicy crp;

    public CacheManager(int maxCacheSize, CacheReplacementPolicy crp) {
        this.maxCacheSize = maxCacheSize;
        this.cacheWords = new HashSet<>();
        this.crp = crp;
    }

    public boolean query(String word)
    {
        return cacheWords.contains(word);
    }

    public void add(String word) {
        crp.add(word);
        cacheWords.add(word);

        if (cacheWords.size() > maxCacheSize)
        {
            String wordToRemove = crp.remove();
            cacheWords.remove(wordToRemove);
        }
    }
}
