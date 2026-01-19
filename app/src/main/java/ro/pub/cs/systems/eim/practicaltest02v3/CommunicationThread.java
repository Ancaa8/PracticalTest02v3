package ro.pub.cs.systems.eim.practicaltest02v3;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

public class CommunicationThread extends Thread {

    private Socket socket;

    private ServerThread serverThread;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }


    @Override
    public void run() {
        try {
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer =
                    new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            // TODO 1: CITEȘTE DATELE DE LA CLIENT
            // String a = reader.readLine();
            // String b = reader.readLine();

            String word = reader.readLine();

            if (word == null) {
                socket.close();
                return;
            }


            // TODO 2: PRELUCREAZĂ DATELE LOCAL/internet
            // (fără internet, doar logică)

            // String result = ...;

            String response = serverThread.getFromCache(word);

            if (response == null) {
                Log.d("CACHE", "MISS for word: " + word);

                String urlString =
                        "https://api.dictionaryapi.dev/api/v2/entries/en/"
                                + word;

                URL url = new URL(urlString);
                HttpURLConnection connection =
                        (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader httpReader =
                        new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder result = new StringBuilder();
                String line;
                while ((line = httpReader.readLine()) != null) {
                    result.append(line);
                }


                try {
                    JSONArray jsonArray = new JSONArray(result.toString());
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    JSONArray meaningsArray = jsonObject.getJSONArray("meanings");
                    JSONObject firstMeaning = meaningsArray.getJSONObject(0);
                    JSONArray definitionsArray = firstMeaning.getJSONArray("definitions");
                    JSONObject firstDefinition = definitionsArray.getJSONObject(0);
                    String definition = firstDefinition.getString("definition");
                    response = definition;


                } catch (Exception e) {
                    response = "No definition found for " + word;
                }

                serverThread.putInCache(word, response);
            }else {
                Log.d("CACHE", "HIT for word: " + word);
            }

            // TODO 3: TRIMITE RĂSPUNSUL
            // writer.println(result);
            writer.println(response.toString());
            Log.d("DEFINITION", "definition: " + response);

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

