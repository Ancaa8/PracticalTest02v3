package ro.pub.cs.systems.eim.practicaltest02v3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PracticalTest02MainActivityv3 extends AppCompatActivity {

    EditText address;
    EditText port;

    Button connect;

    EditText word;


    Button execute;

    TextView result;

    ServerThread serverThread = null;

    int port_number;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practical_test02v3_main);


        port= findViewById(R.id.port);
        connect = findViewById(R.id.connect);
        word = findViewById(R.id.word);
        execute = findViewById(R.id.execute);
        result = findViewById(R.id.result);
        address = findViewById(R.id.address);

        // TODO: leagă UI (EditText, Button, Spinner, TextView)

        // TODO: START SERVER
        // serverThread = new ServerThread(port);
        // serverThread.start();

        connect.setOnClickListener(v -> {
            port_number= Integer.parseInt(port.getText().toString());
            serverThread = new ServerThread(port_number);
            serverThread.start();
        });



        // TODO: PORNEȘTE CLIENT
        // new ClientThread(...).start();

        execute.setOnClickListener(v -> {

            String w = word.getText().toString();

            if (w.isEmpty()) {
                result.setText("Missing data");
                return;
            }

//            ClientThread clientThread = new ClientThread(
//                    "127.0.0.1",
//                    port_number,
//                    w,
//                    result
//            );
//
//            clientThread.start();
        });


    }

    @Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}