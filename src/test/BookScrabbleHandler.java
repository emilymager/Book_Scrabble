package test;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class BookScrabbleHandler implements ClientHandler{
    private PrintWriter out;
    private Scanner in;

    public BookScrabbleHandler() {}

    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient)
    {
        out = new PrintWriter(outToClient);
        in = new Scanner(inFromclient);

        String line = in.nextLine();
        String[] parts = line.split(",");
        char howToSearch = parts[0].charAt(0);
        parts = Arrays.copyOfRange(parts, 1, parts.length);


        if(howToSearch == 'Q') // search word by query
        {
            out.println(String.valueOf(DictionaryManager.get().query(parts)));
            out.println();
            out.flush();
        }

        if(howToSearch == 'C') // search word by challenge
        {
            out.println(String.valueOf(DictionaryManager.get().challenge(parts)));
            out.println();
            out.flush();
        }

    }

    @Override
    public void close()
    {
        in.close();
        out.close();
    }
}
