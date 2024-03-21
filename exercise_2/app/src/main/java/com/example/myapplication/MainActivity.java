package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.myapplication.R; // Make sure to import R from your package

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    ImageView imageView;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editTextText);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        constraintLayout = findViewById(R.id.constraintLayout);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                new SentimentAnalysisTask().execute(text);
            }
        });
    }

    private class SentimentAnalysisTask extends AsyncTask<String, Void, Double> {

        private static final String API_KEY = "ed113a4a4f6e4b52ae6f3d805c91b2ad";
        private static final String API_URL = "https://myresourcegroupdemo.cognitiveservices.azure.com/text/analytics/v2.0/sentiment";

        @Override
        protected Double doInBackground(String... strings) {
            String text = strings[0];
            Double sentimentScore = null;

            try {
                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Ocp-Apim-Subscription-Key", API_KEY);
                connection.setDoOutput(true);

                String requestBody = "{\"documents\": [{\"language\": \"en\",\"id\": \"1\",\"text\": \"" + text + "\"}]}";

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(requestBody.getBytes(StandardCharsets.UTF_8));
                outputStream.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONArray documents = jsonResponse.getJSONArray("documents");
                    JSONObject document = documents.getJSONObject(0);
                    sentimentScore = document.getDouble("score");
                }

                connection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return sentimentScore;
        }

        @Override
        protected void onPostExecute(Double score) {
            if (score != null) {
                if (score >= 0.5) { // Positive sentiment
                    constraintLayout.setBackgroundColor(getResources().getColor(R.color.green));
                    imageView.setImageResource(R.drawable.happy_icon);
                } else { // Negative sentiment
                    constraintLayout.setBackgroundColor(getResources().getColor(R.color.red));
                    imageView.setImageResource(R.drawable.sad_icon);
                }
            }
            Log.d("SentimentScore", "Score: " + score);
        }
    }
}
