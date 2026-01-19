package ro.pub.cs.systems.eim.practicaltest02v3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread extends Thread {

    private ServerSocket serverSocket;
    private int port;

    private HashMap<String, String> cache = new HashMap<>();

    public synchronized String getFromCache(String word) {
        return cache.get(word);
    }

    public synchronized void putInCache(String word, String definition) {
        cache.put(word, definition);
    }


    public ServerThread(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                Socket socket = serverSocket.accept();
                new CommunicationThread(this, socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopThread() {
        interrupt();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

