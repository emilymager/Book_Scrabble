package test;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
            return true;
        }


        if(cmForNoneExistingWords.query(word)) {
            return false;
        }

        boolean wordExistsInBloomFilter = bloomFilter.contains(word);
        if(wordExistsInBloomFilter)
            cmForExistingWords.add(word);

        else
            cmForNoneExistingWords.add(word);

        return wordExistsInBloomFilter;
    }

    public boolean challenge(String word)
    {
        try
        {
            boolean bool = ioSearcher.search(word, ioSearcher.fileNames) ;
            if(bool)
                cmForExistingWords.add(word);

            else
                cmForNoneExistingWords.add(word);
            return bool;
        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

    }

}
