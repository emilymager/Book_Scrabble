package test;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Dictionary {
    public CacheManager cmForExistingWords;
    public CacheManager cmForNoneExistingWords;
    public BloomFilter bloomFilter;
    public IOSearcher ioSearcher;


    public void addWordsToBloomFilter(String ... fileNames)
    {
        Scanner filesScanner = null;
        try {
            for (String fileName : fileNames) {
                filesScanner = new Scanner(new BufferedReader(new FileReader(fileName)));
                while (filesScanner.hasNext()) {
                    bloomFilter.add(filesScanner.next());
                }
                filesScanner.close();
            }
        }

        catch (FileNotFoundException f) {
            System.err.println("File not found: " + f.getMessage());
        }

        finally {
            if (filesScanner != null) {
                filesScanner.close();
            }
        }
    }

    public Dictionary(String ... fileNames) {
        this.cmForExistingWords = new CacheManager(400, new LRU());
        this.cmForNoneExistingWords = new CacheManager(100, new LFU());
        this.bloomFilter = new BloomFilter(256, "SHA1", "MD5");
         this.ioSearcher = new IOSearcher(fileNames);
        addWordsToBloomFilter(fileNames);
    }

    public boolean query(String word)
    {
        if(cmForExistingWords.query(word)) {
            cmForExistingWords.add(word);
            return true;
        }


        if(cmForNoneExistingWords.query(word)) {
            cmForNoneExistingWords.add(word);
            return false;
        }

        if(bloomFilter.contains(word))
        {
            cmForExistingWords.add(word);
            return true;
        }
        cmForNoneExistingWords.add(word);
        return false;
    }

    public boolean challenge(String word)
    {
        try
        {
            boolean bool = ioSearcher.search(word, ioSearcher.fileNames) ;
            if(bool)
            {
                cmForExistingWords.add(word);
                return true;
            }
            cmForNoneExistingWords.add(word);
            return false;
        }

        catch (FileNotFoundException e) {
            return false;
        }
    }

}
