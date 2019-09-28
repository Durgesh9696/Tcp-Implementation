package com.durgeshparekh.tcp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    TcpClient mTcpClient;
    EditText text, ipText;
    private static String SERVER_IP; //server IP address
    private static final int SERVER_PORT = 13002;
    private TextView tvResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.et_text);
        ipText = findViewById(R.id.et_ip_text);
        tvResponse = findViewById(R.id.response);
    }


    public void onConnectClicked(View view) {
        SERVER_IP = ipText.getText().toString();

        // connect to the server
       new ConnectTask().execute("");
       Toast.makeText(MainActivity.this, "Connected to "+ ipText.getText().toString(), Toast.LENGTH_SHORT).show();

    }

    public void onDisconnectClicked(View view) {
        if (mTcpClient != null) {
            mTcpClient.stopClient();
            Toast.makeText(this, "Connection Closed", Toast.LENGTH_SHORT).show();
            tvResponse.setText("Disconnected");

        }
    }

    public void onSendClicked(View view) {
        String msg = text.getText().toString();
        //sends the message to the server
        if (mTcpClient != null) {
            mTcpClient.sendMessage(msg);
        }

    }

    public void onClearClicked(View view) {
        tvResponse.setText("");

    }


    @SuppressLint("StaticFieldLeak")
    public class ConnectTask extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            mTcpClient.run(SERVER_IP);

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.e("onProgressUpdate: ","Called...." );
            //response received from server
            Log.e("test", "response " + Arrays.toString(values));
            tvResponse.setText(values[0]);
            //process server response here....

        }
    }
}
