package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MyServer {
    private int port;
    private volatile boolean stop;
    private ClientHandler ch;
    private ServerSocket serSoc;

    public MyServer(int port, ClientHandler ch)
    {
        this.port = port;
        stop = false;
        this.ch = ch;
    }

    public void start(){
        new Thread(()-> {
            try {
                runServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void runServer() throws Exception {
        serSoc = new ServerSocket(port);
        serSoc.setSoTimeout(1000);

        while(!stop)
        {
            try
            {
                Socket aClient = serSoc.accept();
                InputStream in = aClient.getInputStream();
                OutputStream out = aClient.getOutputStream();

                ch.handleClient(in, out);
                in.close();
                out.close();
                aClient.close();
            }
            catch(SocketTimeoutException e)
            {
                e.printStackTrace();
            }
            catch(IOException e)
            {
                if(!serSoc.isClosed())
                    e.printStackTrace();
            }
        }
    }

    public void close() {
        stop = true;
        try {
            if (serSoc != null && !serSoc.isClosed()) {
                serSoc.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
