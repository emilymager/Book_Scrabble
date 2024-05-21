package test;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class IOSearcher {

    public static boolean search(String word, String ... fileNames) throws FileNotFoundException
    {
        Scanner filesScanner = null;
        try {
            for (String fileName : fileNames) {
                filesScanner = new Scanner(new BufferedReader(new FileReader(fileName)));
                while (filesScanner.hasNextLine()) {
                    String line = filesScanner.nextLine();
                    if (line.contains(word)) {
                        return true;
                    }
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
        return false;
    }
}
